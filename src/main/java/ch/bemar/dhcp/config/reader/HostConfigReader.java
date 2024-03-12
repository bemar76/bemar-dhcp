package ch.bemar.dhcp.config.reader;

import ch.bemar.dhcp.config.DhcpHostConfig;

public class HostConfigReader extends AConfigReader{

	public HostConfigReader() {

	}

	public DhcpHostConfig readHost(ConfigFile confFile) {

		DhcpHostConfig host = new DhcpHostConfig();

		return host;
	}

}
