package ch.bemar.dhcp.persistence;

import java.sql.SQLException;
import java.util.Collection;

public interface SqlBaseMethods<T> {

	void update(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException;

	void save(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException;

	void delete(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException;

	Collection<T> findByExample(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException;

}