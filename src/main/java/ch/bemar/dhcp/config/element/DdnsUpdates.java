package ch.bemar.dhcp.config.element;

import ch.bemar.dhcp.config.ConfigName;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigName("ddns-updates")
public class DdnsUpdates extends ABoolean {

	public DdnsUpdates(String configLine)  {
		super(configLine);
	}

	@Override
	public String getKeyWord() {
		return "ddns-updates";
	}

}
