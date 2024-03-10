package ch.bemar.dhcp.config.element;

import java.net.InetAddress;
import java.util.Map;

import org.dhcp4java.DHCPOption;
import org.dhcp4java.HardwareAddress;

public class FixedIpConfig {

	private HardwareAddress hardwareAdress;
	private InetAddress fixedAddress;

	private Map<String, DHCPOption> options;

}
