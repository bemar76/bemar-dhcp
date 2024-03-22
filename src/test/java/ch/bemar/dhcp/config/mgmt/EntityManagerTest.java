package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.dhcp4java.HardwareAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.persistence.DbLease;

public class EntityManagerTest {

	@Test
	void testMappingDb2Address() throws UnknownHostException {

		DbLease orig = new DbLease();
		orig.setHostname("bemar-PC");
		orig.setIp("192.168.64.34");
		orig.setLastContact(System.currentTimeMillis() - (32768 * 1000));

		Address address = EntityMapper.convert(orig);

		DbLease converted = EntityMapper.convert(address);

		Assertions.assertEquals(orig, converted);

	}

	@Test
	void testMappingAddress2Db() throws UnknownHostException {

		long now = System.currentTimeMillis();

		Address orig = new Address();
		orig.setHostname("bemar-PC");
		orig.setIp(InetAddress.getByName("192.168.64.34"));
		orig.setLastContact(now);
		orig.setLeasedTo(HardwareAddress.getByMac("00:20:cb:d2:27:7b"));

		DbLease address = EntityMapper.convert(orig);

		Address converted = EntityMapper.convert(address);

		Assertions.assertEquals(orig, converted);

	}

}
