package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;

import org.dhcp4java.HardwareAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Arp {
	
	private InetAddress ip;
	private HardwareAddress mac;
	private boolean dynamic;
	@Override
	public String toString() {
		return "Arp [ip=" + ip + ", mac=" + mac + ", dynamic=" + dynamic + "]";
	}
	
	

}
