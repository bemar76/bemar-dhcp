package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;

import org.dhcp4java.HardwareAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArpEntry implements Comparable<ArpEntry> {

	private InetAddress ip;
	private HardwareAddress mac;
	private ArpType type;

	@Override
	public int compareTo(ArpEntry o) {
		return this.ip.getHostAddress().compareTo(o.getIp().getHostAddress());
	}

	@Override
	public String toString() {
		return "ArpEntry [ip=" + ip + ", mac=" + mac + ", type=" + type + "]";
	}

}
