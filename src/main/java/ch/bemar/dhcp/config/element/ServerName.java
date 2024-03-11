package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("server-name")
public class ServerName extends ASingleString {

	public ServerName(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "server-name";
	}

}
