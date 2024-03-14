package ch.bemar.dhcp.util;

import java.util.Arrays;

import org.dhcp4java.DHCPOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ch.bemar.dhcp.constants.DHCPOptionFactory;

public class DHCPOptionTest {

	public static DHCPOptionFactory factory = new DHCPOptionFactory();

	@ParameterizedTest
	@CsvSource({ "option router 192.168.64.1,3,[-64; -88; 64; 1]" //
	})
	void testAllElements(String line, int b, String result) throws Exception {

		DHCPOption option = factory.getFromLine(line);

		Assertions.assertEquals((byte) b, option.getCode());

		Assertions.assertEquals(result.replace(";", ","), Arrays.toString(option.getValue()));

	}

}
