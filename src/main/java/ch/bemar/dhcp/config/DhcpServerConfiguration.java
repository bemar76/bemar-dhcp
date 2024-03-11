package ch.bemar.dhcp.config;

import java.util.Set;

import org.dhcp4java.DHCPOption;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DhcpServerConfiguration extends BaseConfiguration{

	

	private Set<DHCPOption> options;

	private Set<DhcpSubnetConfig> subnets;

}
