package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ArpTable {

	private Map<Interface, Set<Arp>> arpTable;

	public ArpTable() {
		this.arpTable = Maps.newHashMap();
	}

	void addEntry(Interface iface, Arp arp) {

		if (!arpTable.containsKey(iface)) {
			arpTable.put(iface, Sets.newHashSet());
		}

		arpTable.get(iface).add(arp);

	}

	public Arp hasArp(Interface iface, InetAddress ip) {

		for (Arp a : arpTable.get(iface)) {

			if (a.getIp().equals(ip)) {
				return a;
			}

		}

		return null;

	}

	public Arp hasArp(InetAddress ip) {

		for (Interface iface : arpTable.keySet()) {

			Arp found = hasArp(iface, ip);

			if (found != null) {
				return found;
			}
		}

		return null;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		List<Interface> ifaces = Lists.newArrayList(arpTable.keySet());
		Collections.sort(ifaces);
		for (Interface iface : ifaces) {
			sb.append(iface).append("=");

			List<Arp> arps = Lists.newArrayList(arpTable.get(iface));
			Collections.sort(arps);

			sb.append(StringUtils.join(arps));

			sb.append("\n");
		}

		return sb.toString();

	}

}
