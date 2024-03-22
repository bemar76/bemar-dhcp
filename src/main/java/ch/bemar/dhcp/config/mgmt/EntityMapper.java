package ch.bemar.dhcp.config.mgmt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

import org.dhcp4java.HardwareAddress;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import ch.bemar.dhcp.persistence.DbLease;

public class EntityMapper {

	private EntityMapper() {
	}

	public static DbLease convert(Address address) {

		DbLease db = null;

		if (address != null) {

			db = new DbLease();
			db.setHostname(address.getHostname());
			db.setLastContact(address.getLastContact());

			if (address.getIp() != null)
				db.setIp(address.getIp().getHostAddress());

			if (address.getLeasedTo() != null)
				db.setLeasedTo(address.getLeasedTo().getAsMac());
		}

		return db;
	}

	public static Collection<DbLease> convert2Db(Collection<Address> list) {

		List<DbLease> dbs = Lists.newArrayList();

		for (Address a : list) {

			dbs.add(convert(a));
		}

		return dbs;

	}

	public static Address convert(DbLease dbAddress) throws UnknownHostException {

		Address address = null;

		if (dbAddress != null) {
			address = new Address();
			address.setHostname(dbAddress.getHostname());
			address.setLastContact(dbAddress.getLastContact());

			if (!Strings.isNullOrEmpty(dbAddress.getIp()))
				address.setIp(InetAddress.getByName(dbAddress.getIp()));

			if (!Strings.isNullOrEmpty(dbAddress.getLeasedTo()))
				address.setLeasedTo(HardwareAddress.getByMac(dbAddress.getLeasedTo()));
		}

		return address;

	}

	public static Collection<Address> convert2Address(Collection<DbLease> list) throws UnknownHostException {

		List<Address> addresses = Lists.newArrayList();

		for (DbLease a : list) {

			addresses.add(convert(a));
		}

		return addresses;

	}

}
