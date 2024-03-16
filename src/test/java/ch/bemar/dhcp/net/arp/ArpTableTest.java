package ch.bemar.dhcp.net.arp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.net.arp.ArpTable;
import ch.bemar.dhcp.net.arp.ArpTool;

public class ArpTableTest {

	@Test
	void testArpTable() throws IOException {

		String content = IOUtils.toString(this.getClass().getResourceAsStream("/windows_arp_tool_response.txt"),
				StandardCharsets.UTF_8);

		ArpTable table = new ArpTool().buildTable(content);

		System.out.println(table);

		Assertions.assertEquals(
				"ArpTable [arpTable={ch.bemar.dhcp.net.arp.Interface@78e94dcf=[Arp [ip=/172.29.255.255, mac=ff:ff:ff:ff:ff:ff, dynamic=false], Arp [ip=/224.0.0.251, mac=01:00:5e:00:00:fb, dynamic=false], Arp [ip=/239.255.255.250, mac=01:00:5e:7f:ff:fa, dynamic=false], Arp [ip=/224.0.0.22, mac=01:00:5e:00:00:16, dynamic=false]], ch.bemar.dhcp.net.arp.Interface@5bfa9431=[Arp [ip=/172.21.111.255, mac=ff:ff:ff:ff:ff:ff, dynamic=false], Arp [ip=/239.255.255.250, mac=01:00:5e:7f:ff:fa, dynamic=false], Arp [ip=/224.0.0.251, mac=01:00:5e:00:00:fb, dynamic=false], Arp [ip=/224.0.0.22, mac=01:00:5e:00:00:16, dynamic=false]], ch.bemar.dhcp.net.arp.Interface@2133814f=[Arp [ip=/192.168.64.73, mac=f6:ff:ba:de:01:65, dynamic=true], Arp [ip=/239.255.255.250, mac=01:00:5e:7f:ff:fa, dynamic=false], Arp [ip=/192.168.64.210, mac=80:3f:5d:f3:41:73, dynamic=true], Arp [ip=/192.168.64.62, mac=30:4a:26:17:b8:26, dynamic=true], Arp [ip=/192.168.64.1, mac=04:b4:fe:c2:81:be, dynamic=true], Arp [ip=/192.168.64.84, mac=3c:7a:aa:6c:5d:18, dynamic=true], Arp [ip=/192.168.64.255, mac=ff:ff:ff:ff:ff:ff, dynamic=false], Arp [ip=/192.168.64.49, mac=56:e6:36:44:83:70, dynamic=true], Arp [ip=/192.168.64.83, mac=50:56:bf:0d:5f:50, dynamic=true], Arp [ip=/224.0.0.252, mac=01:00:5e:00:00:fc, dynamic=false], Arp [ip=/192.168.64.47, mac=50:1e:2d:32:78:62, dynamic=true], Arp [ip=/192.168.64.107, mac=00:06:78:4a:29:c8, dynamic=true], Arp [ip=/255.255.255.255, mac=ff:ff:ff:ff:ff:ff, dynamic=false], Arp [ip=/192.168.64.72, mac=f8:f5:32:33:79:04, dynamic=true], Arp [ip=/224.0.0.251, mac=01:00:5e:00:00:fb, dynamic=false], Arp [ip=/224.0.0.22, mac=01:00:5e:00:00:16, dynamic=false], Arp [ip=/192.168.64.87, mac=ec:b5:fa:1c:36:29, dynamic=true], Arp [ip=/192.168.64.92, mac=00:22:64:bb:7c:3f, dynamic=true]]}]",
				table.toString());

	}

}
