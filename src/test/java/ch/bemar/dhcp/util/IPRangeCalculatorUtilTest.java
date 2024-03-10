package ch.bemar.dhcp.util;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IPRangeCalculatorUtilTest {

	/**
	 * Test with CIDR 22. 
	 * 
	 * @throws UnknownHostException
	 */
	@Test
	void calculateTest_Cidr22() throws UnknownHostException {
		String startIP = "192.168.0.10"; // Start-IP-Adresse
		String endIP = "192.168.2.64"; // End-IP-Adresse
		String subnetMask = "255.255.252.0"; // Subnetzmaske

		System.out.println("Broadcast Adress: " + IPRangeCalculatorUtil.getBroadcastAddress(startIP, subnetMask));
		Assertions.assertEquals("/192.168.3.255", IPRangeCalculatorUtil.getBroadcastAddress(startIP, subnetMask).toString());
		try {
			List<String> allIPsInRange = IPRangeCalculatorUtil.calculateAllIPsInRange(startIP, endIP, subnetMask);
			System.out.println("All IPs within the range:");
			for (String ip : allIPsInRange) {
				System.out.println(ip);
			}

			Assertions.assertEquals(567, allIPsInRange.size());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test with CIDR 22. 
	 * 
	 * @throws UnknownHostException
	 */
	@Test
	void calculateTest_Cidr24() throws UnknownHostException {
		String startIP = "192.168.0.10"; // Start-IP-Adresse
		String endIP = "192.168.0.220"; // End-IP-Adresse
		String subnetMask = "255.255.255.0"; // Subnetzmaske

		System.out.println("Broadcast Adress: " + IPRangeCalculatorUtil.getBroadcastAddress(startIP, subnetMask));
		Assertions.assertEquals("/192.168.0.255", IPRangeCalculatorUtil.getBroadcastAddress(startIP, subnetMask).toString());
		try {
			List<String> allIPsInRange = IPRangeCalculatorUtil.calculateAllIPsInRange(startIP, endIP, subnetMask);
			System.out.println("All IPs within the range:");
			for (String ip : allIPsInRange) {
				System.out.println(ip);
			}

			Assertions.assertEquals(211, allIPsInRange.size());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test with CIDR 17. 
	 * 
	 * @throws UnknownHostException
	 */
	@Test
	void calculateTest_Cidr17() throws UnknownHostException {
		String startIP = "192.168.0.10"; // Start-IP-Adresse
		String endIP = "192.168.123.220"; // End-IP-Adresse
		String subnetMask = "255.255.128.0"; // Subnetzmaske

		System.out.println("Broadcast Adress: " + IPRangeCalculatorUtil.getBroadcastAddress(startIP, subnetMask));
		Assertions.assertEquals("/192.168.127.255", IPRangeCalculatorUtil.getBroadcastAddress(startIP, subnetMask).toString());
		try {
			List<String> allIPsInRange = IPRangeCalculatorUtil.calculateAllIPsInRange(startIP, endIP, subnetMask);
			System.out.println("All IPs within the range:");
			for (String ip : allIPsInRange) {
				System.out.println(ip);
			}

			Assertions.assertEquals(31699, allIPsInRange.size());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test with CIDR 17. 
	 * 
	 * @throws UnknownHostException
	 */
	@Test
	void calculateTest_Cidr17All() throws UnknownHostException {
		String startIP = "192.168.0.1"; // Start-IP-Adresse
		String endIP = "192.168.127.254"; // End-IP-Adresse
		String subnetMask = "255.255.128.0"; // Subnetzmaske

		System.out.println("Broadcast Adress: " + IPRangeCalculatorUtil.getBroadcastAddress(startIP, subnetMask));
		Assertions.assertEquals("/192.168.127.255", IPRangeCalculatorUtil.getBroadcastAddress(startIP, subnetMask).toString());
		try {
			List<String> allIPsInRange = IPRangeCalculatorUtil.calculateAllIPsInRange(startIP, endIP, subnetMask);
			System.out.println("All IPs within the range:");
			for (String ip : allIPsInRange) {
				System.out.println(ip);
			}

			Assertions.assertEquals(32766, allIPsInRange.size());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
