package ch.bemar.dhcp;

import org.dhcp4java.DHCPPacket;

public interface DHCPResponse {
	
	public byte supportsRequest();
	
	public DHCPPacket service(DHCPPacket request);

}
