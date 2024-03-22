package ch.bemar.dhcp.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

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
		a.setLastContact(System.currentTimeMillis());
		a.setDefaultLeaseTime(32500 * 1000);
		a.setMaxLeaseTime(86500 * 1000);

		Address found = (Address) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		assertNotNull(found);

		assertEquals(a.getIp(), found.getIp(), "IP-Adressen stimmen nicht überein");
		assertEquals(a.getSubnet(), found.getSubnet(), "Subnetze stimmen nicht überein");
		assertEquals(a.getDefaultLeaseTime(), found.getDefaultLeaseTime(),
				"Standard-Lease-Zeiten stimmen nicht überein");
		assertEquals(a.getMaxLeaseTime(), found.getMaxLeaseTime(), "Maximale Lease-Zeiten stimmen nicht überein");
		assertEquals(a.getHostname(), found.getHostname(), "Hostnamen stimmen nicht überein");
		assertEquals(a.getReservedFor(), found.getReservedFor(), "Reservierte Hardware-Adressen stimmen nicht überein");
		assertEquals(a.getLeasedTo(), found.getLeasedTo(), "Zugewiesene Hardware-Adressen stimmen nicht überein");
		// assertEquals(a.getLastContact(), found.getLastContact(), "Letzter
		// Kontaktzeitpunkt stimmt nicht überein");
		assertEquals(a.isConflict(), found.isConflict(), "Konfliktstatus stimmt nicht überein");
		assertEquals(a.isArp(), found.isArp(), "ARP-Status stimmt nicht überein");
	}

	@Test
	@Order(4)
	void testAddressUpdate() throws UnknownHostException {

		Address a = new Address();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.128")));
		a.setHostname("bemar-pc2");
		a.setLastContact(0);
		a.setDefaultLeaseTime(30500 * 1000);
		a.setMaxLeaseTime(76500 * 1000);

		addressService.saveOrUpdate(a);
	}

	@Test
	@Order(5)
	void testAddressRead2() throws UnknownHostException {

		Address a = new Address();
		a.setIp(InetAddress.getByName("192.169.64.54"));
		a.setSubnet(new Subnet(InetAddress.getByName("255.255.255.128")));
		a.setHostname("bemar-pc2");
		a.setLastContact(0);
		a.setDefaultLeaseTime(30500 * 1000);
		a.setMaxLeaseTime(76500 * 1000);

		Address found = (Address) addressService.findByAddress(InetAddress.getByName("192.169.64.54"));

		assertNotNull(found);

		assertEquals(a.getIp(), found.getIp(), "IP-Adressen stimmen nicht überein");
		assertEquals(a.getSubnet(), found.getSubnet(), "Subnetze stimmen nicht überein");
		assertEquals(a.getDefaultLeaseTime(), found.getDefaultLeaseTime(),
				"Standard-Lease-Zeiten stimmen nicht überein");
		assertEquals(a.getMaxLeaseTime(), found.getMaxLeaseTime(), "Maximale Lease-Zeiten stimmen nicht überein");
		assertEquals(a.getHostname(), found.getHostname(), "Hostnamen stimmen nicht überein");
		assertEquals(a.getReservedFor(), found.getReservedFor(), "Reservierte Hardware-Adressen stimmen nicht überein");
		assertEquals(a.getLeasedTo(), found.getLeasedTo(), "Zugewiesene Hardware-Adressen stimmen nicht überein");
		// assertEquals(a.getLastContact(), found.getLastContact(), "Letzter
		// Kontaktzeitpunkt stimmt nicht überein");
		assertEquals(a.isConflict(), found.isConflict(), "Konfliktstatus stimmt nicht überein");
		assertEquals(a.isArp(), found.isArp(), "ARP-Status stimmt nicht überein");
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
