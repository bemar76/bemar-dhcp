package ch.bemar.dhcp.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.util.StringUtils;

public class StringUtilsTest {

	@Test
	void testContains() {

		String message = "Tabelle \"DBLEASE\" nicht gefunden (diese Datenbank ist leer)\r\n"
				+ "Table \"DBLEASE\" not found (this database is empty); SQL statement:\r\n"
				+ "SELECT * FROM DbLease [42104-224]";

		Assertions.assertTrue(StringUtils.containsRegex(message, "Table .* not found"));

	}

}
