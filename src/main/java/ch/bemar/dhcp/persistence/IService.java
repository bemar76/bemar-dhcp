package ch.bemar.dhcp.persistence;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Collection;

public interface IService<T, M, A> {
	
	public void saveOrUpdate(T address) throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException;
	
	public Collection<T> readAll() throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException;

	public T findByAddress(A address) throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException;

	public Collection<T> findByLeasedMac(M hw) throws UnknownHostException, IllegalArgumentException, IllegalAccessException, SQLException;

	public void delete(T address) throws IllegalArgumentException, IllegalAccessException, SQLException;
	
	public void close();

}
