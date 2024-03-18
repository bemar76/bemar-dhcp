package ch.bemar.dhcp.processor;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPPacket;

import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.mgmt.IAddress;
import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.core.TransportSocket;
import ch.bemar.dhcp.util.DhcpOptionUtils;

public abstract class AProcessor implements IProcessor {

	protected abstract DhcpSubnetConfig getSubnetConfig();

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
		response.setAddrPort(new InetSocketAddress(getSubnetConfig().getBroadcastAddress().getHostAddress(), 68));
	}

}
