package ch.bemar.dhcp;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;

import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.reader.ServerConfigReader;
import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.core.DHCPServer;
import ch.bemar.dhcp.exception.OptionNotFoundException;
import ch.bemar.dhcp.util.NetworkInterfaceInfo;
import ch.bemar.dhcp.util.PropertiesLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BemarDhcpServer {

	/**
	 * Launcher for the server.
	 * 
	 * <p>
	 * args[0] = path to config file
	 * 
	 * @param args
	 * @throws Exception
	 * @throws OptionNotFoundException
	 */
	public static void main(String[] args) throws Exception {

		Options options = buildOptions();

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("bemar-dhcp", options);

			System.exit(1);
			return;
		}

		Properties properties = PropertiesLoader.loadProperties(DhcpConstants.DEFAULT_PROPERTIES_FILE);
		log.info("got application properties: {}", properties);

		ServerConfigReader scr = new ServerConfigReader();
		DhcpServerConfiguration config = null;

		if (cmd.hasOption("f")) {
			String inputFilePath = cmd.getOptionValue("fileinput");

			File cfgFile = new File(inputFilePath);
			log.info("reading config from file: {}", cfgFile);
			config = scr.readConfigFromFile(cfgFile);

		} else if (cmd.hasOption("i")) {

			NetworkInterfaceInfo.printIfaceInfo();
			System.exit(0);

		} else {

			InputStream is = BemarDhcpServer.class.getResourceAsStream("/" + DhcpConstants.DEFAULT_CONFIG_FILE);

			if (is == null) {
				log.error("Could not find {} in classpath", DhcpConstants.DEFAULT_CONFIG_FILE);
				System.exit(2);
			}

			String content = IOUtils.toString(is, StandardCharsets.UTF_8);
			log.info("reading config from classpath file");
			config = scr.readConfigFromString(content);

		}

		DHCPServer server = new DHCPServer(config);

		Thread mainThread = new Thread(server, "bemar-dhcp");
		mainThread.start();

		mainThread.join();

		System.exit(0);
	}

	private static Options buildOptions() {
		Options options = new Options();

		Option fInput = new Option("f", "fileinput", true, "Config file path");
		fInput.setRequired(false);
		options.addOption(fInput);

		Option info = new Option("i", "info", false, "Interface info");
		info.setRequired(false);
		options.addOption(info);

		return options;
	}

}
