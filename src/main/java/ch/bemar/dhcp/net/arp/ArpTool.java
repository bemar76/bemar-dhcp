package ch.bemar.dhcp.net.arp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dhcp4java.HardwareAddress;

import ch.bemar.dhcp.env.EnvironmentManager;

public class ArpTool {

	ArpTable getTable() throws Exception {

		try {

			Process process = Runtime.getRuntime()
					.exec(EnvironmentManager.getInstance().getEnvAsString(ArpPropertiesConstants.CMD));
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String content = IOUtils.toString(reader);

			process.waitFor();
			reader.close();

			return buildTable(content);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

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

					table.addEntry(iface, getArpEntry(line));

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

	private Arp getArpEntry(String line) throws UnknownHostException {
		String[] tokens = StringUtils.split(line.trim());

		return new Arp(
				InetAddress.getByName(
						tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_IP)]),
				new HardwareAddress(
						tokens[EnvironmentManager.getInstance().getEnvAsInteger(ArpPropertiesConstants.COL_MAC)]),
				EnvironmentManager.getInstance().getEnvAsString(ArpPropertiesConstants.TYPE_DYNAMIC).equals(tokens[2]));
	}
}