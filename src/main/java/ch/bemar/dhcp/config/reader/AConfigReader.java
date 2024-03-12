package ch.bemar.dhcp.config.reader;

import java.lang.reflect.Field;

import org.dhcp4java.DHCPOption;

import ch.bemar.dhcp.config.element.ConfigElementFactory;
import ch.bemar.dhcp.config.element.IConfigElement;
import ch.bemar.dhcp.constants.DhcpConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AConfigReader {

	private ConfigElementFactory elementFactory;

	public AConfigReader() {
		this.elementFactory = new ConfigElementFactory();
	}

	protected boolean handleLine(String line, Object config) throws Exception {

	}

	protected boolean handleOption(String line, Object config) throws Exception {

		if (line.trim().toLowerCase().startsWith(DhcpConstants.OPTION)) {

		DHCPOption.
			
		}
	}

	protected boolean handleElement(String line, Object config) throws Exception {
		IConfigElement element = elementFactory.getElementByConfigLine(line);
		log.debug("found element {}", element);

		if (element != null) {

			for (Field field : config.getClass().getFields()) {

				if (field.getType().isAssignableFrom(element.getClass())) {

					field.setAccessible(true);
					field.set(config, element);
					return true;

				}

			}
		}

		return false;
	}

}
