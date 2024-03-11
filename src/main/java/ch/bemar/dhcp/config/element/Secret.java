package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import org.hibernate.tool.hbm2x.StringUtils;

import ch.bemar.dhcp.config.ConfigName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("secret")
public class Secret extends ASingleString {

	public Secret(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "secret";
	}

}
