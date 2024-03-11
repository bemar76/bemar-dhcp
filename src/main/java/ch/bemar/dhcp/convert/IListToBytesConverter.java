package ch.bemar.dhcp.convert;

import java.util.Collection;

public interface IListToBytesConverter {
	
	public byte[] convert(Collection<String> values) throws Exception;
	
	public byte[] supportsOptions();

}
