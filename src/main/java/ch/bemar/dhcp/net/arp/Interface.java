package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Interface implements Comparable<Interface> {

	private InetAddress address;
	private String name;

	@Override
	public String toString() {
		return "Interface [address=" + address + ", name=" + name + "]";
	}

	@Override
	public int compareTo(Interface o) {
		return getAddress().getHostAddress().compareTo(o.getAddress().getHostAddress());
	}

}
