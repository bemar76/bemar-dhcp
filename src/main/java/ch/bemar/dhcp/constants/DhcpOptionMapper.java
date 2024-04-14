package ch.bemar.dhcp.constants;

import java.util.HashMap;
import java.util.Map;

import org.dhcp4java.DHCPConstants;

import com.google.common.base.Strings;

import ch.bemar.dhcp.exception.OptionNotFoundException;
import ch.bemar.dhcp.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DhcpOptionMapper {

	private static final Map<String, Byte> optionToByteMap = new HashMap<>();

	static {

		optionToByteMap.put("subnet-mask", DHCPConstants.DHO_SUBNET_MASK);
		optionToByteMap.put("time-offset", DHCPConstants.DHO_TIME_OFFSET);
		optionToByteMap.put("routers", DHCPConstants.DHO_ROUTERS);
		optionToByteMap.put("time-server", DHCPConstants.DHO_TIME_SERVERS);
		optionToByteMap.put("name-server", DHCPConstants.DHO_NAME_SERVERS);
		optionToByteMap.put("domain-name-server", DHCPConstants.DHO_DOMAIN_NAME_SERVERS);
		optionToByteMap.put("domain-name-servers", DHCPConstants.DHO_DOMAIN_NAME_SERVERS);
		optionToByteMap.put("log-server", DHCPConstants.DHO_LOG_SERVERS);
		optionToByteMap.put("cookie-server", DHCPConstants.DHO_COOKIE_SERVERS);
		optionToByteMap.put("lpr-server", DHCPConstants.DHO_LPR_SERVERS);
		optionToByteMap.put("impress-server", DHCPConstants.DHO_IMPRESS_SERVERS);
		optionToByteMap.put("resource-location-server", DHCPConstants.DHO_RESOURCE_LOCATION_SERVERS);
		optionToByteMap.put("host-name", DHCPConstants.DHO_HOST_NAME);
		optionToByteMap.put("boot-file-size", DHCPConstants.DHO_BOOT_SIZE);
		optionToByteMap.put("merit-dump-file", DHCPConstants.DHO_MERIT_DUMP);
		optionToByteMap.put("domain-name", DHCPConstants.DHO_DOMAIN_NAME);
		optionToByteMap.put("swap-server", DHCPConstants.DHO_SWAP_SERVER);
		optionToByteMap.put("root-path", DHCPConstants.DHO_ROOT_PATH);
		optionToByteMap.put("extensions-path", DHCPConstants.DHO_EXTENSIONS_PATH);
		optionToByteMap.put("ip-forwarding", DHCPConstants.DHO_IP_FORWARDING);
		optionToByteMap.put("non-local-source-routing", DHCPConstants.DHO_NON_LOCAL_SOURCE_ROUTING);
		optionToByteMap.put("policy-filter", DHCPConstants.DHO_POLICY_FILTER);
		optionToByteMap.put("max-dgram-reassembly", DHCPConstants.DHO_MAX_DGRAM_REASSEMBLY);
		optionToByteMap.put("default-ttl", DHCPConstants.DHO_DEFAULT_IP_TTL);
		optionToByteMap.put("mtu", DHCPConstants.DHO_INTERFACE_MTU);
		optionToByteMap.put("all-subnets-local", DHCPConstants.DHO_ALL_SUBNETS_LOCAL);
		optionToByteMap.put("broadcast-address", DHCPConstants.DHO_BROADCAST_ADDRESS);
		optionToByteMap.put("perform-mask-discovery", DHCPConstants.DHO_PERFORM_MASK_DISCOVERY);
		optionToByteMap.put("mask-supplier", DHCPConstants.DHO_MASK_SUPPLIER);
		optionToByteMap.put("router-discovery", DHCPConstants.DHO_ROUTER_DISCOVERY);
		optionToByteMap.put("router-solicitation-address", DHCPConstants.DHO_ROUTER_SOLICITATION_ADDRESS);
		optionToByteMap.put("static-route", DHCPConstants.DHO_STATIC_ROUTES);
		optionToByteMap.put("trailer-encapsulation", DHCPConstants.DHO_TRAILER_ENCAPSULATION);
		optionToByteMap.put("arp-cache-timeout", DHCPConstants.DHO_ARP_CACHE_TIMEOUT);
		optionToByteMap.put("ieee802-3-encapsulation", DHCPConstants.DHO_IEEE802_3_ENCAPSULATION);
		optionToByteMap.put("default-tcp-ttl", DHCPConstants.DHO_DEFAULT_TCP_TTL);
		optionToByteMap.put("tcp-keepalive-interval", DHCPConstants.DHO_TCP_KEEPALIVE_INTERVAL);
		optionToByteMap.put("tcp-keepalive-garbage", DHCPConstants.DHO_TCP_KEEPALIVE_GARBAGE);
		optionToByteMap.put("nis-domain", DHCPConstants.DHO_NIS_DOMAIN);
		optionToByteMap.put("nis-servers", DHCPConstants.DHO_NIS_SERVERS);
		optionToByteMap.put("ntp-servers", DHCPConstants.DHO_NTP_SERVERS);
		optionToByteMap.put("vendor-specific", DHCPConstants.DHO_VENDOR_ENCAPSULATED_OPTIONS);
		optionToByteMap.put("netbios-name-servers", DHCPConstants.DHO_NETBIOS_NAME_SERVERS);
		optionToByteMap.put("netbios-dd-server", DHCPConstants.DHO_NETBIOS_DD_SERVER);
		optionToByteMap.put("netbios-node-type", DHCPConstants.DHO_NETBIOS_NODE_TYPE);
		optionToByteMap.put("netbios-scope", DHCPConstants.DHO_NETBIOS_SCOPE);
		optionToByteMap.put("font-servers", DHCPConstants.DHO_FONT_SERVERS);
		optionToByteMap.put("x-display-manager", DHCPConstants.DHO_X_DISPLAY_MANAGER);
		optionToByteMap.put("requested-address", DHCPConstants.DHO_DHCP_REQUESTED_ADDRESS);
		optionToByteMap.put("lease-time", DHCPConstants.DHO_DHCP_LEASE_TIME);
		optionToByteMap.put("option-overload", DHCPConstants.DHO_DHCP_OPTION_OVERLOAD);
		optionToByteMap.put("message-type", DHCPConstants.DHO_DHCP_MESSAGE_TYPE);
		optionToByteMap.put("server-identifier", DHCPConstants.DHO_DHCP_SERVER_IDENTIFIER);
		optionToByteMap.put("parameter-request-list", DHCPConstants.DHO_DHCP_PARAMETER_REQUEST_LIST);
		optionToByteMap.put("message", DHCPConstants.DHO_DHCP_MESSAGE);
		optionToByteMap.put("max-message-size", DHCPConstants.DHO_DHCP_MAX_MESSAGE_SIZE);
		optionToByteMap.put("renewal-time", DHCPConstants.DHO_DHCP_RENEWAL_TIME);
		optionToByteMap.put("rebinding-time", DHCPConstants.DHO_DHCP_REBINDING_TIME);
		optionToByteMap.put("vendor-class-identifier", DHCPConstants.DHO_VENDOR_CLASS_IDENTIFIER);
		optionToByteMap.put("client-identifier", DHCPConstants.DHO_DHCP_CLIENT_IDENTIFIER);
		optionToByteMap.put("nwip-domain", DHCPConstants.DHO_NWIP_DOMAIN_NAME);
		optionToByteMap.put("nwip-suboptions", DHCPConstants.DHO_NWIP_SUBOPTIONS);
		optionToByteMap.put("nisplus-domain", DHCPConstants.DHO_NISPLUS_DOMAIN);
		optionToByteMap.put("nisplus-servers", DHCPConstants.DHO_NISPLUS_SERVER);
		optionToByteMap.put("tftp-server", DHCPConstants.DHO_TFTP_SERVER);
		optionToByteMap.put("bootfile", DHCPConstants.DHO_BOOTFILE);
		optionToByteMap.put("mobile-ip-home-agent", DHCPConstants.DHO_MOBILE_IP_HOME_AGENT);
		optionToByteMap.put("smtp-server", DHCPConstants.DHO_SMTP_SERVER);
		optionToByteMap.put("pop3-server", DHCPConstants.DHO_POP3_SERVER);
		optionToByteMap.put("nntp-server", DHCPConstants.DHO_NNTP_SERVER);
		optionToByteMap.put("www-server", DHCPConstants.DHO_WWW_SERVER);
		optionToByteMap.put("finger-server", DHCPConstants.DHO_FINGER_SERVER);
		optionToByteMap.put("irc-server", DHCPConstants.DHO_IRC_SERVER);
		optionToByteMap.put("streettalk-server", DHCPConstants.DHO_STREETTALK_SERVER);
		optionToByteMap.put("stda-server", DHCPConstants.DHO_STDA_SERVER);
		optionToByteMap.put("user-class", DHCPConstants.DHO_USER_CLASS);
		optionToByteMap.put("fqdn", DHCPConstants.DHO_FQDN);
		optionToByteMap.put("agent-options", DHCPConstants.DHO_DHCP_AGENT_OPTIONS);
		optionToByteMap.put("nds-servers", DHCPConstants.DHO_NDS_SERVERS);
		optionToByteMap.put("nds-tree-name", DHCPConstants.DHO_NDS_TREE_NAME);
		optionToByteMap.put("nds-context", DHCPConstants.DHO_NDS_CONTEXT);
		optionToByteMap.put("client-last-transaction-time", DHCPConstants.DHO_CLIENT_LAST_TRANSACTION_TIME);
		optionToByteMap.put("associated-ip", DHCPConstants.DHO_ASSOCIATED_IP);
		optionToByteMap.put("user-authentication-protocol", DHCPConstants.DHO_USER_AUTHENTICATION_PROTOCOL);
		optionToByteMap.put("auto-configure", DHCPConstants.DHO_AUTO_CONFIGURE);
		optionToByteMap.put("name-service-search", DHCPConstants.DHO_NAME_SERVICE_SEARCH);
		optionToByteMap.put("subnet-selection", DHCPConstants.DHO_SUBNET_SELECTION);
		optionToByteMap.put("domain-search", DHCPConstants.DHO_DOMAIN_SEARCH);
		optionToByteMap.put("classless-route", DHCPConstants.DHO_CLASSLESS_ROUTE);
		// exotic options
//		optionToByteMap.put("sip-servers-dhcp-option", (byte) 120);
//		optionToByteMap.put("classless-static-route-option", (byte) 121);
//		optionToByteMap.put("cablelabs-client-configuration", (byte) 122);
//		optionToByteMap.put("geoconf-civic", (byte) 123);
//		optionToByteMap.put("ieee-802-1q-vlan-id", (byte) 124);
//		optionToByteMap.put("ieee-802-1d/p-layer-2-priority", (byte) 125);
//		optionToByteMap.put("diffserv-code-point", (byte) 133);
//		optionToByteMap.put("http-proxy-for-phone-specific-applications", (byte) 135);
//		optionToByteMap.put("pxe-uefi-http-boot", (byte) 175);
//		optionToByteMap.put("tftp-server-address", (byte) 150);
//		optionToByteMap.put("etherboot", (byte) 175);
//		optionToByteMap.put("ip-telephone", (byte) 176);
//		optionToByteMap.put("packetcable-and-cablehome", (byte) 177);
//		optionToByteMap.put("vendor-identifying-vendor-class", (byte) 124);
//		optionToByteMap.put("vendor-identifying-vendor-specific-information", (byte) 125);

	}

	public static Byte getOptionByteByName(String name) throws OptionNotFoundException {
		log.debug("search option: {}", name);
		Byte b = null;

		if (!Strings.isNullOrEmpty(name)) {
			b = optionToByteMap.get(name.trim());

			if (b == null && !name.trim().toLowerCase().startsWith("dhcp-")) // maybe we can find it with "dhcp-" suffix
				b = optionToByteMap.get("dhcp-" + name.trim());

			if (b == null && name.trim().toLowerCase().startsWith("dhcp-")) // maybe we can find it without "dhcp-"
																			// suffix
				b = optionToByteMap.get(StringUtils.remove("dhcp-", name));

			if (b == null) {
				throw new OptionNotFoundException("No option type for '" + name + "' found");
			}

		} else {
			throw new OptionNotFoundException("The name of the option my not null");
		}
		return b;
	}

}
