package ch.bemar.dhcp.persistence;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ADbDao implements IDao<DbLease, String, String>, SqlTransactionExecutor {

	private static SessionFactory sessionFactory;
	private Session session;

	protected ADbDao(File file) {
		sessionFactory = new Configuration().configure(file).buildSessionFactory();
	}

	protected ADbDao(String ressource) {
		sessionFactory = new Configuration().configure(ressource).buildSessionFactory();
	}

	protected ADbDao() {
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

	public void saveInTransaction(DbLease address) {

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

	public void updateInTransaction(DbLease address) {

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

	public void deleteInTransaction(DbLease address) {

		Transaction tx = null;

		try {

			tx = getStartedTransaction();

			DbLease fromDb = getSession().find(DbLease.class, address.getIp());

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

	public void close() {

		if (session != null && session.isOpen()) {
			log.info("Shutting down db session");
			session.close();
			log.info("Shutting down db sessionfactory");
			sessionFactory.close();
		}

	}
}
