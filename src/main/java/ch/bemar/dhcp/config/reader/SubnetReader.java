package ch.bemar.dhcp.config.reader;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.DhcpHostConfig;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.element.Netmask;
import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.util.StringUtils;

public class SubnetReader extends AConfigReader {

	private HostConfigReader hostConfigurationReader;

	public SubnetReader() {
		this.hostConfigurationReader = new HostConfigReader();
	}

	public DhcpSubnetConfig readSubnet(ConfigFile confFile) throws Exception {

		DhcpSubnetConfig subnetConfig = new DhcpSubnetConfig();

		String line = confFile.getCurrentLine();

		int openBracket = 1;

		if (line.contains("subnet") && line.contains("netmask") && line.trim().endsWith("{")) {

			handleFirstLine(line, subnetConfig);
		}

		while (confFile.hasElements()) {

			line = confFile.getNextLine();

			if (line.trim().endsWith("}")) {

				openBracket--;

				if (openBracket == 0) {
					break;
				}

			} else if (line.trim().startsWith("host")) {

				openBracket++;

				DhcpHostConfig host = hostConfigurationReader.readHost(confFile);
				subnetConfig.getHosts().add(host);

				openBracket--;

			} else {

				handleLine(line, subnetConfig);
			}

		}

		if (openBracket != 0) {
			throw new IllegalStateException("There are still brackets open but no lines left");
		}

		return subnetConfig;
	}

	private void handleFirstLine(String line, DhcpSubnetConfig subnetConfig) throws UnknownHostException {
		line = StringUtils.substringBeforeLast(line, "{");

		String[] tokens = StringUtils.splitRespectsQuotes(line);

		Subnet subnet = new Subnet(tokens[0] + " " + tokens[1]);
		Netmask netmask = new Netmask(tokens[2] + " " + tokens[3]);

		subnetConfig.setSubnetAddress(subnet);
		subnetConfig.setNetmask(netmask);
	}
}
