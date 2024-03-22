package ch.bemar.dhcp.persistence;

import java.net.UnknownHostException;
import java.util.Collection;

public interface IDao<T, M, A> {

	public void save(T address);
	
	public void update(T address);
	
	public void delete(T address);
	
	public Collection<T> readAll() throws UnknownHostException;

	public T findByAddress(A address) throws UnknownHostException;

	public Collection<T> findByReservedMac(M hw) throws UnknownHostException;

	public Collection<T> findByLeasedMac(M hw) throws UnknownHostException;

	public Collection<T> findAllWithValidLease() throws UnknownHostException;

	public Collection<T> findAllWithInvalidLease() throws UnknownHostException;

}
