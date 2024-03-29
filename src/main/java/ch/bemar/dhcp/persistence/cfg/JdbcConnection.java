package ch.bemar.dhcp.persistence.cfg;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class JdbcConnection {
	private List<Property> properties;
	private List<Mapping> mappings;

	@XmlElement(name = "property")
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	@XmlElement(name = "mapping")
	public List<Mapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<Mapping> mapping) {
		this.mappings = mapping;
	}

	public Object getPropertyByName(String propName) {

		for (Property prop : properties) {
			if (prop.getName().equalsIgnoreCase(propName)) {
				return prop;
			}
		}

		return null;
	}
	
	public Object getMappingByClassname(String classname) {

		for (Mapping mapping : mappings) {
			if (mapping.getClazz().equalsIgnoreCase(classname)) {
				return mapping;
			}
		}

		return null;
	}
}