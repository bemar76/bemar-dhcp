package ch.bemar.dhcp.persistence;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.persistence.cfg.Configuration;
import ch.bemar.dhcp.persistence.cfg.XmlLoader;
import jakarta.xml.bind.JAXBException;

public class XmlLoaderTest {

	@Test
	void testReadConfig() throws JAXBException, IOException {

		Configuration config = XmlLoader
				.loadConfiguration(XmlLoaderTest.class.getResourceAsStream("/persistence.cfg4.xml"));

		Assertions.assertEquals(2, config.getJdbcConnection().getMappings().size());

		Assertions.assertEquals(4, config.getJdbcConnection().getProperties().size());

		Assertions.assertEquals("", config.getJdbcConnection().getPropertyByName(Configuration.PROP_CON_URL));
		Assertions.assertEquals("", config.getJdbcConnection().getPropertyByName(Configuration.PROP_DRIVER_CLASS));
		Assertions.assertEquals("", config.getJdbcConnection().getPropertyByName(Configuration.PROP_PASSWORD));
		Assertions.assertEquals("", config.getJdbcConnection().getPropertyByName(Configuration.PROP_USERNAME));

		Assertions.assertEquals("",
				config.getJdbcConnection().getMappingByClassname("ch.bemar.dhcp.persistence.DbLease"));
		Assertions.assertEquals("",
				config.getJdbcConnection().getMappingByClassname("ch.bemar.dhcp.persistence.Dingens"));

	}

}
