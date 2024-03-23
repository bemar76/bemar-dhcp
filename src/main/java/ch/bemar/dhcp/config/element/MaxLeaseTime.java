package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;
@ConfigName("max-lease-time")
public class MaxLeaseTime extends ASingleInteger {

	public MaxLeaseTime(String configLine) throws UnknownHostException {
		super(configLine);
	}
	
	public MaxLeaseTime(int value) throws UnknownHostException {
		super(value);
	}

	@Override
	public String getKeyWord() {
		return "max-lease-time";
	}

}
