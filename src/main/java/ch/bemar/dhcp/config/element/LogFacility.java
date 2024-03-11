package ch.bemar.dhcp.config.element;

import java.net.UnknownHostException;

import org.hibernate.tool.hbm2x.StringUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFacility extends ASingleString {

	public LogFacility(String configLine) throws UnknownHostException {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "log-facility";
	}

}
