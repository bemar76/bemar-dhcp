package ch.bemar.dhcp.util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.dhcp4java.DHCPOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ch.bemar.dhcp.constants.DHCPOptionFactory;

public class DHCPOptionTest {

	public static DHCPOptionFactory factory = new DHCPOptionFactory();

	@ParameterizedTest
	@CsvSource({ "option router 192.168.64.1,3,[-64; -88; 64; 1]", //
			"option domain-name \"bemar.local\",15,[34; 98; 101; 109; 97; 114; 46; 108; 111; 99; 97; 108; 34]", //
			"option domain-name-servers 8.8.8.8; 8.8.4.4,6,[8; 8; 8; 8; 8; 8; 4; 4]", //
			"option domain-name \"example.org\",15,[34; 101; 120; 97; 109; 112; 108; 101; 46; 111; 114; 103; 34]", //
			"option subnet-mask 255.255.255.0,1,[-1; -1; -1; 0]", //
			"option broadcast-address 10.0.0.255,28,[10; 0; 0; -1]"})
	void testAllElements(String line, int b, String result) throws Exception {

		line = StringUtils.replace(line, ";", ",");

		DHCPOption option = factory.getFromLine(line);

		Assertions.assertEquals((byte) b, option.getCode());

		Assertions.assertEquals(result.replace(";", ","), Arrays.toString(option.getValue()));

	}

}
