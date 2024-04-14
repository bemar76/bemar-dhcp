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

		Assertions.assertEquals(2, config.getConnection().getMappings().size());

		Assertions.assertEquals(4, config.getConnection().getProperties().size());

		Assertions.assertEquals("jdbc:h2:mem:testdb4;DB_CLOSE_DELAY=-1",
				config.getConnection().getPropertyValueByName(Configuration.PROP_CON_URL));
		Assertions.assertEquals("org.h2.Driver",
				config.getConnection().getPropertyValueByName(Configuration.PROP_DRIVER_CLASS));
		Assertions.assertEquals("", config.getConnection().getPropertyValueByName(Configuration.PROP_PASSWORD));
		Assertions.assertEquals("sa", config.getConnection().getPropertyValueByName(Configuration.PROP_USERNAME));

		Assertions.assertTrue(config.getConnection().containsClassname("ch.bemar.dhcp.persistence.model.DbLease"));
		Assertions.assertTrue(config.getConnection().containsClassname("ch.bemar.dhcp.persistence.Dingens"));

	}

}
