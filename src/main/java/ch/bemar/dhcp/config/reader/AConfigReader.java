package ch.bemar.dhcp.config.reader;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.dhcp4java.DHCPOption;

import ch.bemar.dhcp.config.BaseConfiguration;
import ch.bemar.dhcp.config.element.ConfigElementFactory;
import ch.bemar.dhcp.config.element.IConfigElement;
import ch.bemar.dhcp.constants.DHCPOptionFactory;
import ch.bemar.dhcp.constants.DhcpConstants;
import ch.bemar.dhcp.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AConfigReader {

	private ConfigElementFactory elementFactory;
	private DHCPOptionFactory optionFactory;

	public AConfigReader() {
		this.elementFactory = new ConfigElementFactory();
		this.optionFactory = new DHCPOptionFactory();
	}

	protected boolean handleLine(String line, BaseConfiguration config) throws Exception {

		if (line.trim().toLowerCase().startsWith(DhcpConstants.OPTION)) {

			return handleOption(line, config);

		} else {

			return handleElement(line, config);

		}

	}

	protected String removeSemicolon(String line) {
		if (line.contains(DhcpConstants.SEMICOLON)) {
			return StringUtils.substringBeforeLast(line, DhcpConstants.SEMICOLON);
		}

		return line;
	}

	protected boolean handleOption(String line, BaseConfiguration config) throws Exception {

		if (line.trim().toLowerCase().startsWith(DhcpConstants.OPTION)) {

			if (line.contains("\"")) {
				log.info("removing quotes: {}", line);
				line = StringUtils.remove(line, "\"");
				log.info("removed quotes: {}", line);
			}

			DHCPOption option = optionFactory.getFromLine(line);

			config.getOptions().add(option);
			return true;
		}

		return false;
	}

	protected boolean handleElement(String line, BaseConfiguration config) throws Exception {
		IConfigElement element = elementFactory.getElementByConfigLine(line);
		log.debug("found element {}", element);

		if (element != null) {

			for (Field field : ReflectionUtils.getAllFields(config.getClass())) {

				if (field.getType().isAssignableFrom(element.getClass())) {
					try {

						field.setAccessible(true);
						field.set(config, element);
						return true;

					} catch (Exception ex) {
						log.error(ex.getMessage(), ex);
						throw ex;
					}

				}

			}
		}

		log.warn("no element found for line: {}", line);
		return false;
	}

}
