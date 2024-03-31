package ch.bemar.dhcp.dns;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Properties;

import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.Type;
import org.xbill.DNS.Update;

import ch.bemar.dhcp.constants.DnsConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DnsUpdateManager {

	private final Properties dnsProperties;

	public DnsUpdateManager(Properties dnsProperties) {
		this.dnsProperties = dnsProperties;
	}

	public Message updateDnsRecord(InetAddress ip, String dnsName, String zoneName, int ttl) throws IOException {

		if (dnsProperties != null //
				&& !dnsProperties.isEmpty() //
				&& dnsProperties.containsKey(DnsConstants.PROP_DNS_UPDATES_ENABLED) //
				&& dnsProperties.getProperty(DnsConstants.PROP_DNS_UPDATES_ENABLED).trim().equalsIgnoreCase("true")) {
			
			return doDnsUpdateRecord(ip, dnsName, zoneName, ttl);
		} else {
			log.info("dns updates not enabled");
		}

		return null;

	}

	private Message doDnsUpdateRecord(InetAddress ip, String dnsName, String zoneName, int ttl) throws IOException {
		try {
			Name zone = Name.fromString(zoneName);
			Name host = Name.fromString(dnsName, zone);
			Update update = new Update(zone);

			update.add(host, Type.A, ttl, ip.getHostAddress());

			TSIG tsig = new TSIG(dnsProperties.getProperty(DnsConstants.PROP_ALGORITHM),
					dnsProperties.getProperty(DnsConstants.PROP_KEYNAME),
					dnsProperties.getProperty(DnsConstants.PROP_SECRET));
			update.setTSIG(tsig);

			// Den Update-Request senden
			SimpleResolver resolver = new SimpleResolver(dnsProperties.getProperty(DnsConstants.PROP_DNSSERVER));
			resolver.setTSIGKey(tsig);
			resolver.setPort(Integer.valueOf(dnsProperties.getProperty(DnsConstants.PROP_DNSPORT)));
			resolver.setTimeout(Duration
					.ofSeconds(Long.valueOf(dnsProperties.getOrDefault(DnsConstants.PROP_TIMEOUT, "60").toString())));
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

}
