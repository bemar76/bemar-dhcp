package ch.bemar.dhcp.config.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.dhcp4java.DHCPOption;

import com.google.common.base.Strings;

import ch.bemar.dhcp.config.element.DhcpServerConfiguration;
import ch.bemar.dhcp.constants.DhcpConstants;
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

	public DhcpServerConfiguration readConfigFromFile(File file) throws OptionNotFoundException, Exception {

		String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		log.debug("read content {} from file {}", content, file);

		BufferedReader br = new BufferedReader(new StringReader(content));
		log.debug("created buffered reader from content");

		DhcpServerConfiguration serverConfig = new DhcpServerConfiguration();
		log.debug("created config model");

		String line = null;

		while ((line = br.readLine()) != null) {
			log.debug("current line: {}", line);

			if (!Strings.isNullOrEmpty(line)) {

				if (line.trim().startsWith(DhcpConstants.OPTION)) {
					ConfigOption cfgOption = new ConfigOption(line);
					DHCPOption dhcpOption = DhcpOptionReader.createDHCPOption(cfgOption);
					
					serverConfig.getOptions().add(dhcpOption);
				}

			}

		}

		return serverConfig;

	}
	
	private void assignAttributesToFields(String content, DhcpServerConfiguration serverConfig) {
		
		
		
		
	}
}
