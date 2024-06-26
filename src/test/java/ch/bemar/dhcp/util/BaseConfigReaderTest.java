package ch.bemar.dhcp.util;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.config.DhcpHostConfig;
import ch.bemar.dhcp.config.DhcpKeyConfig;
import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.DhcpZoneConfig;
import ch.bemar.dhcp.config.reader.ServerConfigReader;
import ch.bemar.dhcp.exception.OptionNotFoundException;

public class BaseConfigReaderTest {

	@Test
	void testServerConfigRead() throws OptionNotFoundException, Exception {

		String content = IOUtils.toString(this.getClass().getResourceAsStream("/base_dhcpd.conf"),
				StandardCharsets.UTF_8);

		ServerConfigReader scr = new ServerConfigReader();

		DhcpServerConfiguration config = scr.readConfigFromString(content);

		System.out.println(config);

		Assertions.assertEquals("[DHO_DOMAIN_NAME_SERVERS(6)=8.8.8.8 8.8.4.4 , DHO_DOMAIN_NAME(15)=\"bemar.local\"]",
				config.getOptions().toString());

		Assertions.assertEquals("max-lease-time = 3600", config.getMaxLeaseTime().toString());

		Assertions.assertEquals("default-lease-time = 300", config.getDefaultLeaseTime().toString());

		Assertions.assertEquals(1, config.getSubnets().size());

		DhcpSubnetConfig subnet = config.getSubnets().iterator().next();

		Assertions.assertEquals("IpRange(start=/192.168.64.100, end=/192.168.64.200)", subnet.getRange().toString());

		Assertions.assertEquals(
				"[DHO_DOMAIN_NAME_SERVERS(6)=192.168.64.5 8.8.8.8 8.8.4.4 , DHO_DOMAIN_NAME_SERVERS(6)=8.8.8.8 8.8.4.4 , DHO_DOMAIN_NAME(15)=\"bemar.local\", DHO_ROUTERS(3)=192.168.64.1 ]",
				subnet.getOptions().toString());

		Assertions.assertEquals("max-lease-time = 7200", subnet.getMaxLeaseTime().toString());

		Assertions.assertEquals("default-lease-time = 600", subnet.getDefaultLeaseTime().toString());

		Assertions.assertEquals("bemar.local.", subnet.getDdnsDomainName().getValue());

		Assertions.assertEquals(true, subnet.getDdnsUpdates().getValue());

		Assertions.assertEquals(1, subnet.getHosts().size());

		DhcpHostConfig host = subnet.getHosts().iterator().next();

		Assertions.assertEquals("static-client", host.getName().toString());

		Assertions.assertEquals("8c:ff:19:02:3e:3e", host.getHardwareAddress().toString());

		Assertions.assertEquals("fixed-address = /192.168.64.98", host.getFixedIpAddress().toString());

		Map<String, DhcpZoneConfig> zones = config.getZones();

		Assertions.assertEquals(2, zones.size());

		DhcpZoneConfig zoneCfg = zones.get("bemar.local.");

		Assertions.assertNotNull(zoneCfg);

		Assertions.assertEquals("/192.168.64.61", zoneCfg.getPrimary().getValue().toString());

		Assertions.assertEquals("dhcpupdate.bemar.local", zoneCfg.getKey().getValue());
		
		Assertions.assertEquals(54, zoneCfg.getPort().getValue());

		zoneCfg = zones.get("64.168.192.in-addr.arpa.");

		Assertions.assertNotNull(zoneCfg);

		Assertions.assertEquals("/192.168.64.61", zoneCfg.getPrimary().getValue().toString());

		Assertions.assertEquals("dhcpupdate.bemar.local", zoneCfg.getKey().getValue());
		
		Assertions.assertEquals(54, zoneCfg.getPort().getValue());

		Assertions.assertEquals(1, config.getKeys().size());

		DhcpKeyConfig keyCfg = config.getKeys().get("dhcpupdate.bemar.local");

		Assertions.assertNotNull(keyCfg);

		Assertions.assertEquals("dhcpupdate.bemar.local", keyCfg.getKey().getValue());

		Assertions.assertEquals("hmac-sha256", keyCfg.getAlgorithm().getValue());

		Assertions.assertEquals("VtdYOHRJjqM/u1kwx9F8qs/QWU7BXJBsF2kDMd8Dn+4=", keyCfg.getSecret().getValue());

	}

}
