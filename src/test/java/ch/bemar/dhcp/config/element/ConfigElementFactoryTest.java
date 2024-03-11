package ch.bemar.dhcp.config.element;

import java.net.InetAddress;

import org.dhcp4java.HardwareAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.emory.mathcs.backport.java.util.Arrays;

class ConfigElementFactoryTest {

	@Test
	void testFixedIpConfig() throws Exception {

		String line = "fixed-address 10.0.0.200";

		FixedAddress fixedAddress = (FixedAddress) new ConfigElementFactory().getElementByConfigLine(line);

		Assertions.assertNotNull(fixedAddress);

		Assertions.assertEquals(InetAddress.getByName("10.0.0.200"), fixedAddress.getValue());

	}

	@Test
	void testMacAddress() throws Exception {

		String line = "hardware ethernet 00:14:5E:5A:31:57";

		HardwareAddress hardwareAddress = (HardwareAddress) new ConfigElementFactory().getElementByConfigLine(line);

		Assertions.assertNotNull(hardwareAddress);

		Assertions.assertEquals("[0, 20, 94, 90, 49, 87]", Arrays.toString(hardwareAddress.getValue()));

	}

}
