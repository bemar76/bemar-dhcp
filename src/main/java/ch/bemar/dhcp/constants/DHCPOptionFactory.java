package ch.bemar.dhcp.constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPOption.OptionFormat;

import com.google.common.collect.Lists;

import ch.bemar.dhcp.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DHCPOptionFactory {

	public DHCPOption getFromLine(String line) throws Exception {

		String[] tokens = StringUtils.split(line, " ", 3);

		byte option = DhcpOptionMapper.getOptionByteByName(tokens[1]);

		Class format = DHCPOption.getOptionFormat(option);

		return getOptionByCodeAndValue(option, removeQuotes(tokens[2]));
	}

	private String removeQuotes(String value) {
		if (value.contains("\"")) {
			return StringUtils.removeLeadingAndEndingQuotes(value);
		}

		return value;
	}

	private static InetAddress[] toAddressArray(String value) throws UnknownHostException {
		String[] tokens = StringUtils.split(value, ",");
		List<InetAddress> addresses = Lists.newArrayList();

		for (String token : tokens) {
			addresses.add(InetAddress.getByName(token.trim()));
		}

		return addresses.toArray(new InetAddress[0]);
	}

	private static short[] toShortArray(String value) throws UnknownHostException {
		String[] tokens = StringUtils.split(value, ",");
		short[] shorts = new short[tokens.length];

		for (int i = 0; i < tokens.length; i++) {

			shorts[i] = Short.valueOf(tokens[i].trim());
		}

		return shorts;
	}

	public static DHCPOption getOptionByCodeAndValue(byte code, String value) throws Exception {
		OptionFormat format = DHCPOption._DHO_FORMATS.get(code);
		if (format == null) {
			return null;
		}
		switch (format) {
		case INET:
			return DHCPOption.newOptionAsInetAddress(code, InetAddress.getByName(value));
		case INETS:
			return DHCPOption.newOptionAsInetAddresses(code, toAddressArray(value));
		case INT:
			return DHCPOption.newOptionAsInt(code, Integer.valueOf(value.trim()));
		case SHORT:
			return DHCPOption.newOptionAsShort(code, Short.valueOf(value.trim()));
		case SHORTS:
			return DHCPOption.newOptionAsShorts(code, toShortArray(value.trim()));
		case BYTE:
			return DHCPOption.newOptionAsByte(code, Byte.parseByte(value.trim()));
		case BYTES:
			return DHCPOption.newOptionAsString(code, value.trim());
		case STRING:
			return DHCPOption.newOptionAsString(code, value.trim());
		default:
			return null;
		}
	}
}
