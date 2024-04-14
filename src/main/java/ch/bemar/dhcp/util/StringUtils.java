package ch.bemar.dhcp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

import ch.bemar.dhcp.net.arp.ArpEntry;

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

	/**
	 * splits a string into separate tokens by space separator. Respects words
	 * inside quotes as one string leading and ending quotes will be removed
	 * 
	 * @param input
	 * @return
	 */
	public static String[] splitRespectsQuotes(String input) {

		if (!Strings.isNullOrEmpty(input)) {
			
			List<String> tokens = new ArrayList<>();
			Pattern pattern = Pattern.compile("\"([^\"]*)\"|([^\\s\"]+)");
			Matcher matcher = pattern.matcher(input);

			while (matcher.find()) {
				if (matcher.group(1) != null) {
					tokens.add(removeLeadingAndEndingQuotes(matcher.group(1)));
				} else {
					tokens.add(removeLeadingAndEndingQuotes(matcher.group(2)));
				}
			}

			return tokens.toArray(new String[0]);
		}
		
		return new String[0];
	}

	public static String removeLeadingAndEndingQuotes(String value) {

		if (value != null && value.trim().startsWith("\"") && value.trim().endsWith("\"")) {

			value = org.apache.commons.lang3.StringUtils.substringAfter(value, "\"");
			value = org.apache.commons.lang3.StringUtils.substringBeforeLast(value, "\"");
			value.trim();

		}

		return value;

	}

	public static String substringBeforeLast(String line, String separator) {
		return org.apache.commons.lang3.StringUtils.substringBeforeLast(line, separator);
	}

	public static int countMatches(String line, String pattern) {
		return org.apache.commons.lang3.StringUtils.countMatches(line, pattern);
	}

	public static String[] split(String line, String separator) {
		return org.apache.commons.lang3.StringUtils.split(line, separator);
	}

	public static String substringBefore(String line, String separator) {
		return org.apache.commons.lang3.StringUtils.substringBefore(line, separator);
	}

	public static String remove(String line, String pattern) {
		return org.apache.commons.lang3.StringUtils.substringBefore(line, pattern);
	}

	public static String[] split(String line, String separator, int count) {
		return org.apache.commons.lang3.StringUtils.split(line, separator, count);
	}

	public static String[] split(String line) {
		return org.apache.commons.lang3.StringUtils.split(line, " ");
	}

	public static String substringAfter(String line, String separator) {
		return org.apache.commons.lang3.StringUtils.substringAfter(line, separator);
	}

	public static Object join(List objs) {
		return org.apache.commons.lang3.StringUtils.join(objs);
	}

	public static String replace(String line, String pattern, String replace) {
		return org.apache.commons.lang3.StringUtils.replace(line, pattern, replace);
	}

}
