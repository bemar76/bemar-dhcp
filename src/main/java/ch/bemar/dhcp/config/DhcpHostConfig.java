package ch.bemar.dhcp.config;

import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.config.element.FixedAddress;
import ch.bemar.dhcp.config.element.NextServer;
import lombok.Data;

@Data
public class DhcpHostConfig extends BaseConfiguration {

	private String name;

	private HardwareAddress hardwareAddress;

	private FixedAddress fixedIpAddress;

	private NextServer nextServer;

	public String toString() {
		return machMalString(this);
	}

}
