package ch.bemar.dhcp.persistence.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import ch.bemar.dhcp.persistence.IEntityFactory;
import ch.bemar.dhcp.persistence.SqlBaseMethods;
import ch.bemar.dhcp.persistence.StatementBuilder;
import ch.bemar.dhcp.persistence.cfg.Configuration;
import ch.bemar.dhcp.persistence.cfg.Mapping;
import ch.bemar.dhcp.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * simple substitute for hibernate because its hell on earth to bring hibernate
 * to native compilation (Windows, Linux, Mac)
 * 
 * @author bemar
 *
 * @param <T>
 */
@Slf4j
public abstract class DbDao<T> implements SqlBaseMethods<T> {

	private Connection connection;

	private Configuration dbCfg;

	private StatementBuilder<T> stmtBuilder;

	private IEntityFactory<T> factory;

	protected DbDao(Configuration dbCfg, IEntityFactory<T> factory) throws Exception {
		this.factory = factory;
		this.stmtBuilder = new StatementBuilder<>();
		setConfig(dbCfg);
		prepareDatabase();
	}

	private void prepareDatabase() throws IllegalArgumentException, IllegalAccessException, SQLException, IOException {

		try {
			findByExample(factory.getEmptyEntity());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (org.h2.jdbc.JdbcSQLSyntaxErrorException e) {
			if (StringUtils.containsRegex(e.getMessage(), "Table .* not found")) {
				create(factory.getEmptyEntity());
			} else {
				log.error(e.getMessage(), e);
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	private void setConfig(Configuration config) throws Exception {
		dbCfg = config;

		try {

			Class.forName(dbCfg.getConnection().getPropertyValueByName(Configuration.PROP_DRIVER_CLASS).toString());

			connection = DriverManager.getConnection(
					dbCfg.getConnection().getPropertyValueByName(Configuration.PROP_CON_URL).toString(),
					dbCfg.getConnection().getPropertyValueByName(Configuration.PROP_USERNAME).toString(),
					dbCfg.getConnection().getPropertyValueByName(Configuration.PROP_PASSWORD).toString());

		} catch (ClassNotFoundException | SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	protected Statement createStatement() throws SQLException {

		return connection.createStatement();
	}

	public void close() {
		try {

			if (connection != null && !connection.isClosed()) {
				log.info("Shutting down db session");
				connection.close();
			}

		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	private void checkEntityMapping(T entity) throws SQLException {
		for (Mapping mapping : dbCfg.getConnection().getMappings().getMappingList()) {
			if (mapping.getClassName().equals(entity.getClass().getName())) {
				return;
			}
		}

		throw new SQLException("The entity " + entity.getClass() + " is not in jdbc mapping");
	}

	@Override
	public void update(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException {
		checkEntityMapping(entity);

		Statement stmt = connection.createStatement();

		String query = stmtBuilder.update(entity);

		stmt.executeUpdate(query);
	}

	@Override
	public void save(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException {
		checkEntityMapping(entity);

		Statement stmt = connection.createStatement();

		String query = stmtBuilder.insert(entity);

		stmt.execute(query);

	}

	@Override
	public Collection<T> findByExample(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException {
		checkEntityMapping(entity);

		Statement stmt = connection.createStatement();

		String query = stmtBuilder.select(entity, false);

		ResultSet rs = stmt.executeQuery(query);

		return getEntitiesFromResultSet(rs);
	}

	@Override
	public void delete(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException {
		checkEntityMapping(entity);

		Statement stmt = connection.createStatement();

		String query = stmtBuilder.delete(entity);

		stmt.execute(query);
	}

	public void create(T entity) throws SQLException, IllegalArgumentException, IllegalAccessException, IOException {
		checkEntityMapping(entity);

		Statement stmt = connection.createStatement();

		InputStream is = this.getClass().getResourceAsStream("/" + entity.getClass().getSimpleName() + ".sql");
		String create = IOUtils.toString(is, StandardCharsets.UTF_8);

		stmt.execute(create);
	}

	public List<T> getEntitiesFromResultSet(ResultSet rs) throws SQLException {
		if (rs == null) {
			throw new IllegalArgumentException("ResultSet darf nicht null sein.");
		}

		List<T> leases = Lists.newArrayList();

		while (rs.next()) {
			T lease = factory.getFromResultSet(rs);

			leases.add(lease);
		}

		return leases;
	}

}
