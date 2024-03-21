package ch.bemar.dhcp.persistence;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.config.mgmt.Address;

@TestMethodOrder(OrderAnnotation.class)
public class AddressPersistenceTest {

	private static AddressService addressService;

	@BeforeAll
	static void init() {
		addressService = new AddressService();
	}

	@Test
	@Order(1)
	void testAddressWrite() throws UnknownHostException {

		Address a = new Address();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.0")));
		a.setHostname("bemar-pc");
		a.setLastContact(0);
		a.setDefaultLeaseTime(32500 * 1000);
		a.setMaxLeaseTime(86500 * 1000);

		addressService.update(a);
	}

	@Test
	@Order(2)
	void testAddressRead() throws UnknownHostException {

		Address a = new Address();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.0")));
		a.setHostname("bemar-pc");
		a.setLastContact(System.currentTimeMillis());
		a.setDefaultLeaseTime(32500 * 1000);
		a.setMaxLeaseTime(86500 * 1000);

		Address found = (Address) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		Assertions.assertEquals(a, found);
	}

}
