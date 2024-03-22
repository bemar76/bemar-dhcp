package ch.bemar.dhcp.persistence;

import java.io.File;
import java.util.Collection;

import org.hibernate.query.NativeQuery;

public class LeaseDbDao extends ADbDao {

	private static final String DBNAME = DbLease.class.getSimpleName();
	private static final String SELECT = "Select * from " + DBNAME;

	public LeaseDbDao() {
		super();
	}

	public LeaseDbDao(File file) {
		super(file);
	}

	public LeaseDbDao(String ressource) {
		super(ressource);
	}

	@Override
	public void update(DbLease address) {

		if (findByAddress(address.getIp()) != null) {
			updateInTransaction(address);
		}

	}

	@Override
	public void save(DbLease address) {

		if (findByAddress(address.getIp()) == null) {
			saveInTransaction(address);
		}

	}

	@Override
	public Collection<DbLease> readAll() {

		NativeQuery<DbLease> query = getSession().createNativeQuery(SELECT, DbLease.class);

		return query.list();
	}

	@Override
	public DbLease findByAddress(String address) {

		return getSession().find(DbLease.class, address);
	}

	@Override
	public Collection<DbLease> findByReservedMac(String hw) {

		String sql = SELECT + " where reservedFor = :reservedFor";

		NativeQuery<DbLease> query = getSession().createNativeQuery(sql, DbLease.class);

		query.setParameter("reservedFor", hw);

		return query.list();
	}

	@Override
	public Collection<DbLease> findByLeasedMac(String hw) {

		String sql = SELECT + " where leasedTo = :leasedTo";

		NativeQuery<DbLease> query = getSession().createNativeQuery(sql, DbLease.class);

		query.setParameter("leasedTo", hw);

		return query.list();
	}

	@Override
	public void delete(DbLease address) {
		deleteInTransaction(address);
	}

}
