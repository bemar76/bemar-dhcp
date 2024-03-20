package ch.bemar.dhcp.processor;

import static org.dhcp4java.DHCPConstants.BOOTREPLY;
import static org.dhcp4java.DHCPConstants.DHCPDISCOVER;
import static org.dhcp4java.DHCPConstants.DHCPOFFER;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPPacket;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.mgmt.AddressManagement;
import ch.bemar.dhcp.config.mgmt.IAddress;
import ch.bemar.dhcp.exception.DHCPBadPacketException;
import ch.bemar.dhcp.exception.NoAddressFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DhcpDiscoverProcessor extends AProcessor {

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

			InetAddress requestedAddress = getRequestedAddress(request);
			String hostname = getClientHostname(request);

			IAddress offered = addressManagement.getAddress(request.getHardwareAddress(), hostname,
					(requestedAddress != null ? requestedAddress : null));

			if (offered == null) {
				throw new NoAddressFoundException("There was no free ip address found to offer");
			}

			log.info("got ip {} for mac {}", offered.getAddress().getHostAddress(),
					request.getHardwareAddress().getHardwareAddressHex());

			return createOfferPacket(request, offered);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

		return null;
	}

	public DHCPPacket createOfferPacket(DHCPPacket request, IAddress offeredAddress) throws UnknownHostException {
		// check request
		if (request == null) {
			throw new NullPointerException("request is null");
		}
		if (!request.isDhcp()) {
			throw new DHCPBadPacketException("request is BOOTP");
		}
		Byte requestMessageType = request.getDHCPMessageType();
		if (requestMessageType == null) {
			throw new DHCPBadPacketException("request has no message type");
		}
		if (requestMessageType != DHCPDISCOVER) {
			throw new DHCPBadPacketException("request is not DHCPDISCOVER");
		}
		// check offeredAddress
		if (offeredAddress == null) {
			throw new IllegalArgumentException("offeredAddress must not be null");
		}
		if (!(offeredAddress.getAddress() instanceof Inet4Address)) {
			throw new IllegalArgumentException("offeredAddress must be IPv4");
		}

		DHCPPacket response = new DHCPPacket();

		response.setOp(BOOTREPLY);
		response.setHtype(request.getHtype());
		response.setHlen(request.getHlen());
		// Hops is left to 0
		response.setXid(request.getXid());
		// Secs is left to 0
		response.setFlags(request.getFlags());
		// Ciaddr is left to 0.0.0.0
		response.setYiaddr(offeredAddress.getAddress());
		// Siaddr ?
		response.setGiaddrRaw(request.getGiaddrRaw());
		response.setChaddr(request.getChaddr());
		// sname left empty
		// file left empty

		// we set the DHCPOFFER type
		response.setDHCPMessageType(DHCPOFFER);

		response.setOptions(subnetConfig.getOptions());

		handleStandardOptions(request, response, offeredAddress);

		return response;
	}

	@Override
	public byte processType() {

		return DHCPConstants.DHCPDISCOVER;

	}

	@Override
	protected DhcpSubnetConfig getSubnetConfig() {
		return this.subnetConfig;
	}

}
