package ch.bemar.dhcp.convert.optimpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import com.google.auto.service.AutoService;

import ch.bemar.dhcp.convert.IListToBytesConverter;

@AutoService(IListToBytesConverter.class)
public class DomainListConverter implements IListToBytesConverter {

	@Override
	public byte[] convert(Set<String> values) throws IOException {
		return domainListToBytes(values.toArray(new String[0]));
	}

	@Override
	public byte[] supportsOptions() {
		return new byte[] { (byte) 119 };
	}

	public static byte[] domainListToBytes(String[] domainList) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			for (String domain : domainList) {
				for (String label : domain.split("\\.")) {
					byte[] labelBytes = label.getBytes();
					if (labelBytes.length > 63) {
						throw new IllegalArgumentException("Label length exceeds 63 bytes: " + label);
					}
					baos.write(labelBytes.length); // Länge des Labels
					baos.write(labelBytes); // Label-Bytes
				}
				baos.write(0);
			}

			return baos.toByteArray();
		}
	}
}
