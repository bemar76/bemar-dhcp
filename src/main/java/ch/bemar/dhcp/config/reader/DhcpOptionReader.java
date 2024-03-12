package ch.bemar.dhcp.config.reader;

import java.util.Set;

import org.dhcp4java.DHCPOption;

import com.google.common.base.Strings;

import ch.bemar.dhcp.constants.DhcpOptionMapper;
import ch.bemar.dhcp.convert.ValueByteBuilder;
import ch.bemar.dhcp.exception.OptionNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DhcpOptionReader extends AConfigReader{

	public static DHCPOption createDHCPOption(ConfigOption option) throws Exception {

		Byte optionType = null;

		if (!Strings.isNullOrEmpty(option.getOptionName())) {

			optionType = DhcpOptionMapper.getOptionByteByName(option.getOptionName());
			log.trace("got optionType {} from optionName {}", optionType, option.getOptionName());
		}

		if (option.getCode() != null) {
			optionType = option.getCode().byteValue();
			log.trace("got optionType {} from option number {}", optionType, option.getCode());
		}

		if (optionType != null) {

			return createDHCPOption(optionType, getValueBytes(optionType, option.getOptionValues()));

		} else {

			log.error("option byte not found by name or code");
			throw new OptionNotFoundException("The option with name '" + option.getOptionName() + "' or code '"
					+ option.getCode() + "' is unknown");
		}

	}

	public static byte[] getValueBytes(Byte optionType, Set<String> values) throws Exception {
		return ValueByteBuilder.valueSetToBytes(optionType, values);
	}

	public static DHCPOption createDHCPOption(byte code, byte[] optionValue) throws OptionNotFoundException {

		return new DHCPOption(code, optionValue);
	}

}
