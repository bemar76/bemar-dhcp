package ch.bemar.dhcp;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArgumentOptions {

	private ArgumentOptions() {
	}

	static {
		parser = new DefaultParser();
		formatter = new HelpFormatter();
	}

	private static CommandLineParser parser;
	private static HelpFormatter formatter;
	private static CommandLine cmd;
	private static Options options;

	public static void readArguments(String[] args) {

		try {
			cmd = parser.parse(buildOptions(), args);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
			formatter.printHelp("bemar-dhcp", buildOptions());

			System.exit(1);
			return;
		}

	}

	public static Options buildOptions() {

		if (options == null) {

			options = new Options();

			for (OptionConstant oc : OptionConstant.values()) {

				Option o = new Option(oc.getShortOpt(), oc.getLongOpt(), oc.isExpectArgument(), oc.getDescribtion());
				o.setRequired(false);
				options.addOption(o);

			}

		}

		return options;
	}

	public static boolean hasOption(String opt) {
		return cmd.hasOption(opt);
	}

	public static String getOptionValue(String opt) {
		return cmd.getOptionValue(opt);
	}

}
