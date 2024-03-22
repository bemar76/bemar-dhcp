package ch.bemar.dhcp.persistence;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.mgmt.Address;
import ch.bemar.dhcp.config.mgmt.EntityMapper;

public class AddressService implements IService<Address, HardwareAddress, InetAddress> {

	private AddressDao dao;

	public AddressService() {
		this.dao = new AddressDao();
	}

	@Override
	public void saveOrUpdate(Address address) {

		DbAddress conv = EntityMapper.convert((Address) address);

		DbAddress fromDb = dao.findByAddress(conv.getIp());

		if (fromDb != null) {
			fromDb.setArp(conv.isArp());
			fromDb.setConflict(conv.isConflict());
			fromDb.setDefaultLeaseTime(conv.getDefaultLeaseTime());
			fromDb.setHostname(conv.getHostname());
			fromDb.setLastContact(conv.getLastContact());
			fromDb.setLeasedTo(conv.getLeasedTo());
			fromDb.setLeasedUntil(conv.getLeasedUntil());
			fromDb.setMaxLeaseTime(conv.getMaxLeaseTime());
			fromDb.setReservedFor(conv.getReservedFor());
			fromDb.setSubnet(conv.getSubnet());

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
	public Collection<Address> findAllWithValidLease() throws UnknownHostException {
		return EntityMapper.convert2Address(dao.findAllWithValidLease());
	}

	@Override
	public Collection<Address> findAllWithInvalidLease() throws UnknownHostException {
		return EntityMapper.convert2Address(dao.findAllWithInvalidLease());
	}

	@Override
	public void delete(Address address) {

		dao.delete(EntityMapper.convert(address));

	}

}
