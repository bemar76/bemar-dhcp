package ch.bemar.dhcp.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.config.mgmt.Address;

@TestMethodOrder(OrderAnnotation.class)
public class AddressPersistenceTest {

	private static LeaseDbService addressService;
	private static long lastContact;

	@BeforeAll
	static void init() {
		addressService = new LeaseDbService("hibernate.cfg2.xml");
	}

	@AfterAll
	static void destroy() {
		addressService.close();
	}

	@Test
	@Order(1)
	void testAddressWrite() throws UnknownHostException {

		lastContact = System.currentTimeMillis();

		Address a = new Address();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.0")));
		a.setHostname("bemar-pc");
		a.setLastContact(lastContact);

		addressService.saveOrUpdate(a);
	}

	@Test
	@Order(2)
	void testAddressReadAll1() throws UnknownHostException {

		Collection<Address> found = addressService.readAll();

		assertEquals(1, found.size());

	}

	@Test
	@Order(3)
	void testAddressRead1() throws UnknownHostException {

		Address a = new Address();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.0")));
		a.setHostname("bemar-pc");
		a.setLastContact(lastContact);

		Address found = (Address) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		assertNotNull(found);

		assertEquals(a.getIp(), found.getIp(), "IP-Adressen stimmen nicht überein");
		assertEquals(a.getHostname(), found.getHostname(), "Hostnamen stimmen nicht überein");
		assertEquals(a.getLeasedTo(), found.getLeasedTo(), "Zugewiesene Hardware-Adressen stimmen nicht überein");
		assertEquals(a.getLastContact(), found.getLastContact(), "Letzter Kontaktzeitpunkt stimmt nicht überein");

	}

	@Test
	@Order(4)
	void testAddressUpdate() throws UnknownHostException {

		lastContact = System.currentTimeMillis();

		Address a = new Address();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.128")));
		a.setHostname("bemar-pc2");
		a.setLastContact(lastContact);

		addressService.saveOrUpdate(a);
	}

	@Test
	@Order(5)
	void testAddressRead2() throws UnknownHostException {

		Address a = new Address();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.128")));
		a.setHostname("bemar-pc2");
		a.setLastContact(lastContact);

		Address found = (Address) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		assertNotNull(found);

		assertEquals(a.getIp(), found.getIp(), "IP-Adressen stimmen nicht überein");
		assertEquals(a.getHostname(), found.getHostname(), "Hostnamen stimmen nicht überein");
		assertEquals(a.getLeasedTo(), found.getLeasedTo(), "Zugewiesene Hardware-Adressen stimmen nicht überein");
		assertEquals(a.getLastContact(), found.getLastContact(), "Letzter Kontakt stimmt nicht überein");

	}

	@Test
	@Order(6)
	void testAddressDelete() throws UnknownHostException {

		Address found = (Address) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		addressService.delete(found);
	}

	@Test
	@Order(7)
	void testAddressReadAll2() throws UnknownHostException {

		Collection<Address> found = addressService.readAll();

		assertEquals(0, found.size());

	}

}
