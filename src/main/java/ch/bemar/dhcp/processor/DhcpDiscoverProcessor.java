package ch.bemar.dhcp.processor;

import static org.dhcp4java.DHCPConstants.BOOTREPLY;
import static org.dhcp4java.DHCPConstants.DHCPDISCOVER;
import static org.dhcp4java.DHCPConstants.DHCPOFFER;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPPacket;
import org.dhcp4java.DHCPResponseUtil;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.mgmt.AddressManagement;
import ch.bemar.dhcp.config.mgmt.IAddress;
import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.core.TransportSocket;
import ch.bemar.dhcp.exception.DHCPBadPacketException;
import ch.bemar.dhcp.exception.NoAddressFoundException;
import ch.bemar.dhcp.util.DhcpOptionUtils;
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

		resp.setOptions(subnetConfig.getOptions());

		handleStandardOptions(request, resp, offeredAddress);

		return resp;
	}

	private void handleStandardOptions(DHCPPacket request, DHCPPacket response, IAddress offeredAddress)
			throws UnknownHostException {

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_DHCP_LEASE_TIME))
			response.setOptionAsInt(DHCPConstants.DHO_DHCP_LEASE_TIME, offeredAddress.getLeaseTime());

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_SUBNET_MASK))
			response.setOption(DhcpOptionUtils.findOptionOrDefault(subnetConfig.getOptions(),
					DHCPConstants.DHO_SUBNET_MASK, subnetConfig.getNetmask().getValue()));

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_INTERFACE_MTU))
			response.setOption(DhcpOptionUtils.findOptionOrDefault(subnetConfig.getOptions(),
					DHCPConstants.DHO_INTERFACE_MTU, Short.parseShort(TransportSocket.PACKET_SIZE + "")));

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_BROADCAST_ADDRESS))
			response.setOption(DhcpOptionUtils.findOptionOrDefault(subnetConfig.getOptions(),
					DHCPConstants.DHO_BROADCAST_ADDRESS, subnetConfig.getBroadcastAddress()));

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_DHCP_RENEWAL_TIME)) {

			int t1 = new Double(offeredAddress.getLeaseTime() * DhcpConstants.RENEWAL_FACTOR).intValue();

			response.setOption(DhcpOptionUtils.findOptionOrDefault(subnetConfig.getOptions(),
					DHCPConstants.DHO_DHCP_RENEWAL_TIME, t1));

		}

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_DHCP_REBINDING_TIME)) {

			int t2 = new Double(offeredAddress.getLeaseTime() * DhcpConstants.REBIND_FACTOR).intValue();

			response.setOption(DhcpOptionUtils.findOptionOrDefault(subnetConfig.getOptions(),
					DHCPConstants.DHO_DHCP_REBINDING_TIME, t2));

		}

		// we set address/port according to rfc
		response.setAddrPort(DHCPResponseUtil.getDefaultSocketAddress(request, DHCPOFFER));
	}

	@Override
	public byte processType() {

		return DHCPConstants.DHCPDISCOVER;

	}

}
