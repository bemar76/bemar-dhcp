package ch.bemar.dhcp.persistence.cfg;

import jakarta.xml.bind.annotation.XmlElement;

public class Mapping {
	private String clazz;

	@XmlElement(name = "class")
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
}