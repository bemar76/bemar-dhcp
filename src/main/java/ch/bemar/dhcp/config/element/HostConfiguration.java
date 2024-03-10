package ch.bemar.dhcp.config.element;

import java.net.InetAddress;
import java.util.Map;
import java.util.Set;

import org.dhcp4java.DHCPOption;
import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.ConfName;
import lombok.Data;

@Data
public class HostConfiguration {

	@ConfName("hostname")
	private String hostname;

	@ConfName("hardware ethernet")
	private HardwareAddress macAddress;

	@ConfName("fixed-address")
	private InetAddress fixedIpAddress;

	@ConfName("hardware-type")
	private String hardwareType;

	@ConfName("default-lease-time")
	private Integer leaseTime;

	private Set<DHCPOption> options;
}
