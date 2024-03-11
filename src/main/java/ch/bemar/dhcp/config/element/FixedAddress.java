package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("fixed-address")
public class FixedAddress extends ASingleInetAddress {

	public FixedAddress(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "fixed-address";
	}

}
