package ch.bemar.dhcp.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import ch.bemar.dhcp.config.element.IpRange;

public class IPRangeCalculatorUtil {
	public static void main(String[] args) {

	}

	public static List<InetAddress> calculateAllIPsInRange(IpRange range, InetAddress subnetMask)
			throws UnknownHostException {
		List<InetAddress> allIPsInRange = new ArrayList<>();

		byte[] startBytes = range.getStart().getAddress();
		byte[] endBytes = range.getEnd().getAddress();
		byte[] maskBytes = subnetMask.getAddress();

		// Berechnung der Netzwerkadresse und der Broadcast-Adresse
		byte[] networkAddress = new byte[4];
		byte[] broadcastAddress = new byte[4];
		for (int i = 0; i < 4; i++) {
			networkAddress[i] = (byte) (startBytes[i] & maskBytes[i]);
			broadcastAddress[i] = (byte) (networkAddress[i] | ~maskBytes[i]);
		}

		// Iteration über alle IP-Adressen im Bereich
		byte[] currentAddress = networkAddress.clone();
		while (!isGreaterThan(currentAddress, broadcastAddress) && !isEqual(currentAddress, endBytes)) {
			InetAddress currentIP = InetAddress.getByAddress(currentAddress);
			if (isGreaterThan(currentAddress, startBytes)) {
				allIPsInRange.add(currentIP);
			}
			incrementAddress(currentAddress);

		}

		// Add the end IP address if it's within the range
		if (isEqual(currentAddress, endBytes)) {
			allIPsInRange.add(range.getEnd());
		}

		return allIPsInRange;
	}

	public static InetAddress getBroadcastAddress(InetAddress startAddr, InetAddress maskAddr)
			throws UnknownHostException {

		byte[] startBytes = startAddr.getAddress();
		byte[] maskBytes = maskAddr.getAddress();

		byte[] networkAddress = new byte[4];
		byte[] broadcastAddress = new byte[4];
		for (int i = 0; i < 4; i++) {
			networkAddress[i] = (byte) (startBytes[i] & maskBytes[i]);
			broadcastAddress[i] = (byte) (networkAddress[i] | ~maskBytes[i]);
		}

		return InetAddress.getByAddress(broadcastAddress);
	}

	private static void incrementAddress(byte[] address) {
		for (int i = address.length - 1; i >= 0; i--) {
			if ((address[i] & 0xFF) == 255) {
				address[i] = 0;
			} else {
				address[i]++;
				break;
			}
		}
	}

	private static boolean isGreaterThan(byte[] address1, byte[] address2) {
		for (int i = 0; i < 4; i++) {
			int a = address1[i] & 0xFF;
			int b = address2[i] & 0xFF;
			if (a > b) {
				return true;
			} else if (a < b) {
				return false;
			}
		}
		return true;
	}

	private static boolean isEqual(byte[] address1, byte[] address2) {
		for (int i = 0; i < 4; i++) {
			if (address1[i] != address2[i]) {
				return false;
			}
		}
		return true;
	}
}
