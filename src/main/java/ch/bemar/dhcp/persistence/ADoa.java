package ch.bemar.dhcp.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ADoa implements IDao<DbAddress, String, String>, SqlTransactionExecutor {

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

	public Transaction getStartedTransaction() {
		return session.beginTransaction();
	}

	public void saveInTransaction(DbAddress address) {

		Transaction tx = null;

		try {

			tx = getStartedTransaction();

			getSession().save(address);

			tx.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (tx != null)
				tx.rollback();
		} finally {
		}
	}

	public void updateInTransaction(DbAddress address) {

		Transaction tx = null;

		try {

			tx = getStartedTransaction();

			getSession().update(address);

			tx.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (tx != null)
				tx.rollback();
		} finally {
		}
	}

	public void deleteInTransaction(DbAddress address) {

		Transaction tx = null;

		try {

			tx = getStartedTransaction();

			DbAddress fromDb = getSession().find(DbAddress.class, address.getIp());
			
			if (fromDb != null)
				getSession().delete(fromDb);

			tx.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (tx != null)
				tx.rollback();
		} finally {
		}
	}

}
