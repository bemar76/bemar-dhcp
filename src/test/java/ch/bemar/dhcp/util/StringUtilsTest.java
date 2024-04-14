package ch.bemar.dhcp.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

	@Test
	void testSplitRespectsQuotes1() {

		String text = "Das ist \"ein schöner\" Text";

		String[] tokens = StringUtils.splitRespectsQuotes(text);

		Assertions.assertEquals("Das", tokens[0]);
		Assertions.assertEquals("ist", tokens[1]);
		Assertions.assertEquals("ein schöner", tokens[2]);
		Assertions.assertEquals("Text", tokens[3]);

	}

	@Test
	void testSplitRespectsQuotes2() {

		String text = "Das ist ein schöner Text";

		String[] tokens = StringUtils.splitRespectsQuotes(text);

		Assertions.assertEquals("Das", tokens[0]);
		Assertions.assertEquals("ist", tokens[1]);
		Assertions.assertEquals("ein", tokens[2]);
		Assertions.assertEquals("schöner", tokens[3]);
		Assertions.assertEquals("Text", tokens[4]);

	}

	@Test
	void testSplitRespectsQuotes3() {

		String text = null;

		String[] tokens = StringUtils.splitRespectsQuotes(text);

		Assertions.assertEquals(0, tokens.length);

	}
	
	@Test
	void testSplitRespectsQuotes4() {

		String text = "   ";

		String[] tokens = StringUtils.splitRespectsQuotes(text);

		Assertions.assertEquals(0, tokens.length);

	}
}
