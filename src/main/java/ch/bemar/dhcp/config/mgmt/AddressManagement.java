package ch.bemar.dhcp.config.mgmt;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

import org.dhcp4java.HardwareAddress;

import com.google.common.collect.Lists;

import ch.bemar.dhcp.config.DhcpHostConfig;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.exception.NoAddressFoundException;
import ch.bemar.dhcp.util.IPRangeCalculatorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressManagement {

	private LeaseManager leaseManager;

	private DhcpSubnetConfig subnetConfig;

	private List<Address> addresses;

	public AddressManagement(DhcpSubnetConfig subnet) throws IOException, NoAddressFoundException {

		this.subnetConfig = subnet;
		this.addresses = Lists.newArrayList();

		this.leaseManager = new LeaseManager();

		log.info("Building AddressManagement DB");

		log.info("handling addresses for subnet {}", subnet);

		List<InetAddress> ips = IPRangeCalculatorUtil.calculateAllIPsInRange(subnet.getRange(),
				subnet.getSubnetAddress().getValue());

		if (ips.isEmpty()) {
			throw new NoAddressFoundException("There are no addresses calculated for range " + subnetConfig.getRange());
		}

		for (InetAddress ip : ips) {

			Address a = new Address(ip, subnet.getSubnetAddress(), (subnet.getDefaultLeaseTime() * 1000),
					subnet.getMaxLeaseTime());

			for (DhcpHostConfig host : subnet.getHosts()) {

				if (host.getFixedIpAddress().getValue().equals(ip)) {
					a.setReservedFor(host.getHardwareAddress());
				}

			}

			addresses.add(a);
			log.trace("adding ip {} to db", a);
		}

		Collections.sort(addresses);

		log.info("{} addresses calculated for range {}", addresses.size(), subnetConfig.getRange());

	}

	public synchronized IAddress getAddress(HardwareAddress mac, String hostname) throws Exception {
		return getAddress(mac, hostname, null);
	}

	public synchronized IAddress getAddress(HardwareAddress mac, String hostname, InetAddress requestAddress)
			throws Exception {

		IAddress address = checkReservationsOrLeasesForMac(mac, hostname);

		if (address == null) {
			address = getNextAddress(mac, hostname, requestAddress);
		}

		if (address == null) {
			address = getNextAddress(mac, hostname);
		}

		return address;

	}

	synchronized IAddress checkReservationsOrLeasesForMac(HardwareAddress mac, String hostname) throws Exception {

		for (Address address : addresses) {

			if ((address.getReservedFor() != null && address.getReservedFor().equals(mac))
					|| (mac.equals(address.getLeasedTo()))) {

				log.info("Found reservation or leasing for {}", mac);

				IAddress addr = leaseManager.handleReservedLeasing(address, hostname, mac);

				if (addr != null) {
					return addr;
				}

			}

		}

		log.error("No reservation was found for mac: {}", mac);

		return null;

	}

	synchronized IAddress getNextAddress(HardwareAddress mac, String hostname) throws Exception {

		return getNextAddress(mac, hostname, null);
	}

	synchronized IAddress getNextAddress(HardwareAddress mac, String hostname, InetAddress requestAddress)
			throws Exception {

		for (Address address : addresses) {

			if (requestAddress == null || address.getAddress().equals(requestAddress)) {

				IAddress addr = leaseManager.handleNextFreeLeasing(address, hostname, mac);

				if (addr != null) {
					return addr;
				}

			}

		}

		if (requestAddress != null)
			log.warn("Requested address {} for mac {} is not free", requestAddress, mac);
		else
			log.warn("No free address found for mac {}", mac);

		return null;
	}

}
