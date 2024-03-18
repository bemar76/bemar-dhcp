package ch.bemar.dhcp.config.mgmt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

import org.dhcp4java.HardwareAddress;

import com.google.common.collect.Sets;

import ch.bemar.dhcp.config.DhcpHostConfig;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.exception.NoAddressFoundException;
import ch.bemar.dhcp.util.IPRangeCalculatorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressManagement {

	private LeaseManager leaseManager;

	private DhcpSubnetConfig subnetConfig;

	private Set<Address> addresses;

	public AddressManagement(DhcpSubnetConfig subnet) throws IOException, NoAddressFoundException {

		this.subnetConfig = subnet;
		this.addresses = Sets.newHashSet();

		this.leaseManager = new LeaseManager();

		log.info("Building AddressManagement DB");

		log.info("handling addresses for subnet {}", subnet);

		List<InetAddress> ips = IPRangeCalculatorUtil.calculateAllIPsInRange(subnet.getRange(),
				subnet.getSubnetAddress().getValue());

		if (ips.isEmpty()) {
			throw new NoAddressFoundException("There are no addresses calculated for range " + subnetConfig.getRange());
		}

		for (InetAddress ip : ips) {

			Address a = new Address(ip, subnet.getSubnetAddress(), subnet.getDefaultLeaseTime(),
					subnet.getMaxLeaseTime());

			for (DhcpHostConfig host : subnet.getHosts()) {

				if (host.getFixedIpAddress().getValue().equals(ip)) {
					a.setReservedFor(host.getHardwareAddress());
				}

			}

			addresses.add(a);
			log.trace("adding ip {} to db", a);
		}

		log.info("{} addresses calculated for range {}", addresses.size(), subnetConfig.getRange());

	}

	public synchronized IAddress getAddress(HardwareAddress mac) throws Exception {

		IAddress address = checkReservationsOrLeasesForMac(mac);

		if (address == null) {
			address = getNextAddress(mac);
		}

		return address;

	}

	synchronized IAddress checkReservationsOrLeasesForMac(HardwareAddress mac) throws Exception {

		for (Address address : addresses) {

			if ((address.getReservedFor() != null && address.getReservedFor().equals(mac))
					|| (mac.equals(address.getLeasedTo()))) {

				log.info("Found reservation or leasing for {}", mac);

				IAddress addr = leaseManager.handleReservedLeasing(address, mac);

				if (addr != null) {
					return addr;
				}

			}

		}

		log.error("No reservation was found for mac: {}", mac);

		return null;

	}

	synchronized IAddress getNextAddress(HardwareAddress mac) throws Exception {

		for (Address address : addresses) {

			IAddress addr = leaseManager.handleNextFreeLeasing(address, mac);

			if (addr != null) {
				return addr;
			}

		}

		log.error("No free address was found for mac: {}", mac);

		return null;
	}

}
