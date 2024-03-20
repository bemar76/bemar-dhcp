package ch.bemar.dhcp.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

public class AddressDao implements IDao<DbAddress, String, String> {

	private static final SessionFactory sessionFactory;
	private static Session session;

	static {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	public Session getSession() {
		if (session == null || !session.isOpen()) {
			session = sessionFactory.openSession();
		}

		return session;
	}

	@Override
	public void update(DbAddress address) {
		getSession().update(address);
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

}
