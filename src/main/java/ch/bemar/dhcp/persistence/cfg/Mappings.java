package ch.bemar.dhcp.persistence.cfg;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class Mappings {
	private List<Mapping> mappingList;

	@XmlElement(name = "mapping")
	public List<Mapping> getMappingList() {
		return mappingList;
	}

	public void setMappingList(List<Mapping> mappingList) {
		this.mappingList = mappingList;
	}

	public Integer size() {
		return mappingList.size();
	}
}
