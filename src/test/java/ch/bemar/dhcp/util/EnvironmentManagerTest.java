package ch.bemar.dhcp.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.bemar.dhcp.env.EnvironmentManager;
import ch.bemar.dhcp.net.arp.ArpPropertiesConstants;


public class EnvironmentManagerTest {

	@Test
	void testEnvManagerWithoutPreLoad() {
		Assertions.assertEquals("arp -a", EnvironmentManager.getInstance().getEnvAsString(ArpPropertiesConstants.CMD));
	}

	

}
