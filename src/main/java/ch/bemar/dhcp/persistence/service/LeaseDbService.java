package ch.bemar.dhcp.persistence.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Collection;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.lease.EntityMapper;
import ch.bemar.dhcp.config.lease.LeaseAddress;
import ch.bemar.dhcp.persistence.IService;
import ch.bemar.dhcp.persistence.cfg.Configuration;
import ch.bemar.dhcp.persistence.dao.LeaseDbDao;
import ch.bemar.dhcp.persistence.model.DbLease;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeaseDbService implements IService<LeaseAddress, HardwareAddress, InetAddress> {

	private LeaseDbDao dao;

	public LeaseDbService(Configuration dbCfg) throws Exception {
		this.dao = new LeaseDbDao(dbCfg);
	}

	@Override
	public void saveOrUpdate(LeaseAddress address)
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {

		DbLease conv = EntityMapper.convert((LeaseAddress) address);

		DbLease fromDb = dao.findByAddress(address.getAddress());

		if (fromDb != null) {
			fromDb.setHostname(conv.getHostname());
			fromDb.setLastContact(conv.getLastContact());
			fromDb.setLeasedTo(conv.getLeasedTo());
			dao.update(fromDb);

		} else {

			dao.save(conv);
		}

	}

	@Override
	public Collection<LeaseAddress> readAll()
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {
		return EntityMapper.convert2Address(dao.readAll());
	}

	@Override
	public LeaseAddress findByAddress(InetAddress address)
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {
		return EntityMapper.convert(dao.findByAddress(address));
	}

	@Override
	public Collection<LeaseAddress> findByLeasedMac(HardwareAddress hw)
			throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException {
		return EntityMapper.convert2Address(dao.findByLeasedMac(hw));
	}

	@Override
	public void delete(LeaseAddress address) throws IllegalArgumentException, IllegalAccessException, SQLException {

		dao.delete(EntityMapper.convert(address));

	}

	@Override
	public void close() {
		dao.close();
	}

}
