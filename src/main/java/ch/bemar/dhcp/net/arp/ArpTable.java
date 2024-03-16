package ch.bemar.dhcp.net.arp;

import java.net.InetAddress;
import java.util.Map;
import java.util.Set;

import org.dhcp4java.HardwareAddress;

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
		return "ArpTable [arpTable=" + arpTable + "]";
	}

}
