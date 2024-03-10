package ch.bemar.dhcp.config.element;

import java.net.InetAddress;
import java.util.Set;

import org.dhcp4java.DHCPOption;

import ch.bemar.dhcp.config.ConfName;
import lombok.Data;

@Data
public class DhcpSubnetConfig {

	@ConfName("subnet")
	private InetAddress subnetAddress;

	@ConfName("netmask")
	private InetAddress netmask;

	@ConfName("range")
	private IpRange range;

	@ConfName("default-lease-time")
	private Integer defaultLeaseTime;

	@ConfName("max-lease-time")
	private Integer maxLeaseTime;

	private Set<DHCPOption> options;

	private Set<HostConfiguration> hosts;

}