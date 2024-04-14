package ch.bemar.dhcp.config.reader;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.exception.OptionNotFoundException;
import ch.bemar.dhcp.util.StringUtils;
import lombok.Data;

@Data
public class ConfigOption {

	String optionName;
	Integer code;
	Set<String> optionValues;
	String line;

	public ConfigOption(String line) throws OptionNotFoundException {

		if (Strings.isNullOrEmpty(line)) {
			throw new OptionNotFoundException("Can't build an option from null or empty");
		}

		if (!line.trim().endsWith(DhcpConstants.SEMICOLON)) {
			throw new OptionNotFoundException("Option does not end with semicolon");
		}

		this.line = line;

		String[] tokens = getSplittedLine();

		if (tokens.length != 3) {
			throw new OptionNotFoundException("Option length smaller than 3");
		}

		optionName = tokens[1];
		optionValues = splitAndTrimValues(tokens[2]);

	}

	public Integer getCode() {
		return this.code;
	}

	public Set<String> splitAndTrimValues(String value) {

		value = StringUtils.remove(value, DhcpConstants.SEMICOLON).trim();

		String[] tokens = StringUtils.split(value, ",");

		return Sets.newHashSet(tokens).stream().map(String::trim).collect(Collectors.toSet());

	}

	public String[] getSplittedLine() {

		return StringUtils.split(line, " ", 3);

	}

}
