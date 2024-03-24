package ch.bemar.dhcp.persistence;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.lease.EntityMapper;
import ch.bemar.dhcp.config.lease.LeaseAddress;

public class LeaseDbService implements IService<LeaseAddress, HardwareAddress, InetAddress> {

	private LeaseDbDao dao;

	public LeaseDbService() {
		this.dao = new LeaseDbDao();
	}

	public LeaseDbService(File file) {
		this.dao = new LeaseDbDao(file);
	}

	public LeaseDbService(String ressource) {
		this.dao = new LeaseDbDao(ressource);
	}

	@Override
	public void saveOrUpdate(LeaseAddress address) {

		DbLease conv = EntityMapper.convert((LeaseAddress) address);

		DbLease fromDb = dao.findByAddress(conv.getIp());

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
	public Collection<LeaseAddress> readAll() throws UnknownHostException {
		return EntityMapper.convert2Address(dao.readAll());
	}

	@Override
	public LeaseAddress findByAddress(InetAddress address) throws UnknownHostException {
		return EntityMapper.convert(dao.findByAddress(address.getHostAddress()));
	}

	@Override
	public Collection<LeaseAddress> findByReservedMac(HardwareAddress hw) throws UnknownHostException {
		return EntityMapper.convert2Address(dao.findByReservedMac(hw.getAsMac()));
	}

	@Override
	public Collection<LeaseAddress> findByLeasedMac(HardwareAddress hw) throws UnknownHostException {
		return EntityMapper.convert2Address(dao.findByLeasedMac(hw.getAsMac()));
	}

	@Override
	public void delete(LeaseAddress address) {

		dao.delete(EntityMapper.convert(address));

	}

	@Override
	public void close() {
		dao.close();
	}

}
