package ch.bemar.dhcp.config.reader;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.dhcp4java.DHCPOption;

import ch.bemar.dhcp.config.BaseConfiguration;
import ch.bemar.dhcp.config.DhcpKeyConfig;
import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.DhcpSubnetConfig;
import ch.bemar.dhcp.config.DhcpZoneConfig;
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
	private ZoneConfigReader zoneReader;
	private KeyConfigReader keyConfigReader;

	public ServerConfigReader() {
		this.subnetReader = new SubnetReader();
		this.zoneReader = new ZoneConfigReader();
		this.keyConfigReader = new KeyConfigReader();
	}

	public DhcpServerConfiguration readConfigFromFile(File file) throws OptionNotFoundException, Exception {
		return readConfigFromString(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
	}

	public DhcpServerConfiguration readConfigFromString(String content) throws Exception {

		ConfigFile confFile = new ConfigFile(content);

		DhcpServerConfiguration serverConfig = new DhcpServerConfiguration();
		log.debug("created config model");

		while (confFile.hasElements()) {

			String line = confFile.getNextLine();

			if (line.startsWith("subnet")) {

				serverConfig.getSubnets().add(subnetReader.readSubnet(confFile));

			} else if (line.startsWith("key")) {

				DhcpKeyConfig key = keyConfigReader.readKey(confFile);

				serverConfig.getKeys().put(key.getKey().getValue(), key);

			} else if (line.startsWith("zone")) {

				DhcpZoneConfig zone = zoneReader.readZone(confFile);

				serverConfig.getZones().put(zone.getZoneName(), zone);

			} else {

				handleLine(line, serverConfig);

			}

		}

		copyJob(serverConfig);

		return serverConfig;

	}

	private void copyJob(DhcpServerConfiguration serverConfig) {
		log.info("copying options from global config to subnet config");
		for (DHCPOption option : serverConfig.getOptions()) {

			for (DhcpSubnetConfig subnet : serverConfig.getSubnets()) {

				if (!subnet.getOptions().contains(option)) {
					log.info("Option {} not present in subnet {}", option, subnet);
					subnet.getOptions().add(option);
				}

				subnet.getZones().putAll(serverConfig.getZones());

				subnet.getKeys().putAll(serverConfig.getKeys());

			}

		}

	}

	@Override
	protected boolean handleLine(String line, BaseConfiguration config) throws Exception {
		return super.handleLine(line, config);
	}

}
