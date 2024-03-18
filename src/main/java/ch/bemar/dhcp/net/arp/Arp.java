package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;

import org.dhcp4java.HardwareAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Arp implements Comparable<Arp> {
	
	private InetAddress ip;
	private HardwareAddress mac;
	private boolean dynamic;
	@Override
	public String toString() {
		return "Arp [ip=" + ip + ", mac=" + mac + ", dynamic=" + dynamic + "]";
	}
	@Override
	public int compareTo(Arp o) {
		return this.ip.getHostAddress().compareTo(o.getIp().getHostAddress());
	}
	
	

}
