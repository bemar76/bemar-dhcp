package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HostName extends ASingleString {

	public HostName(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "host-name";
	}

}
