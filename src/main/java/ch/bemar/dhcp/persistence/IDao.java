package ch.bemar.dhcp.persistence;

import java.util.Collection;

public interface IDao<T, M, A> {

	public void update(T address);

	public Collection<T> readAll();

	public T findByAddress(A address);

	public Collection<T> findByReservedMac(M hw);

	public Collection<T> findByLeasedMac(M hw);

	public Collection<T> findAllWithValidLease();

}
