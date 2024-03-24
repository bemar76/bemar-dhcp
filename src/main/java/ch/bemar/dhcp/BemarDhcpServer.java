package ch.bemar.dhcp;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.reader.ServerConfigReader;
import ch.bemar.dhcp.core.DHCPServer;
import ch.bemar.dhcp.env.EnvConstants;
import ch.bemar.dhcp.env.EnvironmentManager;
import ch.bemar.dhcp.util.NetworkInterfaceInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BemarDhcpServer {

	/**
	 * Launcher for the server.
	 * 
	 * <p>
	 * args[0] = configured options in {@link OptionConstant}
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		EnvironmentManager.getInstance();

		ArgumentOptions.readArguments(args);

		if (ArgumentOptions.hasOption(OptionConstant.PROD_MODE)) {
			LogConfiguration.load("classpath:logback-prod.xml");
		}

		if (ArgumentOptions.hasOption(OptionConstant.SIMULATION)) {
			log.warn("############################# SIMULATION MODE ######################################");
		}

		ShutdownHook.installShutdownHook();

		ServerConfigReader scr = new ServerConfigReader();
		DhcpServerConfiguration config = null;

		if (ArgumentOptions.hasOption(OptionConstant.FILEINPUT)) {
			String inputFilePath = ArgumentOptions.getOptionValue(OptionConstant.FILEINPUT);

			File cfgFile = new File(inputFilePath);
			log.info("reading config from file: {}", cfgFile);
			config = scr.readConfigFromFile(cfgFile);

		} else if (ArgumentOptions.hasOption(OptionConstant.IFACEINFO)) {

			NetworkInterfaceInfo.printIfaceInfo();
			System.exit(0);

		} else {

			InputStream is = BemarDhcpServer.class.getResourceAsStream("/" + EnvConstants.DEFAULT_CONFIG_FILE);

			if (is == null) {
				log.error("Could not find {} in classpath", EnvConstants.DEFAULT_CONFIG_FILE);
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

}
