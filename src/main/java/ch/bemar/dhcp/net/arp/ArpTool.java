package ch.bemar.dhcp.net.arp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;
import org.dhcp4java.HardwareAddress;
import org.hibernate.tool.hbm2x.StringUtils;

public class ArpTool {

	private ArpProperties props;

	public ArpTool() throws IOException {
		props = new ArpProperties();
	}

	ArpTable getTable() throws Exception {

		try {

			Process process = Runtime.getRuntime().exec(props.getCmd());
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

				if (line.trim().startsWith(props.getIfaceLineStart())) {

					iface = getInterface(line);

				} else if (!line.trim().isEmpty() && !line.trim().startsWith(props.getArpHeaderStartWith())) {

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

		return new Interface(InetAddress.getByName(tokens[props.getIfaceIpColIndex()]),
				tokens[props.getIfaceNameColIndex()]);

	}

	private Arp getArpEntry(String line) throws UnknownHostException {
		String[] tokens = StringUtils.split(line.trim());

		return new Arp(InetAddress.getByName(tokens[props.getArpIpColIndex()]),
				new HardwareAddress(tokens[props.getArpIpMacIndex()]), props.getTypeDynamic().equals(tokens[2]));
	}
}