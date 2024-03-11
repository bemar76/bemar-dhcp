package ch.bemar.dhcp.config.element;

import java.util.Arrays;

import org.codehaus.plexus.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ConfigElementFactoryTest {

	@ParameterizedTest
	@CsvSource({ "fixed-address 10.0.0.200,ch.bemar.dhcp.config.element.FixedAddress,/10.0.0.200", //
			"hardware ethernet 00:14:5E:5A:31:57,org.dhcp4java.HardwareAddress,[0; 20; 94; 90; 49; 87]", //
			"default-lease-time 6000,ch.bemar.dhcp.config.element.DefaultLeaseTime,6000", //
			"max-lease-time 8000,ch.bemar.dhcp.config.element.MaxLeaseTime,8000", //
			"authoritative,ch.bemar.dhcp.config.element.Authoritative,true", //
			"allow booting,ch.bemar.dhcp.config.element.Allow,booting", //
			"ddns-update-style none,ch.bemar.dhcp.config.element.DdnsUpdateStyle,none",
			"ddns-updates off,ch.bemar.dhcp.config.element.DdnsUpdates,off", //
			"update-static-leases on,ch.bemar.dhcp.config.element.UpdateStaticLeases,on",
			"algorithm hmac-md5,ch.bemar.dhcp.config.element.Algorithm,hmac-md5",
			"secret \"geheim\",ch.bemar.dhcp.config.element.Secret,\"geheim\"",
			"primary 192.168.1.1,ch.bemar.dhcp.config.element.Primary,/192.168.1.1",
			"key dhcp-update,ch.bemar.dhcp.config.element.Key,dhcp-update"})
	void testAllElements(String line, String expectedClazzname, String expectedValueString) throws Exception {

		IConfigElement instance = new ConfigElementFactory().getElementByConfigLine(line);
		log.debug("got instance: {}", instance);

		Assertions.assertNotNull(instance);

		log.debug("trying to generate class from name '{}'", expectedClazzname);
		Assertions.assertEquals(Class.forName(expectedClazzname), instance.getClass());

		Object result = instance.getValue();
		log.debug("got result value from instance: '{}'", result);

		Assertions.assertTrue(assertTrue(expectedValueString, instance.getValue()));

	}

	private boolean assertTrue(String expected, Object result) {

		if (result.getClass().isArray() && result instanceof byte[]) {
			log.debug("'{}' == '{}'", StringUtils.replace(expected, ";", ","), Arrays.toString((byte[]) result));
			return StringUtils.replace(expected, ";", ",").equals(Arrays.toString((byte[]) result));
		}

		log.debug("'{}' == '{}'", expected, result);
		return expected.equals(result.toString());
	}

}
