package ch.bemar.dhcp.config;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.Data;

@Data
public class DhcpServerConfiguration extends BaseConfiguration {

	public DhcpServerConfiguration() {
		this.subnets = Sets.newHashSet();
	}

	private Set<DhcpSubnetConfig> subnets;

	public String toString() {
		return machMalString(this);
	}

}
