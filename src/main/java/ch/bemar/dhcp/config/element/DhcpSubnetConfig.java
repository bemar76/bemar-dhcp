package ch.bemar.dhcp.config.element;

import java.util.Map;
import java.util.Set;

import org.dhcp4java.DHCPOption;
import org.dhcp4java.InetCidr;

import lombok.Data;

@Data
public class DhcpSubnetConfig {

	private InetCidr subnet;	
	private IpRange range;

	private Map<String, DHCPOption> options;

	private int defaultLeaseTime;
	private int maxLeaseTime;

	private Set<FixedIpConfig> fixedIpConfigs;

}
