package ch.bemar.dhcp.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
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
			log.info("no address");

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
