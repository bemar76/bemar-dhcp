package ch.bemar.org.dhcp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DHCPUtils {

	public static String decodeDHCPRequest(DatagramPacket packet) {
		byte[] requestData = packet.getData();
		int length = packet.getLength();

		// Das DHCP-Request-Paket beginnt normalerweise mit einem bestimmten Header, den
		// wir analysieren müssen.
		// Anschließend folgen verschiedene Optionen, die wir ebenfalls auswerten
		// müssen.

		StringBuilder sb = new StringBuilder();

		// Extrahiere Informationen aus dem DHCP-Header
		// byte opCode = requestData[0]; // Operation Code (Boot Request)
		// byte hwType = requestData[1]; // Hardware Type
		// byte hwAddrLength = requestData[2]; // Hardware Address Length
		// byte hops = requestData[3]; // Hops
		// int transactionId = (requestData[4] << 24) + (requestData[5] << 16) +
		// (requestData[6] << 8) + requestData[7]; // Transaction ID

		// Füge die extrahierten Informationen zum Klartext-String hinzu
		sb.append("Operation Code: ").append(getopCode(requestData)).append("\n");
		sb.append("Hardware Type: ").append(getHwType(requestData)).append("\n");
		sb.append("Hardware Address Length: ").append(getHwAddrLength(requestData)).append("\n");
		sb.append("Hops: ").append(getHops(requestData)).append("\n");
		sb.append("Transaction ID (num): ").append(getTransactionIdInInt(requestData)).append("\n");
		sb.append("Transaction ID (hex): ").append(getTransactionIdInString(requestData)).append("\n");
		sb.append("Client MAC Adresse: ").append(getMacAddress(requestData)).append("\n");

		// Es gibt weitere DHCP-Optionen, die extrahiert und interpretiert werden
		// können.
		// Hier fügen wir nur einige Beispiele hinzu, aber je nach Ihren Anforderungen
		// müssen Sie möglicherweise mehr Optionen analysieren.

		// Option 53: DHCP Message Type
		byte dhcpMessageType = 0;
		for (int i = 240; i < length; i++) {
			if (requestData[i] == 53) {
				dhcpMessageType = requestData[i + 2];
				break;
			}
		}
		sb.append("DHCP Message Type: ").append(dhcpMessageType).append("\n");

		// Option 50: Requested IP Address
		InetAddress requestedIpAddress = null;
		for (int i = 240; i < length; i++) {
			if (requestData[i] == 50) {
				byte[] ipBytes = { requestData[i + 2], requestData[i + 3], requestData[i + 4], requestData[i + 5] };
				try {
					requestedIpAddress = InetAddress.getByAddress(ipBytes);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		if (requestedIpAddress != null) {
			sb.append("Requested IP Address: ").append(requestedIpAddress.getHostAddress()).append("\n");
		}

		// Weitere DHCP-Optionen können auf ähnliche Weise analysiert und zum
		// Klartext-String hinzugefügt werden.

		return sb.toString();
	}

	public static String getopCode(byte[] requestData) {
		return Byte.toString(requestData[0]);
	}

	public static String getHwType(byte[] requestData) {
		return Byte.toString(requestData[1]);
	}

	public static String getHwAddrLength(byte[] requestData) {
		return Byte.toString(requestData[2]);
	}

	public static String getHops(byte[] requestData) {
		return Byte.toString(requestData[3]);
	}

	public static int getTransactionIdInInt(byte[] requestData) {

		byte[] transId = new byte[4];
		transId[0] = requestData[4];
		transId[1] = requestData[5];
		transId[2] = requestData[6];
		transId[3] = requestData[7];

		return ByteBuffer.wrap(transId).getInt();

	}

	public static String getTransactionIdInString(byte[] requestData) {

		byte[] transId = new byte[4];
		transId[0] = requestData[4];
		transId[1] = requestData[5];
		transId[2] = requestData[6];
		transId[3] = requestData[7];

		StringBuffer sb = new StringBuffer();
		for (byte b : transId) {
			sb.append(String.format("%02X ", b));
		}

		return sb.toString();

	}

	public static String getMacAddress(byte[] requestData) {
		byte[] macAddress = new byte[6];
		System.arraycopy(requestData, 28, macAddress, 0, 6); // 28 ist der Offset des MAC-Adressfelds im DHCP-Header
		String macAddressString = String.format("%02X-%02X-%02X-%02X-%02X-%02X", macAddress[0], macAddress[1], macAddress[2], macAddress[3], macAddress[4],
				macAddress[5]);
		// System.out.println("MAC-Adresse des Clients: " + macAddressString);
		return macAddressString;
	}

}
