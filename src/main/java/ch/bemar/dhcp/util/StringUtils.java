package ch.bemar.dhcp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	private StringUtils() {
	}

	public static boolean containsRegex(String text, String regex) {
		return getMatcher(text, regex).find();
	}

	private static Matcher getMatcher(String text, String regex) {

		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(text);

	}

}
