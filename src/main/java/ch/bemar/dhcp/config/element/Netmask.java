package ch.bemar.dhcp.config.element;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("netmask")
public class Netmask extends ASingleInetAddress {

	public Netmask(String configLine) throws UnknownHostException {
		super(configLine);
	}

	public Netmask(InetAddress address) {
		super(address);
	}

	@Override
	public String getKeyWord() {
		return "netmask";
	}

}
