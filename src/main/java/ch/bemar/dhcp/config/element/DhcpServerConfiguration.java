package ch.bemar.dhcp.config.element;

import java.util.Set;

import org.dhcp4java.DHCPOption;

import ch.bemar.dhcp.config.ConfName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DhcpServerConfiguration {

	@ConfName("default-lease-time")
	private Integer defaultLeaseTime;

	@ConfName("max-lease-time")
	private Integer maxLeaseTime;

	@ConfName("authoritative")
	private boolean authoritative;

	private Set<DHCPOption> options;

	private Set<DhcpSubnetConfig> subnets;

}
