package ch.bemar.dhcp.persistence;

import java.net.InetAddress;
import java.util.Collection;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.mgmt.IAddress;

public class AddressService implements IService{

	@Override
	public void update(IAddress address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<IAddress> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAddress findByAddress(InetAddress address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IAddress> findByReservedMac(HardwareAddress hw) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IAddress> findByLeasedMac(HardwareAddress hw) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IAddress> findAllWithValidLease() {
		// TODO Auto-generated method stub
		return null;
	}

}
