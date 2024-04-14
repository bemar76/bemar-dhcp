package ch.bemar.dhcp.config.reader;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.DhcpHostConfig;
import ch.bemar.dhcp.util.StringUtils;

public class HostConfigReader extends AConfigReader {

	public HostConfigReader() {

	}

	public DhcpHostConfig readHost(ConfigFile confFile) throws Exception {

		DhcpHostConfig hostConfig = new DhcpHostConfig();

		String line = confFile.getCurrentLine();

		int openBracket = 1;

		if (line.contains("host") && line.trim().endsWith("{")) {

			handleFirstLine(line, hostConfig);
		}

		while (confFile.hasElements()) {

			line = confFile.getNextLine();

			if (line.trim().endsWith("}")) {

				openBracket--;

				if (openBracket == 0) {
					break;
				}

			} else {

				handleLine(line, hostConfig);
			}

		}

		if (openBracket != 0) {
			throw new IllegalStateException("There are still brackets open but no lines left");
		}

		return hostConfig;
	}

	private void handleLine(String line, DhcpHostConfig config) throws Exception {

//		if (line.trim().startsWith("hardware")) {
//			
//			config.setHardwareAddress(new HardwareAddress(line));
//			
//		} else if (line.trim().startsWith("fixed-address")) {
//			
//			config.setFixedIpAddress(new FixedAddress(line));
//			
//		}else {
			
			super.handleLine(line, config);
//		}

	}

	private void handleFirstLine(String line, DhcpHostConfig hostConfig) throws UnknownHostException {
		line = StringUtils.substringBeforeLast(line, "{");

		String[] tokens = StringUtils.split(line.trim());

		hostConfig.setName(tokens[1]);

	}
}
