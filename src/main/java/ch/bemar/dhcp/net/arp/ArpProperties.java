package ch.bemar.dhcp.net.arp;

import java.io.IOException;
import java.util.Properties;

public class ArpProperties {

	private Properties props;

	public ArpProperties() throws IOException {

		props = new Properties();

		props.load(this.getClass().getResourceAsStream("/arp.properties"));
	}

	public String getCmd() {
		return props.getProperty("cmd");
	}

	public String getTypeDynamic() {
		return props.getProperty("type_dynamic");
	}

	public String getTypeStatic() {
		return props.getProperty("type_fix");
	}

	public int getArpIpColIndex() {
		return Integer.valueOf(props.getProperty("col_arp_ip"));
	}

	public int getArpIpMacIndex() {
		return Integer.valueOf(props.getProperty("col_arp_mac"));
	}

	public int getArpTypeColIndex() {
		return Integer.valueOf(props.getProperty("col_arp_type"));
	}

	public String getIfaceLineStart() {
		return props.getProperty("line_iface_start");
	}

	public int getIfaceIpColIndex() {
		return Integer.valueOf(props.getProperty("col_iface_ip"));
	}

	public int getIfaceNameColIndex() {
		return Integer.valueOf(props.getProperty("col_iface_name"));
	}

	public String getArpHeaderStartWith() {
		return props.getProperty("line_arp_header_start");
	}

	public boolean isArpActive() {
		return "true".equalsIgnoreCase(props.getProperty("arp"));
	}
}
