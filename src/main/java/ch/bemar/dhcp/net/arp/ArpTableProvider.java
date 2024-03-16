package ch.bemar.dhcp.net.arp;

import java.io.IOException;
import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArpTableProvider {

	private static ArpTable table;

	private ArpTool tool;

	public ArpTableProvider() throws IOException {
		this.tool = new ArpTool();
		refresh();
	}

	public synchronized Arp foundInArp(InetAddress address) {

		return table.hasArp(address);

	}

	public synchronized void refresh() {
		try {

			table = tool.getTable();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
