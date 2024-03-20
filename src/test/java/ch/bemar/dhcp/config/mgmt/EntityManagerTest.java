package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.dhcp4java.HardwareAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.persistence.DbAddress;

public class EntityManagerTest {

	@Test
	void testMappingDb2Address() throws UnknownHostException {

		DbAddress orig = new DbAddress();
		orig.setArp(false);
		orig.setConflict(false);
		orig.setDefaultLeaseTime(32768 * 1000);
		orig.setHostname("bemar-PC");
		orig.setIp("192.168.64.34");
		orig.setLastContact(System.currentTimeMillis() - (32768 * 1000));
		orig.setLeasedUntil(System.currentTimeMillis());
		orig.setMaxLeaseTime((32768 * 1000));
		orig.setReservedFor("00:20:cb:d2:27:7b");
		orig.setSubnet("255.255.255.0");

		Address address = EntityMapper.convert(orig);

		DbAddress converted = EntityMapper.convert(address);

		Assertions.assertEquals(orig, converted);

	}

	@Test
	void testMappingAddress2Db() throws UnknownHostException {

		Address orig = new Address();
		orig.setArp(false);
		orig.setConflict(false);
		orig.setDefaultLeaseTime(32768 * 1000);
		orig.setHostname("bemar-PC");
		orig.setIp(InetAddress.getByName("192.168.64.34"));
		orig.setLastContact(System.currentTimeMillis() - (32768 * 1000));
		orig.setMaxLeaseTime((32768 * 1000));
		orig.setReservedFor(new HardwareAddress(HardwareAddress.getHardwareAddressByString("00:20:cb:d2:27:7b")));
		orig.setSubnet(new Subnet(InetAddress.getByName("255.255.255.0")));

		DbAddress address = EntityMapper.convert(orig);

		Address converted = EntityMapper.convert(address);

		Assertions.assertEquals(orig, converted);

	}

}
