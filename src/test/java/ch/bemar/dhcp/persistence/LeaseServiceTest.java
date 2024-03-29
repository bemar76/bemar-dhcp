package ch.bemar.dhcp.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Collection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.config.lease.LeaseAddress;
import ch.bemar.dhcp.persistence.service.LeaseDbService;

@TestMethodOrder(OrderAnnotation.class)
public class LeaseServiceTest {

	private static LeaseDbService addressService;
	private static long lastContact;

	@BeforeAll
	static void init() throws Exception {
		addressService = new LeaseDbService(LeaseServiceTest.class.getResourceAsStream("/persistence.cfg2.xml"));
	}

	@AfterAll
	static void destroy() {
		addressService.close();
	}

	@Test
	@Order(1)
	void testAddressWrite()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		lastContact = System.currentTimeMillis();

		LeaseAddress a = new LeaseAddress();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.0")));
		a.setHostname("bemar-pc");
		a.setLastContact(lastContact);

		addressService.saveOrUpdate(a);
	}

	@Test
	@Order(2)
	void testAddressReadAll1()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		Collection<LeaseAddress> found = addressService.readAll();

		assertEquals(1, found.size());

	}

	@Test
	@Order(3)
	void testAddressRead1()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		LeaseAddress a = new LeaseAddress();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.0")));
		a.setHostname("bemar-pc");
		a.setLastContact(lastContact);

		LeaseAddress found = (LeaseAddress) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		assertNotNull(found);

		assertEquals(a.getIp(), found.getIp(), "IP-Adressen stimmen nicht überein");
		assertEquals(a.getHostname(), found.getHostname(), "Hostnamen stimmen nicht überein");
		assertEquals(a.getLeasedTo(), found.getLeasedTo(), "Zugewiesene Hardware-Adressen stimmen nicht überein");
		assertEquals(a.getLastContact(), found.getLastContact(), "Letzter Kontaktzeitpunkt stimmt nicht überein");

	}

	@Test
	@Order(4)
	void testAddressUpdate()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		lastContact = System.currentTimeMillis();

		LeaseAddress a = new LeaseAddress();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.128")));
		a.setHostname("bemar-pc2");
		a.setLastContact(lastContact);

		addressService.saveOrUpdate(a);
	}

	@Test
	@Order(5)
	void testAddressRead2()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		LeaseAddress a = new LeaseAddress();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.128")));
		a.setHostname("bemar-pc2");
		a.setLastContact(lastContact);

		LeaseAddress found = (LeaseAddress) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		assertNotNull(found);

		assertEquals(a.getIp(), found.getIp(), "IP-Adressen stimmen nicht überein");
		assertEquals(a.getHostname(), found.getHostname(), "Hostnamen stimmen nicht überein");
		assertEquals(a.getLeasedTo(), found.getLeasedTo(), "Zugewiesene Hardware-Adressen stimmen nicht überein");
		assertEquals(a.getLastContact(), found.getLastContact(), "Letzter Kontakt stimmt nicht überein");

	}

	@Test
	@Order(6)
	void testAddressDelete()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		LeaseAddress found = (LeaseAddress) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		addressService.delete(found);
	}

	@Test
	@Order(7)
	void testAddressReadAll2()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		Collection<LeaseAddress> found = addressService.readAll();

		assertEquals(0, found.size());

	}

}
