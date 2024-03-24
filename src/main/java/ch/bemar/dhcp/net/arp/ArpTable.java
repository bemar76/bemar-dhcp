package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArpTable {

	private Map<Interface, Set<ArpEntry>> arpTable;

	public ArpTable() {
		this.arpTable = Maps.newHashMap();
	}

	void addEntry(ArpEntry arp) {

		if (!arpTable.containsKey(arp.getIface())) {
			arpTable.put(arp.getIface(), Sets.newHashSet());
		}

		arpTable.get(arp.getIface()).add(arp);

	}

	public boolean hasEntry(InetAddress ip) {

		return search(ip) != null;

	}

	public ArpEntry search(InetAddress ip) {

		for (Interface iface : arpTable.keySet()) {

			for (ArpEntry a : arpTable.get(iface)) {

				if (a.getIp().equals(ip)) {
					log.debug("found arp entry {} for address {}", a, ip);
					return a;
				}

			}

		}
		log.debug("no arp entry found for {}", ip);
		return null;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		List<Interface> ifaces = Lists.newArrayList(arpTable.keySet());
		Collections.sort(ifaces);
		for (Interface iface : ifaces) {
			sb.append(iface).append("=");

			List<ArpEntry> arps = Lists.newArrayList(arpTable.get(iface));
			Collections.sort(arps);

			sb.append(StringUtils.join(arps));

			sb.append("\n");
		}

		return sb.toString();

	}

}
