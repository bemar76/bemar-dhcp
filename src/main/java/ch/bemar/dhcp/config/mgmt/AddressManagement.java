package ch.bemar.dhcp.config.mgmt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

import org.dhcp4java.HardwareAddress;

import com.google.common.collect.Sets;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.util.IPRangeCalculatorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressManagement {

	private LeaseManager leaseManager;

	private DhcpSubnetConfig subnetConfig;

	private Set<Address> addresses;

	public AddressManagement(DhcpSubnetConfig subnet) throws IOException {

		this.subnetConfig = subnet;
		this.addresses = Sets.newHashSet();

		this.leaseManager = new LeaseManager();

		log.info("Building AddressManagement DB");

		log.info("handling addresses for subnet {}", subnet);

		List<InetAddress> ips = IPRangeCalculatorUtil.calculateAllIPsInRange(subnet.getRange(),
				subnet.getSubnetAddress().getValue());

		for (InetAddress ip : ips) {

			Address a = new Address(ip, subnet.getSubnetAddress(), subnet.getDefaultLeaseTime(),
					subnet.getMaxLeaseTime());

			addresses.add(a);
			log.trace("adding ip {} to db", a);
		}

	}

	public synchronized IAddress checkReservationsOrLeasesForMac(HardwareAddress mac) throws Exception {

		for (Address address : addresses) {

			if (address.getReservedFor().equals(mac) || address.getLeasedTo().equals(mac)) {
				log.info("Found reservation or leasing for {}", mac);

				IAddress addr = leaseManager.handleReservedLeasing(address, mac);

				if (addr != null) {
					return addr;
				}

			}

		}

		log.error("No address was found for mac: {}", mac);

		return null;

	}

	public synchronized IAddress getNextAddress(HardwareAddress mac, InetSocketAddress receivedOn) throws Exception {

		for (Address address : addresses) {

			IAddress addr = leaseManager.handleNextFreeLeasing(address, mac);

			if (addr != null) {
				return addr;
			}

		}

		log.error("No address was found for mac: {}", mac);

		return null;
	}

}
