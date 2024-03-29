package ch.bemar.dhcp.persistence.cfg;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class Connection {
	private Properties properties;
	private Mappings mappings;

	@XmlElement(name = "properties")
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@XmlElement(name = "mappings")
	public Mappings getMappings() {
		return mappings;
	}

	public void setMappings(Mappings mappings) {
		this.mappings = mappings;
	}

	public String getPropertyValueByName(String propName) {

		for (Property prop : properties.getPropertyList()) {
			if (prop.getName().equalsIgnoreCase(propName)) {
				return prop.getValue().trim();
			}
		}

		return null;
	}

	public boolean containsClassname(String classname) {

		for (Mapping mapping : mappings.getMappingList()) {
			if (mapping.getClassName().equalsIgnoreCase(classname)) {
				return true;
			}
		}

		return false;
	}
}
