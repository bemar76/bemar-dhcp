package ch.bemar.dhcp.config;

import java.util.Set;

import org.dhcp4java.DHCPOption;
import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.element.Subnet;
import lombok.Data;

@Data
public class DhcpHostConfig extends BaseConfiguration {

	private HardwareAddress macAddress;

	private Subnet fixedIpAddress;

	private Set<DHCPOption> options;
}
