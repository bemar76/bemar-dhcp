package ch.bemar.dhcp.persistence;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import lombok.extern.slf4j.Slf4j;

@TestMethodOrder(OrderAnnotation.class)
@Slf4j
public class H2ConnectorTest {
	private static SessionFactory sessionFactory;
	private static Session session;

	@BeforeAll
	static void init() {
		sessionFactory = new Configuration().configure("hibernate.cfg3.xml").buildSessionFactory();
		session = sessionFactory.openSession();
	}

	@AfterAll
	static void shutdown() {
		session.close();
		sessionFactory.close();
	}

	@Order(1)
	@Test
	void testWrite() {

		Transaction transaction = session.beginTransaction();

		DbLease address = new DbLease();
		address.setHostname("bemar-pc");
		address.setIp("192.168.64.5");

		session.save(address);

		log.info("Address wrote: {}", address);

		transaction.commit();

	}

	@Order(2)
	@Test
	void testReadAll() {

		String sql = "Select * from " + DbLease.class.getSimpleName();

		NativeQuery<DbLease> query = session.createNativeQuery(sql, DbLease.class);

		List<DbLease> found = query.list();

		log.info("Address found: {}", found);

		Assertions.assertNotNull(found);
		Assertions.assertEquals(1, found.size());

		DbLease item = found.get(0);

		Assertions.assertEquals("bemar-pc", item.getHostname());
		Assertions.assertEquals("192.168.64.5", item.getIp());

	}

	@Order(3)
	@Test
	void testRead() {

		DbLease found = session.find(DbLease.class, "192.168.64.5");

		log.info("Address found: {}", found);

		Assertions.assertNotNull(found);

		Assertions.assertEquals("bemar-pc", found.getHostname());
		Assertions.assertEquals("192.168.64.5", found.getIp());

	}

	@Order(4)
	@Test
	void testWriteSameIpTwice() {

		Transaction transaction = session.beginTransaction();

		DbLease address = new DbLease();
		address.setHostname("bemar-pc");
		address.setIp("192.168.64.5");

		Exception exception = assertThrows(org.hibernate.NonUniqueObjectException.class, () -> {
			session.save(address);
		});

		transaction.commit();

	}

	@Order(5)
	@Test
	void delete() {

		Transaction transaction = session.beginTransaction();

		DbLease found = session.find(DbLease.class, "192.168.64.5");

		session.delete(found);

		log.info("Address deleted: {}", found);

		transaction.commit();

	}

	@Order(6)
	@Test
	void testReadAllAfterDelete() {

		String sql = "Select * from " + DbLease.class.getSimpleName();

		NativeQuery<DbLease> query = session.createNativeQuery(sql, DbLease.class);

		List<DbLease> found = query.list();

		log.info("Address found: {}", found);

		Assertions.assertNotNull(found);
		Assertions.assertEquals(0, found.size());

	}
}