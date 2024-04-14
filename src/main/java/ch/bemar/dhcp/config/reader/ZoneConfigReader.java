package ch.bemar.dhcp.config.reader;

import java.net.UnknownHostException;

import org.apache.maven.shared.utils.StringUtils;

import ch.bemar.dhcp.config.DhcpZoneConfig;
import ch.bemar.dhcp.config.element.Key;
import ch.bemar.dhcp.config.element.Port;
import ch.bemar.dhcp.config.element.Primary;

public class ZoneConfigReader {

	public DhcpZoneConfig readZone(ConfigFile confFile) throws UnknownHostException {
		DhcpZoneConfig zoneConfig = new DhcpZoneConfig();

		String line = confFile.getCurrentLine();

		int openBracket = 1;

		if (line.contains("zone") && line.trim().endsWith("{")) {

			String[] tokens = StringUtils.split(line);
			zoneConfig.setZoneName(tokens[1]);

		}

		while (confFile.hasElements()) {

			line = confFile.getNextLine();

			if (line.trim().endsWith("}")) {

				openBracket--;

				if (openBracket == 0) {
					break;
				}

			} else if (line.trim().startsWith("primary")) {

				zoneConfig.setPrimary(new Primary(line));

			} else if (line.trim().startsWith("key")) {

				zoneConfig.setKey(new Key(line));

			} else if (line.trim().startsWith("port")) {

				zoneConfig.setPort(new Port(line));

			}

		}

		if (openBracket != 0) {
			throw new IllegalStateException("There are still brackets open but no lines left");
		}

		return zoneConfig;
	}

}
