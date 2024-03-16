package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Interface {
	
	private InetAddress address;
	private String name;

}
