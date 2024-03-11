package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

public class MaxLeaseTime extends ASingleInteger {

	public MaxLeaseTime(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "max-lease-time";
	}

}
