package ch.bemar.dhcp.persistence.dao;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Collection;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.persistence.ILeaseDao;
import ch.bemar.dhcp.persistence.cfg.XmlLoader;
import ch.bemar.dhcp.persistence.model.DbLease;
import ch.bemar.dhcp.persistence.model.DbLeaseFactory;

public class LeaseDbDao extends DbDao<DbLease> implements ILeaseDao<DbLease, HardwareAddress, InetAddress> {

	public LeaseDbDao() throws Exception {
		super(XmlLoader.loadConfiguration(LeaseDbDao.class.getResourceAsStream("/persistence.cfg.xml")),
				new DbLeaseFactory());

	}

	public LeaseDbDao(File file) throws Exception {
		super(XmlLoader.loadConfiguration(file), new DbLeaseFactory());

	}

	public LeaseDbDao(InputStream is) throws Exception {
		super(XmlLoader.loadConfiguration(is), new DbLeaseFactory());

	}
	
	

	@Override
	public Collection<DbLease> readAll()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {
		return super.findByExample(new DbLease());
	}

	@Override
	public DbLease findByAddress(InetAddress address)
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		DbLease find = new DbLease();
		find.setIp(address.getHostAddress());

		Collection<DbLease> found = super.findByExample(find);
		if (found.isEmpty()) {
			return null;
		} else if (found.size() > 1) {
			throw new IllegalStateException("Found mor than one entity for unique field " + address);
		}
		return found.iterator().next();
	}

	@Override
	public Collection<DbLease> findByLeasedMac(HardwareAddress hw)
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		DbLease find = new DbLease();
		find.setLeasedTo(hw.getAsMac());

		return super.findByExample(find);

	}

}
