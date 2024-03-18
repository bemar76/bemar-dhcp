package ch.bemar.dhcp.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BroadcastAddressCalculator {

	private BroadcastAddressCalculator() {
	}

	public static InetAddress calculateBroadcastAddress(InetAddress inetAddress, int cidr) throws UnknownHostException {
		// InetAddress Objekt aus der IP-Adresse erstellen
		// InetAddress inetAddress = InetAddress.getByName(ipAddress);

		// IP-Adresse in ein Byte-Array umwandeln
		byte[] addressBytes = inetAddress.getAddress();

		// Berechnung der negierten Subnetzmaske aus CIDR
		int mask = -1 << (32 - cidr);
		byte[] maskBytes = { (byte) (mask >>> 24), (byte) (mask >>> 16 & 0xFF), (byte) (mask >>> 8 & 0xFF),
				(byte) (mask & 0xFF) };

		// Berechnung der Broadcast-Adresse
		byte[] broadcastBytes = new byte[addressBytes.length];
		for (int i = 0; i < addressBytes.length; i++) {
			// Anwenden der negierten Subnetzmaske auf die IP-Adresse
			broadcastBytes[i] = (byte) (addressBytes[i] | (~maskBytes[i] & 0xFF));
		}

		// Broadcast-Adresse als InetAddress zurückgeben
		return InetAddress.getByAddress(broadcastBytes);
	}

	public static InetAddress calculateBroadcastAddress(InetAddress inetAddress, InetAddress netmask)
			throws UnknownHostException {
		return calculateBroadcastAddress(inetAddress, subnetMaskToCIDR(netmask.getHostAddress()));
	}

	public static int subnetMaskToCIDR(String subnetMask) {
		int cidr = 0;
		String[] parts = subnetMask.split("\\.");

		for (String part : parts) {
			int intPart = Integer.parseInt(part);

			while (intPart > 0) {
				cidr += intPart & 1;
				intPart >>= 1;
			}
		}
		log.info("calculated cidr: {}", cidr);
		return cidr;
	}

}
