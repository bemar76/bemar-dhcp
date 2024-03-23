package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;

@ConfigName("default-lease-time")
public class DefaultLeaseTime extends ASingleInteger {

	public DefaultLeaseTime(String configLine) throws UnknownHostException {
		super(configLine);
	}

	public DefaultLeaseTime(int value) throws UnknownHostException {
		super(value);
	}

	@Override
	public String getKeyWord() {
		return "default-lease-time";
	}

}
