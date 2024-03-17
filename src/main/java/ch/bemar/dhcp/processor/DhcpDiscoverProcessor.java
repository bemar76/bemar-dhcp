package ch.bemar.dhcp.processor;

import static org.dhcp4java.DHCPConstants.BOOTREPLY;
import static org.dhcp4java.DHCPConstants.DHCPDISCOVER;
import static org.dhcp4java.DHCPConstants.DHCPOFFER;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPPacket;
import org.dhcp4java.DHCPResponseUtil;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.mgmt.AddressManagement;
import ch.bemar.dhcp.config.mgmt.IAddress;
import ch.bemar.dhcp.exception.DHCPBadPacketException;
import ch.bemar.dhcp.exception.NoAddressFoundException;
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

			IAddress offered = addressManagement.getAddress(request.getHardwareAddress());
			if (offered == null) {
				throw new NoAddressFoundException("There was no free ip address found to offer");
			}

			log.info("got ip {} for mac {}", offered.getAddress().getHostAddress(),
					request.getHardwareAddress().getHardwareAddressHex());

			List<DHCPOption> options = new ArrayList<>();

			options.addAll(subnetConfig.getOptions());

			return createOfferPacket(request, offered);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

		return null;
	}

	public DHCPPacket createOfferPacket(DHCPPacket request, IAddress offeredAddress) {
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

		DHCPPacket resp = new DHCPPacket();

		resp.setOp(BOOTREPLY);
		resp.setHtype(request.getHtype());
		resp.setHlen(request.getHlen());
		// Hops is left to 0
		resp.setXid(request.getXid());
		// Secs is left to 0
		resp.setFlags(request.getFlags());
		// Ciaddr is left to 0.0.0.0
		resp.setYiaddr(offeredAddress.getAddress());
		// Siaddr ?
		resp.setGiaddrRaw(request.getGiaddrRaw());
		resp.setChaddr(request.getChaddr());
		// sname left empty
		// file left empty

		// we set the DHCPOFFER type
		resp.setDHCPMessageType(DHCPOFFER);

		// set standard options
		resp.setOptionAsInt(DHCPConstants.DHO_DHCP_LEASE_TIME, offeredAddress.getLeaseTime());
		// resp.setOptionAsInetAddress(DHCPConstants.DHO_DHCP_SERVER_IDENTIFIER,
		// serverIdentifier);
		// resp.setOptionAsString(DHCPConstants.DHO_DHCP_MESSAGE, message); // if null,
		// it is removed

		InetAddress subnet = subnetConfig.getOptionValueAsInetAddress(subnetConfig.getOptions(),
				DHCPConstants.DHO_SUBNET_MASK);
		if (subnet == null) {
			resp.setOptionAsInetAddress(DHCPConstants.DHO_SUBNET_MASK, subnetConfig.getSubnetAddress().getValue());
		}

		// we set address/port according to rfc
		resp.setAddrPort(DHCPResponseUtil.getDefaultSocketAddress(request, DHCPOFFER));

		return resp;
	}

	@Override
	public byte processType() {

		return DHCPConstants.DHCPDISCOVER;
	}

}
