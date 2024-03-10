package ch.bemar.dhcp.convert;

import java.util.Set;

public interface IListToBytesConverter {
	
	public byte[] convert(Set<String> values) throws Exception;
	
	public byte[] supportsOptions();

}
