package ch.bemar.dhcp.processor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPPacket;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.lease.IAddress;
import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.core.TransportSocket;
import ch.bemar.dhcp.dns.DnsUpdateManager;
import ch.bemar.dhcp.util.DhcpOptionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AProcessor implements IProcessor {

	protected abstract DhcpSubnetConfig getSubnetConfig();

	private final DnsUpdateManager dnsUpdateManager;

	private final ExecutorService executor = Executors.newFixedThreadPool(5);

	AProcessor(DnsUpdateManager dnsUpdateManager) {
		this.dnsUpdateManager = dnsUpdateManager;
	}

	protected void handleStandardOptions(DHCPPacket request, DHCPPacket response, IAddress offeredAddress)
			throws UnknownHostException {

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_DHCP_LEASE_TIME))
			response.setOptionAsInt(DHCPConstants.DHO_DHCP_LEASE_TIME, offeredAddress.getLeaseTime());

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_SUBNET_MASK))
			response.setOption(DhcpOptionUtils.findOptionOrDefault(getSubnetConfig().getOptions(),
					DHCPConstants.DHO_SUBNET_MASK, getSubnetConfig().getNetmask().getValue()));

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_INTERFACE_MTU))
			response.setOption(DhcpOptionUtils.findOptionOrDefault(getSubnetConfig().getOptions(),
					DHCPConstants.DHO_INTERFACE_MTU, Short.parseShort(TransportSocket.PACKET_SIZE + "")));

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_BROADCAST_ADDRESS))
			response.setOption(DhcpOptionUtils.findOptionOrDefault(getSubnetConfig().getOptions(),
					DHCPConstants.DHO_BROADCAST_ADDRESS, getSubnetConfig().getBroadcastAddress()));

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_DHCP_RENEWAL_TIME)) {

			int t1 = new Double(offeredAddress.getLeaseTime() * DhcpConstants.RENEWAL_FACTOR).intValue();

			response.setOption(DhcpOptionUtils.findOptionOrDefault(getSubnetConfig().getOptions(),
					DHCPConstants.DHO_DHCP_RENEWAL_TIME, t1));

		}

		if (!DhcpOptionUtils.hasOption(response.getOptionsCollection(), DHCPConstants.DHO_DHCP_REBINDING_TIME)) {

			int t2 = new Double(offeredAddress.getLeaseTime() * DhcpConstants.REBIND_FACTOR).intValue();

			response.setOption(DhcpOptionUtils.findOptionOrDefault(getSubnetConfig().getOptions(),
					DHCPConstants.DHO_DHCP_REBINDING_TIME, t2));

		}

		// we set address/port according to rfc
		response.setAddrPort(new InetSocketAddress(getSubnetConfig().getBroadcastAddress().getHostAddress(),
				DhcpConstants.RESPONSE_PORT));
	}

	protected InetAddress getRequestedAddress(DHCPPacket request) {

		DHCPOption requestedAddress = DhcpOptionUtils.findOption(request.getOptionsCollection(),
				DHCPConstants.DHO_DHCP_REQUESTED_ADDRESS);

		if (requestedAddress != null) {
			return requestedAddress.getValueAsInetAddr();
		}

		try {
			if (request.getCiaddr() != null
					&& !request.getCiaddr().equals(InetAddress.getByAddress(new byte[] { 0, 0, 0, 0 }))) {
				return request.getCiaddr();
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

		return null;

	}

	protected String getClientHostname(DHCPPacket request) {

		DHCPOption hostName = DhcpOptionUtils.findOption(request.getOptionsCollection(), DHCPConstants.DHO_HOST_NAME);

		if (hostName != null)
			return hostName.getValueAsString();

		return null;

	}

	protected DHCPPacket updateDns(DHCPPacket packet, String name) throws IllegalArgumentException, IOException {
		log.info("sending ddns update");

		log.debug("host name: {}", name);

		if (name != null) {

			executor.submit(new Runnable() {
				public void run() {
					try {
						
						dnsUpdateManager.updateDnsRecord(packet.getYiaddr(), name);
						log.info("update request sent");
						
					} catch (Exception ex) {
						log.error(ex.getMessage(), ex);
					}
				}
			});

		} else {
			log.warn("no dns update sent because no name present");
		}

		return packet;

	}
}
