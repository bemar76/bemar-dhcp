package ch.bemar.dhcp.config.reader;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import ch.bemar.dhcp.config.BaseConfiguration;
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
public class ServerConfigReader extends AConfigReader {

	private SubnetReader subnetReader;

	public ServerConfigReader() {
		this.subnetReader = new SubnetReader();

	}

	public DhcpServerConfiguration readConfigFromFile(File file) throws OptionNotFoundException, Exception {
		return readConfigFromFile(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
	}

	public DhcpServerConfiguration readConfigFromFile(String content) throws OptionNotFoundException, Exception {

		ConfigFile confFile = new ConfigFile(content);

		DhcpServerConfiguration serverConfig = new DhcpServerConfiguration();
		log.debug("created config model");

		while (confFile.hasElements()) {
			
			String line = confFile.getNextLine();

			if (line.startsWith("subnet")) {

				serverConfig.getSubnets().add(subnetReader.readSubnet(confFile));

			} else {

				handleLine(line, serverConfig);

			}

		}

		return serverConfig;

	}

	@Override
	protected boolean handleLine(String line, BaseConfiguration config) throws Exception {		
		return super.handleLine(line, config);
	}

}
