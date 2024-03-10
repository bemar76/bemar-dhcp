package ch.bemar.dhcp.convert;

import java.util.ServiceLoader;
import java.util.Set;

import ch.bemar.dhcp.exception.ConverterNotFoundException;

public class ValueByteBuilder {

	private ValueByteBuilder() {
	}

	public static byte[] valueSetToBytes(Byte optionType, Set<String> values) throws Exception {

		ServiceLoader<IListToBytesConverter> services = ServiceLoader.load(IListToBytesConverter.class);

		for (IListToBytesConverter service : services) {
			if (findByteInArray(service.supportsOptions(), optionType)) {

				return service.convert(values);
			}
		}

		throw new ConverterNotFoundException("For option type '" + optionType + "' no converter was found");

	}

	public static boolean findByteInArray(byte[] array, byte target) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == target) {
				return true;
			}
		}
		return false;
	}

}
