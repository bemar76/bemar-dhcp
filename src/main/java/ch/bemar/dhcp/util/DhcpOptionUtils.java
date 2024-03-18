package ch.bemar.dhcp.util;

import java.net.InetAddress;
import java.util.Collection;

import org.dhcp4java.DHCPOption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DhcpOptionUtils {

	private DhcpOptionUtils() {
	}

	public static DHCPOption findOption(Collection<DHCPOption> options, byte b) {
		for (DHCPOption option : options) {

			if (option.getCode() == b) {
				return option;
			}

		}
		log.error("no option found with byte {}", b);
		return null;
	}

	public static DHCPOption findOptionOrDefault(Collection<DHCPOption> options, byte b, InetAddress address) {

		DHCPOption option = findOption(options, b);
		if (option == null) {
			option = DHCPOption.newOptionAsInetAddress(b, address);
		}

		return option;
	}
	
	public static DHCPOption findOptionOrDefault(Collection<DHCPOption> options, byte b, int value) {

		DHCPOption option = findOption(options, b);
		if (option == null) {
			option = DHCPOption.newOptionAsInt(b, value);
		}

		return option;
	}
	
	public static DHCPOption findOptionOrDefault(Collection<DHCPOption> options, byte b, short value) {

		DHCPOption option = findOption(options, b);
		if (option == null) {
			option = DHCPOption.newOptionAsShort(b, value);
		}

		return option;
	}
	
	

	public static boolean hasOption(Collection<DHCPOption> options, byte b) {
		return findOption(options, b) != null;
	}
}
