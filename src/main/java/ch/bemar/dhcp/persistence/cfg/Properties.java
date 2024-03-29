package ch.bemar.dhcp.persistence.cfg;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class Properties {
	private List<Property> propertyList;

	@XmlElement(name = "property")
	public List<Property> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<Property> propertyList) {
		this.propertyList = propertyList;
	}

	public Integer size() {
		return propertyList.size();
	}
}
