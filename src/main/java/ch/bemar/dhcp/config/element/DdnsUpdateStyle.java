package ch.bemar.dhcp.config.element;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("ddns-update-style")
public class DdnsUpdateStyle extends ASingleString {

	public DdnsUpdateStyle(String configLine) {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "ddns-update-style";
	}

}
