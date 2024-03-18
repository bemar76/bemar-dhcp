package ch.bemar.dhcp.config.element;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("subnet")
public class Subnet extends ASingleInetAddress {

	public Subnet(String configLine) throws UnknownHostException {
		super(configLine);
	}

	public Subnet(InetAddress value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getKeyWord() {
		return "subnet";
	}

}
