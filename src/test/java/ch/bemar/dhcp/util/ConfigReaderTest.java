package ch.bemar.dhcp.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.config.DhcpServerConfiguration;
import ch.bemar.dhcp.config.reader.ServerConfigReader;
import ch.bemar.dhcp.exception.OptionNotFoundException;

public class ConfigReaderTest {

	@Test
	void testServerConfigRead() throws OptionNotFoundException, Exception {

		String content = IOUtils.toString(this.getClass().getResourceAsStream("/dhcpd.conf"), StandardCharsets.UTF_8);

		ServerConfigReader scr = new ServerConfigReader();

		DhcpServerConfiguration config = scr.readConfigFromFile(content);

	}

}
