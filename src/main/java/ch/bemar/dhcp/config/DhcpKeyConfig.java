package ch.bemar.dhcp.config;

import ch.bemar.dhcp.config.element.Algorithm;
import ch.bemar.dhcp.config.element.Key;
import ch.bemar.dhcp.config.element.Secret;
import lombok.Data;

@Data
public class DhcpKeyConfig extends BaseConfiguration{
	
	private Key key;
	
	private Algorithm algorithm ;
	
	private Secret secret;
	
	public String toString() {
		return machMalString(this);
	}
}
