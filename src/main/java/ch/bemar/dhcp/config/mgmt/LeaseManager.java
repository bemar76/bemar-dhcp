package ch.bemar.dhcp.config.mgmt;

import java.io.IOException;
import java.util.Date;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.net.arp.ArpEntry;
import ch.bemar.dhcp.net.arp.ArpTableProvider;
import ch.bemar.dhcp.net.arp.ArpType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaseManager {

	private ArpTableProvider arpTableProvider;

	public LeaseManager() throws IOException {
		this.arpTableProvider = new ArpTableProvider();
	}

	public LeaseManager(ArpTableProvider provider) {
		this.arpTableProvider = provider;
	}

	public IAddress handleReservedLeasing(LeaseAddress address, String hostname, HardwareAddress mac) {

		log.debug("address in conflict: {}", address.isConflict());
		if (address.isConflict()) {
			return null;
		}

		log.debug("leased until: {}", new Date(address.getLeasedUntil()));
		if (address.getLeasedUntil() < System.currentTimeMillis()) { // lease time expired

			log.info("lease expired");

			if (getOkFromArp(address, mac)) { // arp table check was OK

				return setMacAndGetIp(address, hostname, mac);

			}

		} else { // lease still active

			if (address.getLeasedTo().equals(mac)) { // yes. its leased to me

				if (getOkFromArp(address, mac)) { // arp table check was OK

					return setMacAndGetIp(address, hostname, mac);

				}

			} else { // lease still active but its not by me

				log.error("The lease for address {} is still active but its not leased to my mac {} but {}",
						address.getIp(), mac, address.getLeasedTo());

				address.setConflict(true);
			}

		}

		return null;

	}

	public IAddress handleNextFreeLeasing(LeaseAddress address, String hostname, HardwareAddress mac) {

		if (address.isConflict()
				|| (address.getReservedFor() != null && address.getLeasedUntil() > System.currentTimeMillis())) {

			log.debug("address in conflict: {}", address.isConflict());
			log.debug("address reserved for: {}", address.getReservedFor());
			return null;
		}

		log.debug("leased until: {}", new Date(address.getLeasedUntil()));
		if (address.getLeasedUntil() < System.currentTimeMillis()) { // lease time expired

			log.info("lease expired");

			if (getOkFromArp(address, mac)) { // arp table check was OK

				return setMacAndGetIp(address, hostname, mac);

			}

		}

		return null;
	}

	public boolean getOkFromArp(LeaseAddress address, HardwareAddress mac) {
		ArpEntry arpEntry = arpTableProvider.searchInArpTable(address.getAddress());

		if (arpEntry != null && !ArpType.INVALID.equals(arpEntry.getType())) {

			if (ArpType.FIXED.equals(arpEntry.getType())
					|| (ArpType.DYNAMIC.equals(arpEntry.getType()) && !arpEntry.getMac().equals(mac))) {

				return false;
			}

		}

		return true;
	}

	private IAddress setMacAndGetIp(LeaseAddress address, String hostname, HardwareAddress mac) {
		address.setLeasedTo(mac);
		address.setHostname(hostname);
		return address;
	}

}
