package ch.bemar.dhcp.processor;

import java.io.IOException;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPPacket;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.lease.LeaseAddressManagement;
import ch.bemar.dhcp.dns.DnsUpdateManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DhcpInfoProcessor extends AProcessor {

	public DhcpInfoProcessor(DhcpSubnetConfig subnetConfig, LeaseAddressManagement addressManagement,
			DnsUpdateManager updateManager) throws IOException {
		super(updateManager);
	}

	@Override
	public DHCPPacket processPacket(DHCPPacket request) {
		log.info(request.toString());

		return null;
	}

	@Override
	public byte[] processTypes() {

		return new byte[] { DHCPConstants.DHCPACK, DHCPConstants.DHCPNAK, DHCPConstants.DHCPOFFER };
	}

	@Override
	protected DhcpSubnetConfig getSubnetConfig() {
		return null;
	}

}
