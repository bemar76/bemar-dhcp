package ch.bemar.dhcp.persistence;

import java.util.Collection;

public interface ILeaseDao<T, M, A> {

	public void save(T address) throws Exception;
	
	public void update(T address) throws Exception;
	
	public void delete(T address) throws Exception;
	
	public Collection<T> readAll() throws Exception;

	public T findByAddress(A address) throws Exception;

	public Collection<T> findByLeasedMac(M hw) throws Exception;
	

}
