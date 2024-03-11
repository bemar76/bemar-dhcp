package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("authoritative")
public class Authoritative implements IConfigElement<Boolean> {

	public Authoritative(String configLine) throws UnknownHostException {
		
	}

	@Override
	public String getKeyWord() {
		return "authoritative";
	}

	@Override
	public Boolean getValue() {
		return true; // if this instance exists, the server is authoritative
	}

}
