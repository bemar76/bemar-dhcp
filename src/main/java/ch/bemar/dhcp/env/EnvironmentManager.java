package ch.bemar.dhcp.env;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.codehaus.plexus.util.StringUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvironmentManager {

	private static EnvironmentManager envManager;

	private final Map<String, String> attributes;

	private EnvironmentManager() {
		this.attributes = Maps.newHashMap();

		loadClasspathProperties(EnvConstants.DEFAULT_ARP_PROPERTIES_FILE);
		loadClasspathProperties(EnvConstants.DEFAULT_PROPERTIES_FILE);

	}

	public synchronized static EnvironmentManager getInstance() {
		if (envManager == null) {
			envManager = new EnvironmentManager();
		}

		return envManager;
	}

	public String getEnvAsString(String key) {
		return Optional.ofNullable(System.getenv(key)).orElse(attributes.get(key));
	}

	public List<String> getEnvAsStringList(String key, String separator) {
		String found = Optional.ofNullable(System.getenv(key)).orElse(attributes.get(key));

		if (!Strings.isNullOrEmpty(found)) {
			return Lists.newArrayList(StringUtils.split(found, separator));
		}

		return Lists.newArrayList();
	}

	public Integer getEnvAsInteger(String key) {
		String found = Optional.ofNullable(System.getenv(key)).orElse(attributes.get(key));
		if (found != null) {
			return Integer.valueOf(found.trim());
		}

		return null;
	}

	public Boolean getEnvAsBoolean(String key) {
		return "true".equalsIgnoreCase(Optional.ofNullable(System.getenv(key)).orElse(attributes.get(key)));
	}

	private void loadClasspathProperties(String name) {

		if (!name.trim().startsWith("/")) {
			name = "/" + name;
		}

		Properties props = new Properties();
		try {
			props.load(EnvironmentManager.class.getResourceAsStream(name));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		for (String key : props.stringPropertyNames()) {
			if (!attributes.containsKey(key))
				attributes.put(key, props.getProperty(key));
		}

	}
}
