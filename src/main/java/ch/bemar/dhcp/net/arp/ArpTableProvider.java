package ch.bemar.dhcp.net.arp;

import java.io.IOException;
import java.net.InetAddress;

import ch.bemar.dhcp.env.EnvironmentManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArpTableProvider {

	private static ArpTable table;

	private ArpTool tool;

	public ArpTableProvider() throws IOException {
		this.tool = new ArpTool();
		refresh();
	}
	
	ArpTableProvider(ArpTable tbl) throws IOException {
		this.tool = new ArpTool();
		table = tbl;
	}

	public synchronized ArpEntry searchInArpTable(InetAddress address) {

		if (EnvironmentManager.getInstance().getEnvAsBoolean(ArpPropertiesConstants.COL_ARP_ACTIVE))
			return table.search(address);

		return null;
	}

	public synchronized void refresh() {
		try {

			if (EnvironmentManager.getInstance().getEnvAsBoolean(ArpPropertiesConstants.COL_ARP_ACTIVE))
				table = tool.buildTable();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
