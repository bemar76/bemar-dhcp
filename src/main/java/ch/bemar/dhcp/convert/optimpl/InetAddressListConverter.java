package ch.bemar.dhcp.convert.optimpl;

import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;

import ch.bemar.dhcp.convert.IListToBytesConverter;


@AutoService(IListToBytesConverter.class)
public class InetAddressListConverter implements IListToBytesConverter {

	@Override
	public byte[] convert(Set<String> values) throws IOException {

		return ipSetToBytes(convertToInetAddresses(values));
	}

	@Override
	public byte[] supportsOptions() {
		return new byte[] { (byte) 3, (byte) 6, (byte) 42, (byte) 44, (byte) 45, (byte) 48 };
	}

	private InetAddress[] convertToInetAddresses(Set<String> values) throws UnknownHostException {
		Set<InetAddress> addresses = Sets.newHashSet();

		for (String value : values) {

			addresses.add(InetAddress.getByName(value));

		}

		return addresses.toArray(new InetAddress[0]);
	}

	public static byte[] ipSetToBytes(InetAddress[] ips) throws UnknownHostException {
		List<byte[]> options = new ArrayList<>();

		for (InetAddress ip : ips) {
			byte[] nameserverIPBytes = ip.getAddress();
			options.add(nameserverIPBytes);
		}

		int totalLength = 0;
		for (byte[] option : options) {
			totalLength += option.length;
		}

		byte[] dhcpOffer = new byte[totalLength];
		int offset = 0;
		for (byte[] option : options) {
			System.arraycopy(option, 0, dhcpOffer, offset, option.length);
			offset += option.length;
		}

		return dhcpOffer;
	}
}
