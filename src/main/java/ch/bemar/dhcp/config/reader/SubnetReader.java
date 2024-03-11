package ch.bemar.dhcp.config.reader;

import ch.bemar.dhcp.config.DhcpSubnetConfig;

public class SubnetReader {

	private HostConfigReader hostConfigurationReader;

	public SubnetReader() {
		this.hostConfigurationReader = new HostConfigReader();
	}

	public DhcpSubnetConfig readSubnet(ConfigFile confFile) {

		DhcpSubnetConfig subnet = new DhcpSubnetConfig();

		while (confFile.hasElements()) {

			if (confFile.getPreview().toLowerCase().startsWith("host")) {

				subnet.getHosts().add(hostConfigurationReader.readHost(confFile));

			}

		}
		
		return subnet;

	}

}
