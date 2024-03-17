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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class DhcpRequestProcessor implements IProcessor {

	private final DhcpSubnetConfig subnetConfig;

	private final AddressManagement addressManagement;

	public DhcpRequestProcessor(DhcpSubnetConfig subnetConfig, AddressManagement addressManagement) throws IOException {
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

			if (offered != null) {

				log.info("got ip {} for mac {}", offered.getAddress().getHostAddress(),
						request.getHardwareAddress().getHardwareAddressHex());

				List<DHCPOption> options = new ArrayList<>();

				options.addAll(subnetConfig.getOptions());

				return makeDHCPAck(request, offered, options.toArray(new DHCPOption[0]));

			} else {

				Optional<DHCPOption> serverIdentifier = subnetConfig.getOptions().stream()
						.filter(o -> o.getCode() == DHCPConstants.DHO_DHCP_SERVER_IDENTIFIER).findFirst();
				if (serverIdentifier.isEmpty()) {
					throw new IllegalStateException("No server Identifier found");
				}

				return makeDHCPNak(request, "No address found. Try again later",
						serverIdentifier.get().getValueAsInetAddr());
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
	 */
	public static final DHCPPacket makeDHCPAck(DHCPPacket request, IAddress offeredAddress, DHCPOption[] options) {
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

		DHCPPacket resp = new DHCPPacket();

		resp.setOp(BOOTREPLY);
		resp.setHtype(request.getHtype());
		resp.setHlen(request.getHlen());
		// Hops is left to 0
		resp.setXid(request.getXid());
		// Secs is left to 0
		resp.setFlags(request.getFlags());
		resp.setCiaddrRaw(request.getCiaddrRaw());
		if (requestMessageType != DHCPINFORM) {
			resp.setYiaddr(offeredAddress.getAddress());
		}
		// Siaddr ?
		resp.setGiaddrRaw(request.getGiaddrRaw());
		resp.setChaddr(request.getChaddr());
		// sname left empty
		// file left empty

		// we set the DHCPOFFER type
		resp.setDHCPMessageType(DHCPACK);

		// set standard options
		if (requestMessageType == DHCPREQUEST) { // rfc 2131
			resp.setOptionAsInt(DHO_DHCP_LEASE_TIME, offeredAddress.getLeaseTime());
		}
		// resp.setOptionAsInetAddress(DHO_DHCP_SERVER_IDENTIFIER, serverIdentifier);
		// resp.setOptionAsString(DHO_DHCP_MESSAGE, message); // if null, it is removed

		if (options != null) {
			for (DHCPOption opt : options) {
				resp.setOption(opt.applyOption(request));
			}
		}

		// we set address/port according to rfc
		resp.setAddrPort(DHCPResponseUtil.getDefaultSocketAddress(request, DHCPACK));

		return resp;
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
	 */
	public static final DHCPPacket makeDHCPNak(DHCPPacket request, String message, InetAddress serverIdentifier) {
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

		DHCPPacket resp = new DHCPPacket();

		resp.setOp(BOOTREPLY);
		resp.setHtype(request.getHtype());
		resp.setHlen(request.getHlen());
		// Hops is left to 0
		resp.setXid(request.getXid());
		// Secs is left to 0
		resp.setFlags(request.getFlags());
		// ciaddr left to 0
		// yiaddr left to 0
		// Siaddr ?
		resp.setGiaddrRaw(request.getGiaddrRaw());
		resp.setChaddr(request.getChaddr());
		// sname left empty
		// file left empty

		// we set the DHCPOFFER type
		resp.setDHCPMessageType(DHCPNAK);

		// set standard options
		resp.setOptionAsInetAddress(DHO_DHCP_SERVER_IDENTIFIER, serverIdentifier);
		resp.setOptionAsString(DHO_DHCP_MESSAGE, message); // if null, it is removed

		// we do not set other options for this type of message

		// we set address/port according to rfc
		resp.setAddrPort(DHCPResponseUtil.getDefaultSocketAddress(request, DHCPNAK));

		return resp;
	}

	@Override
	public byte processType() {

		return DHCPConstants.DHCPREQUEST;
	}

}
