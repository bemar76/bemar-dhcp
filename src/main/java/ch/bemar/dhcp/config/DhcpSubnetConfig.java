package ch.bemar.dhcp.config;

import java.net.InetAddress;
import java.util.Set;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPOption;

import com.google.common.collect.Sets;

import ch.bemar.dhcp.config.element.IpRange;
import ch.bemar.dhcp.config.element.Netmask;
import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.util.IPAddressInRange;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DhcpSubnetConfig extends BaseConfiguration {

	public DhcpSubnetConfig() {
		this.hosts = Sets.newHashSet();
	}

	private Subnet subnetAddress;

	private Netmask netmask;

	private IpRange range;

	private Set<DhcpHostConfig> hosts;

	public String toString() {
		return machMalString(this);
	}

	public String getDomainName() {
		for (DHCPOption option : super.getOptions()) {

			if (option.getCode() == DHCPConstants.DHO_DOMAIN_NAME) {
				return option.getValueAsString();
			}

		}

		log.error("no domain name was found in options");
		return null;
	}

	public boolean isIpAddressInSubnet(InetAddress ipToCheck) throws Exception {

		return IPAddressInRange.isIpAddressInRange(ipToCheck, subnetAddress.getValue());
	}

}
