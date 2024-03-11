package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class NextServer extends ASingleInetAddress {

	public NextServer(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "fixed-address";
	}

}
