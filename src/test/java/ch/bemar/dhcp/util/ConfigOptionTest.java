package ch.bemar.dhcp.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ch.bemar.dhcp.config.reader.ConfigOption;
import ch.bemar.dhcp.exception.OptionNotFoundException;

class ConfigOptionTest {

	@Test
	void testSimpleOption() throws OptionNotFoundException {

		ConfigOption option = new ConfigOption("option domain-name \"bemar.local\";");

		assertEquals("domain-name", option.getOptionName());
		assertEquals("\"bemar.local\"", option.getOptionValue());

	}

	@Test
	void testMultivalueOption() throws OptionNotFoundException {

		ConfigOption option = new ConfigOption("option domain-name-servers 8.8.8.8, 8.8.4.4;");

		assertEquals("domain-name-servers", option.getOptionName());
		assertEquals("8.8.8.8, 8.8.4.4", option.getOptionValue());

	}

	@Test
	void testEmptyOption() {

		OptionNotFoundException thrown = assertThrows(OptionNotFoundException.class, () -> {
			new ConfigOption("");
		});

		assertEquals("Can't build an option from null or empty", thrown.getMessage());

	}

	@Test
	void testNullOption() {

		OptionNotFoundException thrown = assertThrows(OptionNotFoundException.class, () -> {
			new ConfigOption(null);
		});

		assertEquals("Can't build an option from null or empty", thrown.getMessage());

	}
	
	@Test
	void testNoSemicolonOption() {

		OptionNotFoundException thrown = assertThrows(OptionNotFoundException.class, () -> {
			new ConfigOption("option domain-name-servers 8.8.8.8, 8.8.4.4");
		});

		assertEquals("Option does not end with semicolon", thrown.getMessage());

	}
}
