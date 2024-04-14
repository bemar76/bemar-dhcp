package ch.bemar.dhcp.dns;

import java.io.IOException;
import java.net.InetAddress;

import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.Type;
import org.xbill.DNS.Update;

import ch.bemar.dhcp.config.DhcpKeyConfig;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.DhcpZoneConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DnsUpdateManager {

	private DhcpSubnetConfig subnetConfig;

	public DnsUpdateManager(DhcpSubnetConfig subnetConfig) {
		this.subnetConfig = subnetConfig;
	}

	public Message updateDnsRecord(InetAddress ip, String hostname) throws IOException {

		if (subnetConfig.getDdnsUpdates() != null //
				&& Boolean.TRUE.equals(subnetConfig.getDdnsUpdates().getValue())) {

			return doDnsUpdateRecord(ip, hostname);
		} else {
			log.info("dns updates not enabled");
		}

		return null;

	}

	private Message doDnsUpdateRecord(InetAddress ip, String hostName) throws IOException {
		try {

			DhcpZoneConfig zoneCfg = getZone(subnetConfig.getDdnsDomainName().getValue());

			DhcpKeyConfig keyCfg = getKey(zoneCfg.getKey().getValue());

			Name zone = Name.fromString(zoneCfg.getZoneName());
			Name host = Name.fromString(hostName, zone);
			Update update = new Update(zone);

			update.add(host, Type.A, subnetConfig.getDefaultLeaseTime().getValue(), ip.getHostAddress());

			TSIG tsig = new TSIG(keyCfg.getAlgorithm().getValue(), //
					keyCfg.getKey().getValue(), //
					keyCfg.getSecret().getValue()); //
			update.setTSIG(tsig);

			// Den Update-Request senden
			SimpleResolver resolver = new SimpleResolver(zoneCfg.getPrimary().getValue());
			resolver.setTSIGKey(tsig);
			resolver.setPort(zoneCfg.getPort() != null ? zoneCfg.getPort().getValue() : 53);
//			resolver.setTimeout(Duration
//					.ofSeconds(Long.valueOf(dnsProperties.getOrDefault(DnsConstants.PROP_TIMEOUT, "60").toString())));
			Message response = resolver.send(update);

			if (response.getRcode() == Rcode.NOERROR) {
				log.info("DNS update successful.");
			} else {
				log.error("DNS update failed: " + Rcode.string(response.getRcode()));
				log.error("Answer: {}", response);
			}

			return response;
		} catch (IOException e) {
			log.error("Fehler beim DNS Update: " + e.getMessage(), e);
			throw e;
		}
	}

	public DhcpKeyConfig getKey(String zoneName) {
		if (!zoneName.trim().endsWith(".")) {
			zoneName = zoneName.trim() + ".";
		}

		return subnetConfig.getKeys().get(zoneName.trim());
	}

	public DhcpZoneConfig getZone(String zoneName) {

		if (!zoneName.trim().endsWith(".")) {
			zoneName = zoneName.trim() + ".";
		}

		return subnetConfig.getZones().get(zoneName.trim());
	}

}
