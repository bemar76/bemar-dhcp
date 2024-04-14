package ch.bemar.dhcp.config.reader;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.DhcpKeyConfig;
import ch.bemar.dhcp.config.element.Algorithm;
import ch.bemar.dhcp.config.element.Key;
import ch.bemar.dhcp.config.element.Secret;
import ch.bemar.dhcp.util.StringUtils;

public class KeyConfigReader {

	public DhcpKeyConfig readKey(ConfigFile confFile) throws UnknownHostException {
		DhcpKeyConfig keyconfig = new DhcpKeyConfig();

		String line = confFile.getCurrentLine();

		int openBracket = 1;

		if (line.contains("key") && line.trim().endsWith("{")) {

			keyconfig.setKey(new Key(StringUtils.remove(line, "{")));
			String key = keyconfig.getKey().getValue();
			if (key.trim().startsWith("\"") && key.trim().endsWith("\"")) {
				key = StringUtils.substringAfter(key, "\"");
				key = StringUtils.substringBeforeLast(key, "\"");
				keyconfig.getKey().setValue(key);
			}

		}

		while (confFile.hasElements()) {

			line = confFile.getNextLine();

			if (line.trim().endsWith("}")) {

				openBracket--;

				if (openBracket == 0) {
					break;
				}

			} else if (line.trim().startsWith("algorithm")) {

				keyconfig.setAlgorithm(new Algorithm(line));

			} else if (line.trim().startsWith("secret")) {

				keyconfig.setSecret(new Secret(line));

			}

		}

		if (openBracket != 0) {
			throw new IllegalStateException("There are still brackets open but no lines left");
		}

		return keyconfig;
	}

}
