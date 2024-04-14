package ch.bemar.dhcp.config;

import ch.bemar.dhcp.config.element.Key;
import ch.bemar.dhcp.config.element.Port;
import ch.bemar.dhcp.config.element.Primary;
import lombok.Data;

@Data
public class DhcpZoneConfig extends BaseConfiguration {

	private String zoneName;
	
	private Primary primary;
	
	private Port port;
	
	private Key key;
	
	public String toString() {
		return machMalString(this);
	}
}
