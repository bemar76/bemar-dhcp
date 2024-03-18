package ch.bemar.dhcp.net.arp;

import java.io.IOException;
import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArpTableProvider {

	private static ArpTable table;

	private ArpTool tool;

	private ArpProperties props;

	public ArpTableProvider() throws IOException {
		this.tool = new ArpTool();
		props = new ArpProperties();
		refresh();
	}

	public synchronized Arp foundInArp(InetAddress address) {

		if (props.isArpActive())
			return table.hasArp(address);

		return null;
	}

	public synchronized void refresh() {
		try {

			if (props.isArpActive())
				table = tool.getTable();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
