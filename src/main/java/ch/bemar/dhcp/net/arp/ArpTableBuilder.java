package ch.bemar.dhcp.net.arp;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.env.EnvironmentManager;

public class ArpTableBuilder {

	ArpTable buildTable(String content) {
		ArpTable table = new ArpTable();

		try {

			BufferedReader reader = new BufferedReader(new StringReader(content));

			Interface iface = null;
			String line;

			while ((line = reader.readLine()) != null) {

				if (line.trim().startsWith(
						EnvironmentManager.getInstance().getEnvAsString(ArpPropertiesConstants.LINE_IFACE_START))) {

					iface = getInterface(line);

				} else if (!line.trim().isEmpty() && !line.trim().startsWith(EnvironmentManager.getInstance()
						.getEnvAsString(ArpPropertiesConstants.COL_LINE_ARP_HEADER_START))) {

					table.addEntry(getArpEntry(iface, line));

				}

			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return table;
	}

	private Interface getInterface(String line) throws UnknownHostException {

		String[] tokens = StringUtils.split(line.trim());

		return new Interface(
				InetAddress.getByName(
						tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_IFACE_IP)]),
				tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_IFACE_NAME)]);

	}

	private ArpEntry getArpEntry(Interface iface, String line) throws UnknownHostException {
		String[] tokens = StringUtils.split(line.trim());

		if (tokens.length == 2) {
			return new ArpEntry(iface,
					InetAddress.getByName(
							tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_IP)]),
					new HardwareAddress(new byte[6]), getArpType(
							tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_MAC)])); // because
																														// of
																														// missing
																														// mac
																														// this
																														// is
																														// the
																														// type
																														// col
		}

		if (tokens.length == 3) {
			return new ArpEntry(iface,
					InetAddress.getByName(
							tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_IP)]),
					new HardwareAddress(
							tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_MAC)]),
					getArpType(
							tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_TYPE)]));
		}

		return null;

	}

	private ArpType getArpType(final String token) {
		if (EnvironmentManager.getInstance().getEnvAsString(ArpPropertiesConstants.TYPE_DYNAMIC).equals(token)) {
			return ArpType.DYNAMIC;
		} else if (EnvironmentManager.getInstance().getEnvAsString(ArpPropertiesConstants.TYPE_FIXED).equals(token)) {
			return ArpType.FIXED;
		} else if (EnvironmentManager.getInstance().getEnvAsString(ArpPropertiesConstants.TYPE_INVALID).equals(token)) {
			return ArpType.INVALID;
		}
		return ArpType.UNKNOWN;
	}
}