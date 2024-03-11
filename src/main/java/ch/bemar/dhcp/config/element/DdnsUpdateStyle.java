package ch.bemar.dhcp.config.element;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DdnsUpdateStyle extends ASingleString {

	public DdnsUpdateStyle(String configLine) {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "ddns-update-style";
	}

}
