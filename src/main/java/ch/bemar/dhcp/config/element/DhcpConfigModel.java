package ch.bemar.dhcp.config.element;

import java.util.Map;
import java.util.Set;

import org.dhcp4java.DHCPOption;

public class DhcpConfigModel {

	private Map<String, DHCPOption> options;

	private Set<DhcpSubnetConfig> subnets;

}
