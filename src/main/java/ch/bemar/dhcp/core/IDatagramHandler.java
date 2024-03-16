package ch.bemar.dhcp.core;

import ch.bemar.dhcp.config.DhcpSubnetConfig;

public interface IDatagramHandler {

	public DatagramPacket handlePacket(DatagramPacket packet);

	DhcpSubnetConfig getSubnetConfig();
	
	public void close();
}
