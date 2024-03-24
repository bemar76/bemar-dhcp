package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.dhcp4java.HardwareAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.config.lease.EntityMapper;
import ch.bemar.dhcp.config.lease.LeaseAddress;
import ch.bemar.dhcp.persistence.DbLease;

public class EntityManagerTest {

	@Test
	void testMappingDb2Address() throws UnknownHostException {

		DbLease orig = new DbLease();
		orig.setLeasedTo("00:20:cb:d2:27:7b");
		orig.setHostname("bemar-PC");
		orig.setIp("192.168.64.34");
		orig.setLastContact(System.currentTimeMillis());

		LeaseAddress address = EntityMapper.convert(orig);

		DbLease converted = EntityMapper.convert(address);

		Assertions.assertEquals(orig.getHostname(), converted.getHostname());
		Assertions.assertEquals(orig.getIp(), converted.getIp());
		Assertions.assertEquals(orig.getLeasedTo(), converted.getLeasedTo());
		Assertions.assertTrue(converted.getLastContact() > 0);

	}

	@Test
	void testMappingAddress2Db() throws UnknownHostException {

		LeaseAddress orig = new LeaseAddress();
		orig.setHostname("bemar-PC");
		orig.setIp(InetAddress.getByName("192.168.64.34"));
		orig.setLeasedTo(HardwareAddress.getByMac("00:20:cb:d2:27:7b"));
		orig.setLastContact(System.currentTimeMillis());

		DbLease address = EntityMapper.convert(orig);

		LeaseAddress converted = EntityMapper.convert(address);

		Assertions.assertEquals(orig.getHostname(), converted.getHostname());
		Assertions.assertEquals(orig.getIp(), converted.getIp());
		Assertions.assertEquals(orig.getLeasedTo(), converted.getLeasedTo());
		Assertions.assertTrue(converted.getLastContact() > 0);

	}

}
