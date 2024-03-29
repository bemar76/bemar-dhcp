package ch.bemar.dhcp.persistence.cfg;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Mapping {
	private String className;

	@XmlAttribute(name = "class")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
