package ch.bemar.dhcp.processor;

import org.dhcp4java.DHCPPacket;

public interface IProcessor {

	public DHCPPacket processPacket(DHCPPacket request);

	public byte[] processTypes();

}
