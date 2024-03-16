package ch.bemar.dhcp.util;

import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetworkInterfaceInfo {

	private NetworkInterfaceInfo() {
	}

	public static void printIfaceInfo() {
		try {

			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

			for (NetworkInterface ni : Collections.list(interfaces)) {

				log.info("Interface: " + ni.getName() + " (" + ni.getDisplayName() + ")");

				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				for (InetAddress address : Collections.list(addresses)) {
					log.info("  IP-Adresse: " + address.getHostAddress());
				}
				log.info(""); // Fügt eine Leerzeile für eine bessere Lesbarkeit hinzu
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
