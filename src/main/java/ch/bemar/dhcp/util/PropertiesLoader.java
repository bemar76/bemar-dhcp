package ch.bemar.dhcp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesLoader {

	private PropertiesLoader() {
	}

	public static Properties loadProperties(String name) throws IOException {

		Properties properties = new Properties();
		// try to load default configuration file
		InputStream propFileStream = PropertiesLoader.class.getResourceAsStream("/" + name);
		if (propFileStream != null) {
			properties.load(propFileStream);
		} else {
			log.error("Could not load {}", name);
		}

		return properties;
	}

}
