package ch.bemar.dhcp.config.element;

import java.net.InetAddress;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpRange {

	private InetAddress start;
	private InetAddress end;
	private InetAddress subnetMask;

}
