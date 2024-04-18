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

import ch.bemar.dhcp.persistence.cfg.XmlLoader;
import ch.bemar.dhcp.persistence.dao.LeaseDbDao;
import ch.bemar.dhcp.persistence.model.DbLease;

@TestMethodOrder(OrderAnnotation.class)
public class LeaseDaoTest {

	private static LeaseDbDao leaseDao;
	private static long lastContact;

	@BeforeAll
	static void init() throws Exception {

		leaseDao = new LeaseDbDao(
				XmlLoader.loadConfiguration(LeaseDaoTest.class.getResourceAsStream("/persistence.cfg1.xml")));
	}

	@AfterAll
	static void destroy() {
		leaseDao.close();
	}

	@Test
	@Order(1)
	void testAddressSave() throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		lastContact = System.currentTimeMillis();

		DbLease a = new DbLease();
		a.setIp("192.169.64.54");
		a.setHostname("bemar-pc");
		a.setLastContact(lastContact);

		leaseDao.save(a);
	}

	@Test
	@Order(2)
	void testAddressReadAll1()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		Collection<DbLease> found = leaseDao.readAll();

		assertEquals(1, found.size());

	}

	@Test
	@Order(3)
	void testAddressRead1()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		DbLease a = new DbLease();
		a.setIp("192.169.64.54");
		a.setHostname("bemar-pc");
		a.setLastContact(lastContact);

		DbLease found = (DbLease) leaseDao.findByAddress(InetAddress.getByName("192.169.64.54"));

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

		DbLease a = new DbLease();
		a.setIp("192.169.64.54");
		a.setHostname("bemar-pc2");
		a.setLastContact(lastContact);

		leaseDao.update(a);
	}

	@Test
	@Order(5)
	void testAddressRead2()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		DbLease a = new DbLease();
		a.setIp("192.169.64.54");
		a.setHostname("bemar-pc2");
		a.setLastContact(lastContact);

		DbLease found = (DbLease) leaseDao.findByAddress(InetAddress.getByName("192.169.64.54"));

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

		DbLease found = (DbLease) leaseDao.findByAddress(InetAddress.getByName("192.169.64.54"));

		leaseDao.delete(found);
	}

	@Test
	@Order(7)
	void testAddressReadAll2()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		Collection<DbLease> found = leaseDao.readAll();

		assertEquals(0, found.size());

	}

}
