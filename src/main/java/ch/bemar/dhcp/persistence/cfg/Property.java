package ch.bemar.dhcp.persistence.cfg;

import jakarta.xml.bind.annotation.XmlElement;

public class Property {
	private String name;
	private String value;

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}