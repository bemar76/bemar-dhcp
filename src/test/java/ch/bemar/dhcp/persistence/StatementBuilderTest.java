package ch.bemar.dhcp.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.persistence.model.DbLease;

public class StatementBuilderTest {

	@Test
	void testUpdateStatement() throws IllegalArgumentException, IllegalAccessException {

		DbLease address = new DbLease();
		address.setHostname("bemar-pc");
		address.setIp("192.168.64.5");
		address.setLastContact(1711726310307l);
		address.setLeasedTo("DD:DG:2D:G3:2D:EF");

		String statement = new StatementBuilder<DbLease>().update(address);

		Assertions.assertEquals(
				"UPDATE DbLease set hostname = 'bemar-pc', leasedTo = 'DD:DG:2D:G3:2D:EF', lastContact = 1711726310307 WHERE ip = '192.168.64.5'",
				statement);

	}

	@Test
	void testInsertStatement() throws IllegalArgumentException, IllegalAccessException {

		DbLease address = new DbLease();
		address.setHostname("bemar-pc");
		address.setIp("192.168.64.5");
		address.setLastContact(1711726310307l);
		address.setLeasedTo("DD:DG:2D:G3:2D:EF");

		String statement = new StatementBuilder<DbLease>().insert(address);

		Assertions.assertEquals(
				"INSERT INTO DbLease( ip, hostname, leasedTo, lastContact)VALUES ( '192.168.64.5', 'bemar-pc', 'DD:DG:2D:G3:2D:EF', 1711726310307)",
				statement);

	}

	@Test
	void testDeleteStatement() throws IllegalArgumentException, IllegalAccessException {

		DbLease address = new DbLease();
		address.setHostname("bemar-pc");
		address.setIp("192.168.64.5");
		address.setLastContact(1711726310307l);
		address.setLeasedTo("DD:DG:2D:G3:2D:EF");

		String statement = new StatementBuilder<DbLease>().delete(address);

		Assertions.assertEquals("DELETE FROM DbLease WHERE ip = '192.168.64.5'", statement);

	}

	@Test
	void testSelect1() throws IllegalArgumentException, IllegalAccessException {

		DbLease address = new DbLease();
		address.setHostname("bemar-pc");
		address.setIp("192.168.64.5");
		address.setLastContact(1711726310307l);
		address.setLeasedTo("DD:DG:2D:G3:2D:EF");

		String statement = new StatementBuilder<DbLease>().select(address, true);

		Assertions.assertEquals("SELECT * FROM DbLease WHERE ip = '192.168.64.5' AND hostname = 'bemar-pc' AND leasedTo = 'DD:DG:2D:G3:2D:EF' AND lastContact = 1711726310307", statement);

	}
	
	@Test
	void testSelect2() throws IllegalArgumentException, IllegalAccessException {

		DbLease address = new DbLease();
		address.setHostname("bemar-pc");		
		address.setLeasedTo("DD:DG:2D:G3:2D:EF");

		String statement = new StatementBuilder<DbLease>().select(address, true);

		Assertions.assertEquals("SELECT * FROM DbLease WHERE ip IS null AND hostname = 'bemar-pc' AND leasedTo = 'DD:DG:2D:G3:2D:EF' AND lastContact IS null", statement);

	}
	
	@Test
	void testSelect3() throws IllegalArgumentException, IllegalAccessException {

		DbLease address = new DbLease();
		address.setHostname("bemar-pc");		
		address.setLeasedTo("DD:DG:2D:G3:2D:EF");

		String statement = new StatementBuilder<DbLease>().select(address, false);

		Assertions.assertEquals("SELECT * FROM DbLease WHERE hostname = 'bemar-pc' AND leasedTo = 'DD:DG:2D:G3:2D:EF'", statement);

	}
	
	@Test
	void testSelect4() throws IllegalArgumentException, IllegalAccessException {

		DbLease address = new DbLease();

		String statement = new StatementBuilder<DbLease>().select(address, false);

		Assertions.assertEquals("SELECT * FROM DbLease", statement);

	}

}
