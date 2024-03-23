package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.dhcp4java.HardwareAddress;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.config.element.DefaultLeaseTime;
import ch.bemar.dhcp.config.element.MaxLeaseTime;
import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.config.mgmt.IAddress;
import ch.bemar.dhcp.config.mgmt.LeaseAddress;
import ch.bemar.dhcp.config.mgmt.LeaseManager;

class LeaseManagerTest {

	private static LeaseManager leaseManager;

	private static ArpTable arpTable;

	private static ArpTableProvider provider;

	private static ArpEntry entry1;
	private static ArpEntry entry2;
	private static ArpEntry entry3;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		arpTable = new ArpTable();

		Interface iface = new Interface(InetAddress.getByName("192.168.64.0"), "eth6");

		entry1 = new ArpEntry(InetAddress.getByName("192.168.64.13"), HardwareAddress.getByMac("04:b4:fe:c2:81:be"),
				ArpType.FIXED);
		entry2 = new ArpEntry(InetAddress.getByName("192.168.64.14"), HardwareAddress.getByMac("05:b5:fe:c2:82:be"),
				ArpType.INVALID);
		entry3 = new ArpEntry(InetAddress.getByName("192.168.64.15"), HardwareAddress.getByMac("06:b6:fe:c2:83:be"),
				ArpType.DYNAMIC);

		arpTable.addEntry(iface, entry1);
		arpTable.addEntry(iface, entry2);
		arpTable.addEntry(iface, entry3);

		provider = new ArpTableProvider(arpTable);

		leaseManager = new LeaseManager(provider);

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	// should result null because the requested address is marked as fixed
	void testFixedArp() throws UnknownHostException {

		LeaseAddress la = new LeaseAddress(InetAddress.getByName("192.168.64.13"),
				new Subnet(InetAddress.getByName("255.255.255.0")), new DefaultLeaseTime(1000), new MaxLeaseTime(2000));

		IAddress result = leaseManager.handleNextFreeLeasing(la, "bemar-pc",
				HardwareAddress.getByMac("14:c4:fd:c3:84:ba"));

		Assertions.assertNull(result);
	}

	@Test
	// should result null because the requested address is marked as fixed
	void testFixedArpButHasReservation() throws UnknownHostException {

		LeaseAddress la = new LeaseAddress(InetAddress.getByName("192.168.64.13"),
				new Subnet(InetAddress.getByName("255.255.255.0")), new DefaultLeaseTime(1000), new MaxLeaseTime(2000));

		IAddress result = leaseManager.handleReservedLeasing(la, "bemar-pc",
				HardwareAddress.getByMac("04:b4:fe:c2:81:be"));

		Assertions.assertNull(result);

	}

	@Test
	// should result null because the requested address is marked as fixed even
	// the mac is matching
	void testFixedArpMatchingMac() throws UnknownHostException {

		LeaseAddress la = new LeaseAddress(InetAddress.getByName("192.168.64.13"),
				new Subnet(InetAddress.getByName("255.255.255.0")), new DefaultLeaseTime(1000), new MaxLeaseTime(2000));

		IAddress result = leaseManager.handleNextFreeLeasing(la, "bemar-pc",
				HardwareAddress.getByMac("04:b4:fe:c2:81:be"));

		Assertions.assertNull(result);
	}

	@Test
	// should result the address because address is marked invalid
	void testInvalid() throws UnknownHostException {

		LeaseAddress la = new LeaseAddress(InetAddress.getByName("192.168.64.14"),
				new Subnet(InetAddress.getByName("255.255.255.0")), new DefaultLeaseTime(1000), new MaxLeaseTime(2000));

		IAddress result = leaseManager.handleNextFreeLeasing(la, "bemar-pc",
				HardwareAddress.getByMac("04:b4:fe:c2:81:be"));

		Assertions.assertNotNull(result);

		Assertions.assertEquals(InetAddress.getByName("192.168.64.14"), result.getAddress());
	}

	@Test
	// should result null because address is dynamic but mac is not matching
	void testDynamicNotMatchingMac() throws UnknownHostException {

		LeaseAddress la = new LeaseAddress(InetAddress.getByName("192.168.64.15"),
				new Subnet(InetAddress.getByName("255.255.255.0")), new DefaultLeaseTime(1000), new MaxLeaseTime(2000));

		IAddress result = leaseManager.handleNextFreeLeasing(la, "bemar-pc",
				HardwareAddress.getByMac("04:b4:fe:c2:81:be"));

		Assertions.assertNull(result);

//		Assertions.assertEquals(InetAddress.getByName("192.168.64.14"), result.getAddress());
	}

	@Test
	// should result the address because address is dynamic but mac is matching
	void testDynamicMatchingMac() throws UnknownHostException {

		LeaseAddress la = new LeaseAddress(InetAddress.getByName("192.168.64.15"),
				new Subnet(InetAddress.getByName("255.255.255.0")), new DefaultLeaseTime(1000), new MaxLeaseTime(2000));

		IAddress result = leaseManager.handleNextFreeLeasing(la, "bemar-pc",
				HardwareAddress.getByMac("06:b6:fe:c2:83:be"));

		Assertions.assertNotNull(result);

		Assertions.assertEquals(InetAddress.getByName("192.168.64.15"), result.getAddress());
	}

	@Test
	// should result the address because no arp record present
	void testNoArpRecord() throws UnknownHostException {

		LeaseAddress la = new LeaseAddress(InetAddress.getByName("192.168.64.16"),
				new Subnet(InetAddress.getByName("255.255.255.0")), new DefaultLeaseTime(1000), new MaxLeaseTime(2000));

		IAddress result = leaseManager.handleNextFreeLeasing(la, "bemar-pc",
				HardwareAddress.getByMac("06:b6:fe:c2:83:be"));

		Assertions.assertNotNull(result);

		Assertions.assertEquals(InetAddress.getByName("192.168.64.16"), result.getAddress());
	}

}
