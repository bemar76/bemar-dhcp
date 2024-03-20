package ch.bemar.dhcp.config.mgmt;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import org.dhcp4java.HardwareAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ch.bemar.dhcp.config.DhcpHostConfig;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.element.FixedAddress;
import ch.bemar.dhcp.config.element.IpRange;
import ch.bemar.dhcp.config.element.Netmask;
import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.exception.NoAddressFoundException;

@TestMethodOrder(OrderAnnotation.class)
public class AddressManagementTest {

	private static AddressManagement addressMgmt;

	private static InetAddress address;

	private static String mac = "00:20:9A:1B:AE:C8";
	
	private static String requestMac = "01:21:9A:1B:AE:C8";

	private static String reservedMac = "04:20:9E:1A:AE:C9";

	private static String reservedIp = "172.16.8.17";

	@BeforeAll
	static void init() throws IOException, NoAddressFoundException {

		DhcpSubnetConfig subnet = new DhcpSubnetConfig();
		subnet.setNetmask(new Netmask(InetAddress.getByName("255.255.255.0")));
		subnet.setRange(new IpRange(InetAddress.getByName("172.16.8.10"), InetAddress.getByName("172.16.8.20")));
		subnet.setSubnetAddress(new Subnet(InetAddress.getByName("172.16.8.0")));

		DhcpHostConfig host = new DhcpHostConfig();
		host.setFixedIpAddress(new FixedAddress(InetAddress.getByName(reservedIp)));
		host.setHardwareAddress(new HardwareAddress("hardware ethernet " + reservedMac));

		subnet.getHosts().add(host);

		addressMgmt = new AddressManagement(subnet);
	}

	@Test
	@Order(1)
	void testAddressLeasing() throws Exception {

		HardwareAddress hwAdress = new HardwareAddress("hardware ethernet " + mac);

		IAddress addr = addressMgmt.getAddress(hwAdress);

		System.out.println(addr);

		address = addr.getAddress();

		System.out.println("Leased bis: " + new Date(addr.getLeasedUntil()));

		System.out.println("Last contact: " + new Date(addr.getLastContact()));

		Assertions.assertEquals(mac, addr.getLeasedTo().getAsMac().toUpperCase());
		Assertions.assertEquals(InetAddress.getByName("172.16.8.10"), addr.getAddress());

	}

	@Test
	@Order(2)
	void testAddressReLease() throws Exception {

		HardwareAddress hwAdress = new HardwareAddress("hardware ethernet " + mac);

		IAddress addr = addressMgmt.getAddress(hwAdress);

		System.out.println(addr);

		System.out.println("Leased bis: " + new Date(addr.getLeasedUntil()));

		System.out.println("Last contact: " + new Date(addr.getLastContact()));

		Assertions.assertEquals(mac, addr.getLeasedTo().getAsMac().toUpperCase());
		Assertions.assertEquals(address, addr.getAddress());
		Assertions.assertEquals(InetAddress.getByName("172.16.8.10"), addr.getAddress());
	}

	@Test
	@Order(3)
	void testAddressReservation() throws Exception {

		HardwareAddress hwAdress = new HardwareAddress("hardware ethernet " + reservedMac);

		IAddress addr = addressMgmt.getAddress(hwAdress);

		System.out.println(addr);

		System.out.println("Leased bis: " + new Date(addr.getLeasedUntil()));

		System.out.println("Last contact: " + new Date(addr.getLastContact()));

		Assertions.assertEquals(reservedMac, addr.getLeasedTo().getAsMac().toUpperCase());
		Assertions.assertEquals(InetAddress.getByName(reservedIp), addr.getAddress());

	}
	
	@Test
	@Order(4)
	void testAddressRequestSpecificWithReservation() throws Exception {

		HardwareAddress hwAdress = new HardwareAddress("hardware ethernet " + reservedMac);
		
		InetAddress requestedAddress = InetAddress.getByName("172.16.8.12");

		IAddress addr = addressMgmt.getAddress(hwAdress, requestedAddress);

		System.out.println(addr);

		System.out.println("Leased bis: " + new Date(addr.getLeasedUntil()));

		System.out.println("Last contact: " + new Date(addr.getLastContact()));

		Assertions.assertEquals(reservedMac, addr.getLeasedTo().getAsMac().toUpperCase()); 
		Assertions.assertEquals(InetAddress.getByName(reservedIp), addr.getAddress()); // reservation tops request


	}
	
	@Test
	@Order(5)
	void testAddressRequestSpecific() throws Exception {

		HardwareAddress hwAdress = new HardwareAddress("hardware ethernet " + requestMac);
		
		InetAddress requestedAddress = InetAddress.getByName("172.16.8.12");

		IAddress addr = addressMgmt.getAddress(hwAdress, requestedAddress);

		System.out.println(addr);

		System.out.println("Leased bis: " + new Date(addr.getLeasedUntil()));

		System.out.println("Last contact: " + new Date(addr.getLastContact()));

		Assertions.assertEquals(requestMac, addr.getLeasedTo().getAsMac().toUpperCase());
		Assertions.assertEquals(requestedAddress, addr.getAddress());

	}

}
