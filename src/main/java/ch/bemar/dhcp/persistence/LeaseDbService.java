package ch.bemar.dhcp.persistence;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.mgmt.Address;
import ch.bemar.dhcp.config.mgmt.EntityMapper;

public class LeaseDbService implements IService<Address, HardwareAddress, InetAddress> {

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
	public void saveOrUpdate(Address address) {

		DbLease conv = EntityMapper.convert((Address) address);

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
	public Collection<Address> readAll() throws UnknownHostException {
		return EntityMapper.convert2Address(dao.readAll());
	}

	@Override
	public Address findByAddress(InetAddress address) throws UnknownHostException {
		return EntityMapper.convert(dao.findByAddress(address.getHostAddress()));
	}

	@Override
	public Collection<Address> findByReservedMac(HardwareAddress hw) throws UnknownHostException {
		return EntityMapper.convert2Address(dao.findByReservedMac(hw.getAsMac()));
	}

	@Override
	public Collection<Address> findByLeasedMac(HardwareAddress hw) throws UnknownHostException {
		return EntityMapper.convert2Address(dao.findByLeasedMac(hw.getAsMac()));
	}

	@Override
	public void delete(Address address) {

		dao.delete(EntityMapper.convert(address));

	}

	@Override
	public void close() {
		dao.close();
	}

}
