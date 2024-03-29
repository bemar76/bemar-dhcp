package ch.bemar.dhcp.persistence.cfg;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
public class Configuration {

	public static final String PROP_DRIVER_CLASS = "connection.driver_class";
	public static final String PROP_CON_URL = "connection.url";
	public static final String PROP_USERNAME = "connection.username";
	public static final String PROP_PASSWORD = "connection.password";

	private JdbcConnection jdbcConnection;

	@XmlElement(name = "jdbc-connection")
	public JdbcConnection getJdbcConnection() {
		return jdbcConnection;
	}

	public void setJdbcConnection(JdbcConnection jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}
}
