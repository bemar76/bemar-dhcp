package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

public class DefaultLeaseTime extends ASingleInteger {

	public DefaultLeaseTime(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "default-lease-time";
	}

}
