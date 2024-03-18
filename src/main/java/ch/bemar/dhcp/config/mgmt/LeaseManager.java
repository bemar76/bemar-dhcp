package ch.bemar.dhcp.config.mgmt;

import java.io.IOException;
import java.util.Date;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.net.arp.Arp;
import ch.bemar.dhcp.net.arp.ArpTableProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaseManager {

	private ArpTableProvider arpTableProvider;

	public LeaseManager() throws IOException {
		this.arpTableProvider = new ArpTableProvider();
	}

	public IAddress handleReservedLeasing(Address address, HardwareAddress mac) {

		log.debug("address in conflict: {}", address.isConflict());
		if (address.isConflict()) {
			return null;
		}

		log.debug("leased until: {}", new Date(address.getLeasedUntil()));
		if (address.getLeasedUntil() < System.currentTimeMillis()) { // lease time expired

			log.info("lease expired");

			if (getOkFromArp(address, mac)) { // arp table check was OK

				return setMacAndGetIp(address, mac);

			}

		} else { // lease still active

			if (address.getLeasedTo().equals(mac)) { // yes. its leased to me

				if (getOkFromArp(address, mac)) { // arp table check was OK

					return setMacAndGetIp(address, mac);

				}

			} else { // lease still active but its not by me

				log.error("The lease for address {} is still active but its not leased to my mac {} but {}",
						address.getIp(), mac, address.getLeasedTo());

				address.setConflict(true);
			}

		}

		return null;

	}

	public IAddress handleNextFreeLeasing(Address address, HardwareAddress mac) {

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

				return setMacAndGetIp(address, mac);

			}

		}

		return null;
	}

	public boolean getOkFromArp(Address address, HardwareAddress mac) {
		if (!isAddressActive(address)) { // address was found in arp table

			return true;

		} else if (isAddressActiveByMe(address, mac)) { // address is active be me

			log.info("address {} was found in arp table but its only me", address.getIp());
			return true;
		}

		return false;
	}

	public boolean isAddressActiveByMe(Address address, HardwareAddress mac) {

		Arp arp = arpTableProvider.foundInArp(address.getIp());
		log.debug("arp entry found: {}", arp);

		if (arp != null && !arp.getMac().equals(mac)) {
			return true;
		}

		return false;

	}

	public boolean isAddressActive(Address address) {

		Arp arp = arpTableProvider.foundInArp(address.getIp());
		log.debug("arp entry found: {}", arp);

		return arp != null;

	}

	private IAddress setMacAndGetIp(Address address, HardwareAddress mac) {
		address.setLeasedTo(mac);
		return address;
	}

}
