package ch.bemar.dhcp.config.element;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.apache.maven.shared.utils.StringUtils;
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
			"ddns-updates on,ch.bemar.dhcp.config.element.DdnsUpdates,true", //
			"ddns-updates off,ch.bemar.dhcp.config.element.DdnsUpdates,false", //
			"update-static-leases on,ch.bemar.dhcp.config.element.UpdateStaticLeases,on",
			"algorithm hmac-md5,ch.bemar.dhcp.config.element.Algorithm,hmac-md5",
			"secret \"geheim\",ch.bemar.dhcp.config.element.Secret,geheim",
			"primary 192.168.1.1,ch.bemar.dhcp.config.element.Primary,/192.168.1.1",
			"key dhcp-update,ch.bemar.dhcp.config.element.Key,dhcp-update" })
	void testAllElements(String line, String expectedClazzname, String expectedValueString) throws Exception {

		IConfigElement instance = new ConfigElementFactory().getElementByConfigLine(line);
		log.debug("got instance: {}", instance);

		Assertions.assertNotNull(instance);

		log.debug("trying to generate class from name '{}'", expectedClazzname);
		Assertions.assertEquals(Class.forName(expectedClazzname), instance.getClass());

		Object result = instance.getValue();
		log.debug("got result value from instance: '{}'", result);

		assertThat(toString(instance.getValue()), is(replaceSemicolon(expectedValueString)));

	}

	private static String replaceSemicolon(String expectedValue) {
		return StringUtils.replace(expectedValue, ";", ",");
	}

	private String toString(Object value) {

		if (value != null && value.getClass().isArray()) {

			return Arrays.deepToString((Object[]) convertToArrayOfObject(value));
		}
		if (value != null) {
			return value.toString();
		}

		return "null";
	}

	private static Object[] convertToArrayOfObject(Object array) {
		if (array instanceof Object[]) {
			return (Object[]) array; // Kein Cast notwendig für nicht-primitive Arrays
		}

		int length = Array.getLength(array);
		Object[] objectArray = new Object[length];
		for (int i = 0; i < length; i++) {
			objectArray[i] = Array.get(array, i); // Holen der Elemente als Object
		}
		return objectArray;
	}

}
