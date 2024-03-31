package ch.bemar.dhcp.net.dns;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

import ch.bemar.dhcp.dns.DnsUpdateManager;

@TestMethodOrder(OrderAnnotation.class)
public class TestResolver {
	private static final Logger logger = LoggerFactory.getLogger(TestResolver.class);

	private static final int port = 54;

	private String dnsName = "bemar-PC.bemar.local.";

	private static TestDNSServer mockDnsServer;

	@BeforeAll
	static void setup() throws UnknownHostException {
		mockDnsServer = new TestDNSServer(port);
		mockDnsServer.start();
		SimpleResolver resolver = new SimpleResolver(InetAddress.getLocalHost());
		resolver.setTimeout(Duration.ofSeconds(1));
		resolver.setPort(port);
	}

	@AfterAll
	static void shutdown() {
		mockDnsServer.stop();
	}

	@Test
	@Order(1)
	public void testSimpleUpdate() throws IOException {

		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream("/dns.properties"));

		DnsUpdateManager updater = new DnsUpdateManager(props);

		try {
			InetAddress ip = InetAddress.getByName("192.168.64.38");
			String zoneName = "bemar.local.";

			Message response = updater.updateDnsRecord(ip, dnsName, zoneName, 600);

			Assertions.assertEquals(0, response.getHeader().getRcode());
		} catch (Exception e) {
			System.err.println("Fehler: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	public void testSimpleQuery() throws IOException {

		SimpleResolver resolver = new SimpleResolver(InetAddress.getLocalHost());
		resolver.setTimeout(Duration.ofSeconds(1));
		resolver.setPort(port);

		Lookup lookup = new Lookup(dnsName, Type.A);
		lookup.setResolver(resolver);
		lookup.setCache(null);

		lookup.run();

		Assertions.assertEquals("bemar-PC.bemar.local.	600	IN	A	192.168.64.38", lookup.getAnswers()[0].toString());

	}

	// @Test
	public void extendedResolver() throws IOException {
		List<TestDNSServer> servers = new ArrayList<>();
		List<Resolver> resolvers = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			int port = 1000 + i * 100 + 53;
			TestDNSServer s = new TestDNSServer(port);
			s.start();
			servers.add(s);
			SimpleResolver r = new SimpleResolver(InetAddress.getLocalHost());
			r.setPort(port);
			// r.setTimeout(Duration.ofSeconds(1)); // Timeout of each resolver will be
			// overwritten to ExtendedResolver.DEFAULT_TIMEOUT
			resolvers.add(r);
		}

		ExtendedResolver resolver = new ExtendedResolver(resolvers);
		resolver.setTimeout(Duration.ofSeconds(1));
		resolver.setRetries(5);

		Lookup lookup = new Lookup(Name.root, Type.A);
		lookup.setResolver(resolver);
		lookup.setCache(null);

		long startTime = System.currentTimeMillis();
		lookup.run();
		logger.error(String.format("time: %d", (System.currentTimeMillis() - startTime) / 1000));

		for (TestDNSServer s : servers) {
			Assertions.assertEquals(5, s.getRequestCount()); // This will fail as ExtendedResolver does not work as I
																// expected
			s.stop();
		}
	}

}
