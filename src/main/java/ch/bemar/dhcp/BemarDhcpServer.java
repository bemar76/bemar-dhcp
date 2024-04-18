package ch.bemar.dhcp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.reader.ServerConfigReader;
import ch.bemar.dhcp.core.DHCPServer;
import ch.bemar.dhcp.env.EnvConstants;
import ch.bemar.dhcp.env.EnvironmentManager;
import ch.bemar.dhcp.persistence.cfg.Configuration;
import ch.bemar.dhcp.persistence.cfg.XmlLoader;
import ch.bemar.dhcp.util.NetworkInterfaceInfo;
import jakarta.xml.bind.JAXBException;
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

		if (ArgumentOptions.hasOption(OptionConstant.HELP)) {

			ArgumentOptions.printHelpMenu();
			System.exit(0);

		} else if (ArgumentOptions.hasOption(OptionConstant.SIMULATION)) {

			log.warn("############################# SIMULATION MODE ######################################");

		} else if (ArgumentOptions.hasOption(OptionConstant.IFACEINFO)) {

			NetworkInterfaceInfo.printIfaceInfo();
			System.exit(0);

		}

		ShutdownHook.installShutdownHook();

		try {

			DhcpServerConfiguration config = getServerConfig();

			Configuration dbCfg = getDbConfig();

			DHCPServer server = new DHCPServer(config, dbCfg);

			Thread mainThread = new Thread(server, "bemar-dhcp");
			mainThread.start();

			mainThread.join();

			System.exit(0);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}

	}

	private static Configuration getDbConfig() throws JAXBException, IOException {
		if (ArgumentOptions.hasOption(OptionConstant.DBINPUT)) {
			String inputFilePath = ArgumentOptions.getOptionValue(OptionConstant.DBINPUT);

			return XmlLoader.loadConfiguration(new File(inputFilePath));

		} else {

			InputStream is = BemarDhcpServer.class.getResourceAsStream("/" + EnvConstants.DEFAULT_DB_CONFIG_FILE);

			if (is == null) {
				log.error("Could not find {} in classpath", EnvConstants.DEFAULT_DB_CONFIG_FILE);
				System.exit(2);
			}

			log.info("reading config from classpath file");
			return XmlLoader.loadConfiguration(is);

		}
	}

	private static DhcpServerConfiguration getServerConfig() throws Exception {

		ServerConfigReader serverConfigReader = new ServerConfigReader();

		if (ArgumentOptions.hasOption(OptionConstant.FILEINPUT)) {
			String inputFilePath = ArgumentOptions.getOptionValue(OptionConstant.FILEINPUT);

			File cfgFile = new File(inputFilePath);
			log.info("reading config from file: {}", cfgFile);
			return serverConfigReader.readConfigFromFile(cfgFile);

		} else {

			InputStream is = BemarDhcpServer.class.getResourceAsStream("/" + EnvConstants.DEFAULT_CONFIG_FILE);

			if (is == null) {
				log.error("Could not find {} in classpath", EnvConstants.DEFAULT_CONFIG_FILE);
				System.exit(2);
			}

			String content = IOUtils.toString(is, StandardCharsets.UTF_8);
			log.info("reading config from classpath file");
			return serverConfigReader.readConfigFromString(content);

		}
	}

}
