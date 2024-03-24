package ch.bemar.dhcp.processor;

import static org.dhcp4java.DHCPConstants.BOOTREPLY;
import static org.dhcp4java.DHCPConstants.DHCPACK;
import static org.dhcp4java.DHCPConstants.DHCPINFORM;
import static org.dhcp4java.DHCPConstants.DHCPNAK;
import static org.dhcp4java.DHCPConstants.DHCPREQUEST;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_LEASE_TIME;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_MESSAGE;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_SERVER_IDENTIFIER;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPPacket;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.lease.IAddress;
import ch.bemar.dhcp.config.lease.LeaseAddressManagement;
import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.exception.DHCPBadPacketException;
import ch.bemar.dhcp.exception.NoAddressFoundException;
import ch.bemar.dhcp.util.DhcpOptionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DhcpRequestProcessor extends AProcessor {

	private final DhcpSubnetConfig subnetConfig;

	private final LeaseAddressManagement addressManagement;

	public DhcpRequestProcessor(DhcpSubnetConfig subnetConfig, LeaseAddressManagement addressManagement)
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

			IAddress offered = addressManagement.getAddress(request.getHardwareAddress(), hostname, requestedAddress);

			if (offered == null) {
				throw new NoAddressFoundException("There was no free ip address found to offer");
			}

			if (offered != null) {

				log.info("got ip {} for mac {}", offered.getAddress().getHostAddress(),
						request.getHardwareAddress().getHardwareAddressHex());

				return makeDHCPAck(request, offered);

			} else {

				return makeDHCPNak(request, "No address found. Try again later");
			}

		} catch (

		Exception ex) {
			log.error(ex.getMessage());
		}

		return null;
	}

	/**
	 * Create a populated DHCPACK response.
	 * 
	 * <p>
	 * Reponse is populated according to the DHCP request received (must be
	 * DHCPREQUEST), the proposed client address and a set of pre-set options.
	 * 
	 * <p>
	 * Note: <tt>getDefaultSocketAddress</tt> is called internally to populate
	 * address and port number to which response should be sent.
	 * 
	 * @param request
	 * @param offeredAddress
	 * @param options
	 * @return the newly created ACK Packet
	 * @throws UnknownHostException
	 */
	public final DHCPPacket makeDHCPAck(DHCPPacket request, IAddress offeredAddress) throws UnknownHostException {
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
		if ((requestMessageType != DHCPREQUEST) && (requestMessageType != DHCPINFORM)) {
			throw new DHCPBadPacketException("request is not DHCPREQUEST/DHCPINFORM");
		}
		// check offered address
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
		response.setCiaddrRaw(request.getCiaddrRaw());
		if (requestMessageType != DHCPINFORM) {
			response.setYiaddr(offeredAddress.getAddress());
		}
		// Siaddr ?
		response.setGiaddrRaw(request.getGiaddrRaw());
		response.setChaddr(request.getChaddr());
		// sname left empty
		// file left empty

		// we set the DHCPOFFER type
		response.setDHCPMessageType(DHCPACK);

		// set standard options
		if (requestMessageType == DHCPREQUEST) { // rfc 2131
			response.setOptionAsInt(DHO_DHCP_LEASE_TIME, offeredAddress.getLeaseTime());
		}

		response.setOptions(subnetConfig.getOptions());

		handleStandardOptions(request, response, offeredAddress);

		return response;
	}

	/**
	 * Create a populated DHCPNAK response.
	 * 
	 * <p>
	 * Reponse is populated according to the DHCP request received (must be
	 * DHCPREQUEST), the proposed client address and a set of pre-set options.
	 * 
	 * <p>
	 * Note: <tt>getDefaultSocketAddress</tt> is called internally to populate
	 * address and port number to which response should be sent.
	 * 
	 * @param request
	 * @param serverIdentifier
	 * @param message
	 * @return the newly created NAK Packet
	 * @throws UnknownHostException
	 */
	public final DHCPPacket makeDHCPNak(DHCPPacket request, String message) throws UnknownHostException {
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
		if (requestMessageType != DHCPREQUEST) {
			throw new DHCPBadPacketException("request is not DHCPREQUEST");
		}

		DHCPPacket response = new DHCPPacket();

		response.setOp(BOOTREPLY);
		response.setHtype(request.getHtype());
		response.setHlen(request.getHlen());
		// Hops is left to 0
		response.setXid(request.getXid());
		// Secs is left to 0
		response.setFlags(request.getFlags());
		// ciaddr left to 0
		// yiaddr left to 0
		// Siaddr ?
		response.setGiaddrRaw(request.getGiaddrRaw());
		response.setChaddr(request.getChaddr());
		// sname left empty
		// file left empty

		// we set the DHCPOFFER type
		response.setDHCPMessageType(DHCPNAK);

		// set standard options
		response.setOption(DhcpOptionUtils.findOption(subnetConfig.getOptions(), DHO_DHCP_SERVER_IDENTIFIER));
		response.setOptionAsString(DHO_DHCP_MESSAGE, message); // if null, it is removed

		// we do not set other options for this type of message

		// we set address/port according to rfc
		response.setAddrPort(new InetSocketAddress(getSubnetConfig().getBroadcastAddress().getHostAddress(),
				DhcpConstants.RESPONSE_PORT));

		return response;
	}

	@Override
	public byte[] processTypes() {

		return new byte[] { DHCPConstants.DHCPREQUEST };
	}

	@Override
	protected DhcpSubnetConfig getSubnetConfig() {
		return this.subnetConfig;
	}

}
