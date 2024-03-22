package ch.bemar.dhcp.persistence;

import java.util.Collection;

import org.hibernate.query.NativeQuery;

public class AddressDao extends ADoa {

	@Override
	public void update(DbAddress address) {

		if (findByAddress(address.getIp()) != null) {
			updateInTransaction(address);
		}

	}

	@Override
	public void save(DbAddress address) {

		if (findByAddress(address.getIp()) == null) {
			saveInTransaction(address);
		}

	}

	@Override
	public Collection<DbAddress> readAll() {

		String sql = "Select * from DbAddress";

		NativeQuery<DbAddress> query = getSession().createNativeQuery(sql, DbAddress.class);

		return query.list();
	}

	@Override
	public DbAddress findByAddress(String address) {

		return getSession().find(DbAddress.class, address);
	}

	@Override
	public Collection<DbAddress> findByReservedMac(String hw) {

		String sql = "Select * from DbAddress where reservedFor = :reservedFor";

		NativeQuery<DbAddress> query = getSession().createNativeQuery(sql, DbAddress.class);

		query.setParameter("reservedFor", hw);

		return query.list();
	}

	@Override
	public Collection<DbAddress> findByLeasedMac(String hw) {

		String sql = "Select * from DbAddress where leasedTo = :leasedTo";

		NativeQuery<DbAddress> query = getSession().createNativeQuery(sql, DbAddress.class);

		query.setParameter("leasedTo", hw);

		return query.list();
	}

	@Override
	public Collection<DbAddress> findAllWithValidLease() {
		String sql = "Select * from DbAddress where leasedUntil > :now";

		NativeQuery<DbAddress> query = getSession().createNativeQuery(sql, DbAddress.class);

		query.setParameter("now", System.currentTimeMillis());

		return query.list();
	}

	@Override
	public Collection<DbAddress> findAllWithInvalidLease() {
		String sql = "Select * from DbAddress where leasedUntil <= :now";

		NativeQuery<DbAddress> query = getSession().createNativeQuery(sql, DbAddress.class);

		query.setParameter("now", System.currentTimeMillis());

		return query.list();
	}

	@Override
	public void delete(DbAddress address) {
		deleteInTransaction(address);		
	}

}
