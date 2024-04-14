package ch.bemar.dhcp.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

import org.dhcp4java.DHCPConstants;
import org.dhcp4java.DHCPOption;

import com.google.common.collect.Sets;

import ch.bemar.dhcp.config.element.DdnsDomainName;
import ch.bemar.dhcp.config.element.DdnsRevDomainName;
import ch.bemar.dhcp.config.element.DdnsUpdateStyle;
import ch.bemar.dhcp.config.element.DdnsUpdates;
import ch.bemar.dhcp.config.element.IpRange;
import ch.bemar.dhcp.config.element.Netmask;
import ch.bemar.dhcp.config.element.Subnet;
import ch.bemar.dhcp.util.BroadcastAddressCalculator;
import ch.bemar.dhcp.util.DhcpOptionUtils;
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

		DHCPOption option = DhcpOptionUtils.findOption(getOptions(), DHCPConstants.DHO_DOMAIN_NAME);
		return option.getValueAsString();

	}

	public boolean isIpAddressInSubnet(InetAddress ipToCheck) throws Exception {

		return IPAddressInRange.isIpAddressInRange(ipToCheck, subnetAddress.getValue());
	}

	public InetAddress getBroadcastAddress() throws UnknownHostException {
		return BroadcastAddressCalculator.calculateBroadcastAddress(range.getStart(), netmask.getValue());
	}

}
