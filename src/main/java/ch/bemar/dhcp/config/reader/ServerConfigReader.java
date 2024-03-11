package ch.bemar.dhcp.config.reader;

import java.io.File;
import java.lang.reflect.Field;

import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.exception.OptionNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * reads the config from the file and converts it into the config model
 * 
 * @author bemar
 *
 */
@Slf4j
public class ServerConfigReader {

	private SubnetReader subnetReader;


	public ServerConfigReader() {
		this.subnetReader = new SubnetReader();
	}

	public DhcpServerConfiguration readConfigFromFile(File file) throws OptionNotFoundException, Exception {

		ConfigFile confFile = new ConfigFile(file);

		DhcpServerConfiguration serverConfig = new DhcpServerConfiguration();
		log.debug("created config model");

		while (confFile.hasElements()) {

			handleField(confFile.getCurrentLine(), serverConfig);

			if (confFile.getPreview().toLowerCase().startsWith("subnet")) {

				serverConfig.getSubnets().add(subnetReader.readSubnet(confFile));

			}

		}

		return serverConfig;

	}

	private void handleField(String line, DhcpServerConfiguration serverConfig) {
		Field[] fields = serverConfig.getClass().getFields();

		for (Field field : fields) {

			

		}

	}

}
