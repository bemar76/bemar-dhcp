package ch.bemar.dhcp.config;

import java.util.Set;

import com.google.common.collect.Sets;

import ch.bemar.dhcp.config.element.IpRange;
import ch.bemar.dhcp.config.element.Netmask;
import ch.bemar.dhcp.config.element.Subnet;
import lombok.Data;

@Data
public class DhcpSubnetConfig extends BaseConfiguration {
	
	public DhcpSubnetConfig() {
		this.hosts = Sets.newHashSet();
	}

	private Subnet subnetAddress;

	private Netmask netmask;

	private IpRange range;	

	private Set<DhcpHostConfig> hosts;
	
	public String toString() {
		return machMalString(this);
	}

}
