package ch.bemar.dhcp.util;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import ch.bemar.dhcp.config.DhcpHostConfig;
import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.reader.ServerConfigReader;
import ch.bemar.dhcp.exception.OptionNotFoundException;

public class ComplexConfigReaderTest {

	@Test
	void testServerConfigRead() throws OptionNotFoundException, Exception {

		String content = IOUtils.toString(this.getClass().getResourceAsStream("/dhcpd_ibm.conf"),
				StandardCharsets.UTF_8);

		ServerConfigReader scr = new ServerConfigReader();

		DhcpServerConfiguration config = scr.readConfigFromString(content);

		System.out.println(config);

		Assertions.assertEquals(
				"[DHO_DOMAIN_NAME(15)=\"example.org\", DHO_DOMAIN_NAME_SERVERS(6)=192.168.64.1 192.168.64.1 , DHO_SUBNET_MASK(1)=255.255.255.0]",
				config.getOptions().toString());

		Assertions.assertEquals("max-lease-time = 7200", config.getMaxLeaseTime().toString());

		Assertions.assertEquals("default-lease-time = 600", config.getDefaultLeaseTime().toString());

		Assertions.assertEquals("log-facility = local7", config.getLogFacility().toString());

		Assertions.assertEquals("ddns-updates = off", config.getDdnsUpdates().toString());

		Assertions.assertEquals("ddns-update-style = none", config.getDdnsUpdateStyle().toString());

		Assertions.assertEquals("authoritative", config.getAuthoritative().toString());

		Assertions.assertEquals(1, config.getSubnets().size());

		DhcpSubnetConfig subnet = config.getSubnets().iterator().next();

		Assertions.assertEquals("IpRange(start=/10.0.0.100, end=/10.0.0.220)", subnet.getRange().toString());

		Assertions.assertEquals("[DHO_SUBNET_MASK(1)=255.255.255.0, DHO_BROADCAST_ADDRESS(28)=10.0.0.255, DHO_DOMAIN_NAME(15)=\"example.org\", DHO_DOMAIN_NAME_SERVERS(6)=192.168.64.1 192.168.64.1 ]",
				subnet.getOptions().toString());

		Assertions.assertEquals("max-lease-time = 6000", subnet.getMaxLeaseTime().toString());

		Assertions.assertEquals("default-lease-time = 6000", subnet.getDefaultLeaseTime().toString());

		Assertions.assertEquals(4, subnet.getHosts().size());

		List<DhcpHostConfig> hosts = Lists.newArrayList(subnet.getHosts());

		Collections.sort(hosts, new Comparator<DhcpHostConfig>() {

			@Override
			public int compare(DhcpHostConfig o1, DhcpHostConfig o2) {
				// TODO Auto-generated method stub
				return o1.getName().compareTo(o2.getName());
			}

		});

		Assertions.assertEquals("[ibmpseries\n"
				+ "00:09:6b:ab:0e:f2\n"
				+ "fixed-address = /10.0.0.141\n"
				+ "next-server = /10.0.0.20\n"
				+ "Logger[ch.bemar.dhcp.config.BaseConfiguration]\n"
				+ ", ibmx3655\n"
				+ "00:14:5e:5a:31:57\n"
				+ "fixed-address = /10.0.0.200\n"
				+ "Logger[ch.bemar.dhcp.config.BaseConfiguration]\n"
				+ "[DHO_SUBNET_MASK(1)=255.255.255.0, DHO_DOMAIN_NAME_SERVERS(6)=10.0.0.20 , DHO_DOMAIN_NAME(15)=\"site\", DHO_VENDOR_CLASS_IDENTIFIER(60)=\"PXEClient\"]\n"
				+ ", sunTarget1\n"
				+ "00:03:ba:92:92:f0\n"
				+ "fixed-address = /10.0.0.142\n"
				+ "next-server = /10.0.0.20\n"
				+ "Logger[ch.bemar.dhcp.config.BaseConfiguration]\n"
				+ "[DHO_ROUTERS(3)=10.0.0.15 ]\n"
				+ ", x41\n"
				+ "00:0a:e4:2f:66:38\n"
				+ "fixed-address = /10.0.0.201\n"
				+ "Logger[ch.bemar.dhcp.config.BaseConfiguration]\n"
				+ "[DHO_SUBNET_MASK(1)=255.255.255.0, DHO_DOMAIN_NAME_SERVERS(6)=10.0.0.20 , DHO_DOMAIN_NAME(15)=\"site\"]\n"
				+ "]", hosts.toString());

	}

}
