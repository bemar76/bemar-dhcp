package ch.bemar.dhcp.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPPacket;
import org.dhcp4java.DHCPResponseFactory;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.mgmt.AddressManagement;
import ch.bemar.dhcp.config.mgmt.IAddress;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DhcpDiscoverProcessor implements IProcessor {

	private final DhcpSubnetConfig subnetConfig;

	private final AddressManagement addressManagement;

	public DhcpDiscoverProcessor(DhcpSubnetConfig subnetConfig, AddressManagement addressManagement)
			throws IOException {
		this.subnetConfig = subnetConfig;
		this.addressManagement = addressManagement;
	}

	@Override
	public DHCPPacket processPacket(DHCPPacket request) {
		log.info(request.toString());

		try {

			IAddress offered = addressManagement.checkReservationsOrLeasesForMac(request.getHardwareAddress());
			if (offered == null) {
				addressManagement.getNextAddress(request.getHardwareAddress(), null);
			}

			log.info("got ip {} for mac {}", offered.getAddress().getHostAddress(),
					request.getHardwareAddress().getHardwareAddressHex());

			List<DHCPOption> options = new ArrayList<>();

			options.addAll(subnetConfig.getOptions());

			return DHCPResponseFactory.makeDHCPOffer(request, offered, options.toArray(new DHCPOption[0]));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

		return null;
	}

	@Override
	public byte processType() {

		return DHCPConstants.DHCPDISCOVER;
	}

}
