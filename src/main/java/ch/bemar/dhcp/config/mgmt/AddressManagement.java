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
import ch.bemar.dhcp.persistence.LeaseDbService;
import ch.bemar.dhcp.util.IPRangeCalculatorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressManagement {

	private LeaseManager leaseManager;

	private DhcpSubnetConfig subnetConfig;

	private List<Address> addresses;

	private final LeaseDbService addressService;

	public AddressManagement(DhcpSubnetConfig subnet) throws IOException, NoAddressFoundException {

		this.addressService = new LeaseDbService();

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

			Address foundFromDb = addressService.findByAddress(ip);

			if (foundFromDb == null) {

				Address created = new Address(ip, subnet.getSubnetAddress(), subnet.getDefaultLeaseTime(),
						subnet.getMaxLeaseTime());

				for (DhcpHostConfig host : subnet.getHosts()) {

					if (host.getFixedIpAddress().getValue().equals(ip)) {
						created.setReservedFor(host.getHardwareAddress());
					}

				}
				log.debug("adding created address to map: {}", created);
				addresses.add(created);

				log.debug("saving created address to db: {}", created);
				addressService.saveOrUpdate(created);

			} else {

				log.debug("adding db address: {}", foundFromDb);
				addresses.add(foundFromDb);
			}

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

		return persist(address);

	}

	synchronized IAddress checkReservationsOrLeasesForMac(HardwareAddress mac, String hostname) throws Exception {

		for (Address address : addresses) {

			if ((address.getReservedFor() != null && address.getReservedFor().equals(mac))
					|| (mac.equals(address.getLeasedTo()))) {

				log.info("Found reservation or leasing for {}", mac);

				IAddress addr = leaseManager.handleReservedLeasing(address, hostname, mac);

				if (addr != null) {
					return persist(addr);
				}

			}

		}

		log.error("No reservation was found for mac: {}", mac);

		return null;

	}

	synchronized IAddress getNextAddress(HardwareAddress mac, String hostname) throws Exception {

		return persist(getNextAddress(mac, hostname, null));
	}

	synchronized IAddress getNextAddress(HardwareAddress mac, String hostname, InetAddress requestAddress)
			throws Exception {

		for (Address address : addresses) {

			if (requestAddress == null || address.getAddress().equals(requestAddress)) {

				IAddress addr = leaseManager.handleNextFreeLeasing(address, hostname, mac);

				if (addr != null) {
					return persist(address);
				}

			}

		}

		if (requestAddress != null)
			log.warn("Requested address {} for mac {} is not free", requestAddress, mac);
		else
			log.warn("No free address found for mac {}", mac);

		return null;
	}

	private IAddress persist(IAddress a) {
		if (a != null)
			addressService.saveOrUpdate((Address) a);

		return a;
	}
}
