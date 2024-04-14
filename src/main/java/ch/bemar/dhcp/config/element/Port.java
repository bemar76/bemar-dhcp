package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("port")
public class Port extends ASingleInteger {

	public Port(String configLine) throws UnknownHostException {
		super(configLine);
	}
	
	public Port(int port) {
		super(port);
	}

	@Override
	public String getKeyWord() {
		return "port";
	}

}
