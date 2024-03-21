package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

import org.dhcp4java.HardwareAddress;

import com.google.common.collect.Lists;

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
		db.setReservedFor(address.getReservedFor() != null ? address.getReservedFor().getAsMac() : null);
		db.setSubnet(address.getSubnet() != null ? address.getSubnet().getHostAddress() : null);

		return db;
	}

	public static Collection<DbAddress> convert2Db(Collection<Address> list) {

		List<DbAddress> dbs = Lists.newArrayList();

		for (Address a : list) {

			dbs.add(convert(a));
		}

		return dbs;

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

		if (dbAddress.getReservedFor() != null)
			address.setReservedFor(
					new HardwareAddress(HardwareAddress.getHardwareAddressByString(dbAddress.getReservedFor())));
		else
			address.setReservedFor(null);

		if (dbAddress.getSubnet() != null)
			address.setSubnet(new Subnet(InetAddress.getByName(dbAddress.getSubnet())));
		else
			address.setSubnet(null);

		return address;

	}

	public static Collection<Address> convert2Address(Collection<DbAddress> list) throws UnknownHostException {

		List<Address> addresses = Lists.newArrayList();

		for (DbAddress a : list) {

			addresses.add(convert(a));
		}

		return addresses;

	}

}
