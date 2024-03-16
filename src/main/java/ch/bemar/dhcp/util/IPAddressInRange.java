package ch.bemar.dhcp.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class IPAddressInRange {

	private IPAddressInRange() {
	}

	public static boolean isIPInRange(InetAddress ip, InetAddress startIP, InetAddress endIP, InetAddress netmask)
			throws Exception {

		byte[] ipBytes = ip.getAddress();
		byte[] startIPBytes = startIP.getAddress();
		byte[] endIPBytes = endIP.getAddress();
		byte[] netmaskBytes = netmask.getAddress();

		// Berechne das Netzwerk-Präfix für Start-IP und IP und prüfe die
		// Übereinstimmung
		byte[] networkBytes = new byte[ipBytes.length];
		for (int i = 0; i < ipBytes.length; i++) {
			networkBytes[i] = (byte) (startIPBytes[i] & netmaskBytes[i]);
		}

		ByteBuffer networkBuffer = ByteBuffer.wrap(networkBytes);
		long network = networkBuffer.getInt();

		ByteBuffer ipBuffer = ByteBuffer.wrap(ipBytes);
		long ipLong = ipBuffer.getInt();

		ByteBuffer startIPBuffer = ByteBuffer.wrap(startIPBytes);
		long startIPLong = startIPBuffer.getInt();

		ByteBuffer endIPBuffer = ByteBuffer.wrap(endIPBytes);
		long endIPLong = endIPBuffer.getInt();

		// Prüfe, ob die IP innerhalb des Start- und Endbereichs liegt
		return (ipLong >= startIPLong && ipLong <= endIPLong) && ((ipLong & network) == (startIPLong & network));
	}

	public static boolean isIpAddressInRange(InetAddress ipToCheck, InetAddress subnetMask) throws UnknownHostException {

		byte[] ipToCheckBytes = ipToCheck.getAddress();
		byte[] networkIpBytes = getNetworkAddress(ipToCheck, subnetMask).getAddress();
		byte[] subnetMaskBytes = subnetMask.getAddress();

		if (ipToCheckBytes.length != networkIpBytes.length || ipToCheckBytes.length != subnetMaskBytes.length) {
			throw new IllegalArgumentException("All addresses must be of the same type");
		}

		for (int i = 0; i < ipToCheckBytes.length; i++) {
			int ipByte = ipToCheckBytes[i] & 0xFF;
			int networkByte = networkIpBytes[i] & 0xFF;
			int maskByte = subnetMaskBytes[i] & 0xFF;

			if ((ipByte & maskByte) != (networkByte & maskByte)) {
				return false;
			}
		}
		return true;
	}

	public static InetAddress getNetworkAddress(InetAddress ip, InetAddress subnetMask) throws UnknownHostException {
		byte[] ipAdressBytes = ip.getAddress();
		byte[] subnetMaskBytes = subnetMask.getAddress();
		byte[] networkAddress = new byte[ipAdressBytes.length];

		for (int i = 0; i < ipAdressBytes.length; i++) {
			// Anwenden der Bitweise-UND-Operation auf jedes Byte
			networkAddress[i] = (byte) (ipAdressBytes[i] & subnetMaskBytes[i]);
		}

		return InetAddress.getByAddress(networkAddress);
	}
}
