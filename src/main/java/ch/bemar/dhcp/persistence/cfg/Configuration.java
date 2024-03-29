package ch.bemar.dhcp.persistence.cfg;

import jakarta.xml.bind.annotation.XmlElement;


import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
public class Configuration {
	
    public static final String PROP_DRIVER_CLASS = "connection.driver_class";
    public static final String PROP_CON_URL = "connection.url";
    public static final String PROP_USERNAME = "connection.username";
    public static final String PROP_PASSWORD = "connection.password";
	
	private Connection connection;

	@XmlElement(name = "connection")
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
