package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.persistence.DbAddress;

public class EntityMapper {

	private EntityMapper() {
	}

	public static DbAddress convert(Address address) {

		DbAddress db = new DbAddress();
		db.setArp(address.isArp());
		db.setConflict(address.isConflict());
		db.setDefaultLeaseTime(address.getDefaultLeaseTime());
		db.setHostname(address.getHostname());
		db.setIp(address.getIp().getHostAddress());
		db.setLastContact(address.getLastContact());
		db.setLeasedUntil(address.getLeasedUntil());
		db.setMaxLeaseTime(address.getMaxLeaseTime());
		db.setReservedFor(address.getReservedFor().getAsMac());
		db.setSubnet(address.getSubnet().getHostAddress());

		return db;
	}

	public static Address convert(DbAddress dbAddress) throws UnknownHostException {
		
		Address address = new Address();
		address.setArp(dbAddress.isArp());
		address.setConflict(dbAddress.isConflict());
		address.setDefaultLeaseTime(dbAddress.getDefaultLeaseTime());
		address.setHostname(dbAddress.getHostname());
		address.setIp(InetAddress.getByName(dbAddress.getIp()));
		address.setLastContact(dbAddress.getLastContact());
		address.setMaxLeaseTime(dbAddress.getMaxLeaseTime());
		address.setReservedFor(new HardwareAddress( HardwareAddress.getHardwareAddressByString(dbAddress.getReservedFor())));
		address.setSubnet(new Subnet(InetAddress.getByName(dbAddress.getSubnet())));

		return address;

	}

}
