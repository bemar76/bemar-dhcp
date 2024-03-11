package ch.bemar.dhcp.config;

import java.util.Set;

import org.dhcp4java.DHCPOption;

import ch.bemar.dhcp.config.element.IpRange;
import ch.bemar.dhcp.config.element.Netmask;
import ch.bemar.dhcp.config.element.Subnet;
import lombok.Data;

@Data
public class DhcpSubnetConfig extends BaseConfiguration {

	private Subnet subnetAddress;

	private Netmask netmask;

	private IpRange range;

	private Set<DHCPOption> options;

	private Set<DhcpHostConfig> hosts;

}
