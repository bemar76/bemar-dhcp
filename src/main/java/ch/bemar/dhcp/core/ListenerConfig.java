package ch.bemar.dhcp.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openjdk.tools.sjavac.Log;

import lombok.Getter;

@Getter
public class ListenerConfig {

	public ListenerConfig(String cfg) {
		String[] tokens = cfg.split(":");

		if (tokens.length != 3) {
			throw new IllegalArgumentException("The listener config must be interfaceName/IP, port and subnet name");
		}

		try {
			InetAddress address = InetAddress.getByName(tokens[0].trim());
			this.address = address;
			this.ifaceName = null;

		} catch (UnknownHostException e) {
			Log.info("no address");

			this.ifaceName = tokens[0].trim();
			this.address = null;
		}

		port = Integer.valueOf(tokens[1].trim());
		name = tokens[2].trim();

	}

	private InetAddress address;
	private String ifaceName;
	private final int port;
	private final String name;

}
