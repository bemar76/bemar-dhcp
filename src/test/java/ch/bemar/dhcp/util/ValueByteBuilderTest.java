package ch.bemar.dhcp.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.convert.ValueByteBuilder;

class ValueByteBuilderTest {

	@Test
	void testMultiInetAddresses() throws UnknownHostException {

		InetAddress[] nameserverIPs = { InetAddress.getByName("192.168.0.1"), InetAddress.getByName("8.8.8.8"),
				InetAddress.getByName("8.8.4.4") };

		try {
			byte[] multiIpsBytes = ValueByteBuilder.multiIpsToByte(nameserverIPs);

			System.out.println("IPs in Hex: " + ValueByteBuilder.byteArrayToHexString(multiIpsBytes));

			Assertions.assertEquals("C0A800010808080808080404",
					ValueByteBuilder.byteArrayToHexString(multiIpsBytes));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
