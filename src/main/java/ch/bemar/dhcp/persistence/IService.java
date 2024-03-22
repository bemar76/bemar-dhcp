package ch.bemar.dhcp.persistence;

import java.net.UnknownHostException;
import java.util.Collection;

public interface IService<T, M, A> {
	
	public void saveOrUpdate(T address);
	
	public Collection<T> readAll() throws UnknownHostException;

	public T findByAddress(A address) throws UnknownHostException;

	public Collection<T> findByReservedMac(M hw) throws UnknownHostException;

	public Collection<T> findByLeasedMac(M hw) throws UnknownHostException;

	public Collection<T> findAllWithValidLease() throws UnknownHostException;

	public Collection<T> findAllWithInvalidLease() throws UnknownHostException;
	
	public void delete(T address);

}
