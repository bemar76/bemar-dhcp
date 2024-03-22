package ch.bemar.dhcp.net.arp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArpTableTest {

	@Test
	void testArpTable() throws IOException {

		String content = IOUtils.toString(this.getClass().getResourceAsStream("/windows_arp_tool_verbose_response.txt"),
				StandardCharsets.UTF_8);

		ArpTable table = new ArpTool().buildTable(content);

		System.out.println(table);

		Assertions.assertEquals(
				"Interface [address=/127.0.0.1, name=0x1]=[ArpEntry [ip=/224.0.0.22, mac=00:00:00:00:00:00, type=FIXED], ArpEntry [ip=/224.0.0.251, mac=00:00:00:00:00:00, type=FIXED], ArpEntry [ip=/239.255.255.250, mac=00:00:00:00:00:00, type=FIXED]]\n"
				+ "Interface [address=/172.27.208.1, name=0x22]=[ArpEntry [ip=/172.27.223.255, mac=ff:ff:ff:ff:ff:ff, type=FIXED], ArpEntry [ip=/224.0.0.22, mac=01:00:5e:00:00:16, type=FIXED], ArpEntry [ip=/224.0.0.251, mac=01:00:5e:00:00:fb, type=FIXED], ArpEntry [ip=/239.255.255.250, mac=01:00:5e:7f:ff:fa, type=FIXED]]\n"
				+ "Interface [address=/172.29.240.1, name=0x29]=[ArpEntry [ip=/172.29.245.190, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/172.29.255.255, mac=ff:ff:ff:ff:ff:ff, type=FIXED], ArpEntry [ip=/224.0.0.22, mac=01:00:5e:00:00:16, type=FIXED], ArpEntry [ip=/224.0.0.251, mac=01:00:5e:00:00:fb, type=FIXED], ArpEntry [ip=/239.255.255.250, mac=01:00:5e:7f:ff:fa, type=FIXED]]\n"
				+ "Interface [address=/192.168.64.61, name=0x14]=[ArpEntry [ip=/192.168.64.1, mac=04:b4:fe:c2:81:be, type=DYNAMIC], ArpEntry [ip=/192.168.64.107, mac=00:06:78:4a:29:c8, type=DYNAMIC], ArpEntry [ip=/192.168.64.131, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.2, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.20, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.210, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.255, mac=ff:ff:ff:ff:ff:ff, type=FIXED], ArpEntry [ip=/192.168.64.27, mac=50:1e:2d:32:78:62, type=DYNAMIC], ArpEntry [ip=/192.168.64.29, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.41, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.43, mac=56:e6:36:44:83:70, type=DYNAMIC], ArpEntry [ip=/192.168.64.44, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.47, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.49, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.55, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.59, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.60, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.62, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.70, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.72, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.73, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.79, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.81, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.83, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.87, mac=ec:b5:fa:1c:36:29, type=DYNAMIC], ArpEntry [ip=/192.168.64.91, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.96, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/192.168.64.97, mac=00:00:00:00:00:00, type=INVALID], ArpEntry [ip=/224.0.0.22, mac=01:00:5e:00:00:16, type=FIXED], ArpEntry [ip=/224.0.0.251, mac=01:00:5e:00:00:fb, type=FIXED], ArpEntry [ip=/224.0.0.252, mac=01:00:5e:00:00:fc, type=FIXED], ArpEntry [ip=/239.255.255.250, mac=01:00:5e:7f:ff:fa, type=FIXED], ArpEntry [ip=/255.255.255.255, mac=ff:ff:ff:ff:ff:ff, type=FIXED]]\n"
				,
				table.toString());

	}

}
