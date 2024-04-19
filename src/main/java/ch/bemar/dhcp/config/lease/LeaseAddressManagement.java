package ch.bemar.dhcp.config.lease;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.persistence.cfg.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaseAddressManagement {

	private LeaseManager leaseManager;

	private LeaseTable leaseTable;

	public LeaseAddressManagement(DhcpSubnetConfig subnet, Configuration dbCfg) throws Exception {
		this.leaseTable = new LeaseTable(subnet, dbCfg);
		this.leaseManager = new LeaseManager();

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

		return persist(cleanup(address, mac));

	}

	synchronized IAddress checkReservationsOrLeasesForMac(HardwareAddress mac, String hostname) throws Exception {

		for (LeaseAddress address : leaseTable.getLeaseAddresses()) {

			if ((address.getReservedFor() != null && address.getReservedFor().equals(mac))
					|| (mac.equals(address.getLeasedTo()))) {

				log.info("Found reservation or leasing for {}", mac);

				IAddress addr = leaseManager.handleReservedLeasing(address, hostname, mac);

				if (addr != null) {
					return persist(cleanup(addr, mac));
				}

			}

		}

		log.error("No reservation was found for mac: {}", mac);

		return null;

	}

	synchronized IAddress getNextAddress(HardwareAddress mac, String hostname) throws Exception {

		return persist(cleanup(getNextAddress(mac, hostname, null), mac));
	}

	synchronized IAddress getNextAddress(HardwareAddress mac, String hostname, InetAddress requestAddress)
			throws Exception {

		for (LeaseAddress address : leaseTable.getLeaseAddresses()) {

			if (requestAddress == null || address.getAddress().equals(requestAddress)) {

				IAddress addr = leaseManager.handleNextFreeLeasing(address, hostname, mac);

				if (addr != null) {
					return persist(cleanup(address, mac));
				}

			}

		}

		if (requestAddress != null)
			log.warn("Requested address {} for mac {} is not free", requestAddress, mac);
		else
			log.warn("No free address found for mac {}", mac);

		return null;
	}

	private IAddress persist(IAddress a)
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {
		if (a != null)
			leaseTable.persist((LeaseAddress) a);

		return a;
	}

	/**
	 * checks whether the mac is already defined with other records
	 * 
	 * @param a
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 */
	private IAddress cleanup(IAddress a, HardwareAddress mac)
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		for (LeaseAddress address : leaseTable.getLeaseAddresses()) {

			if (!address.getIp().equals(a) && address.getLeasedTo() != null && address.getLeasedTo().equals(mac)) {
				log.debug("removing old lease for mac {}", mac.getAsMac());
				address.evictLeasing();
				persist(address);
			}

		}
		return a;
	}

	public void close() {
		leaseTable.close();

	}
}
