package ch.bemar.dhcp.config.element;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DdnsUpdates extends ASingleString {

	public DdnsUpdates(String configLine)  {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "ddns-updates";
	}

}
