package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("ddns-rev-domainname")
public class DdnsRevDomainName extends ASingleString {

	public DdnsRevDomainName(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "ddns-rev-domainname";
	}

}
