package ch.bemar.dhcp.config.element;

import java.lang.reflect.Constructor;
import java.util.Set;

import ch.bemar.dhcp.config.ConfigName;
import ch.bemar.dhcp.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigElementFactory {

	private static Set<Class<? extends IConfigElement>> elements;

	static {
		elements = ReflectionUtils.findImplementations(IConfigElement.class);
	}

	public IConfigElement getElementByConfigLine(String line) throws Exception {

		for (Class<? extends IConfigElement> clazz : elements) {

			log.trace("inspecting class {} for config line {}", clazz, line);

			ConfigName anno = clazz.getAnnotation(ConfigName.class);
			log.trace("Class annotation: {}", anno);

			if (anno == null) {
				throw new IllegalStateException("The clazz " + clazz + " has no ConfigName annotation");
			}

			if (line.trim().toLowerCase().startsWith(anno.value().toLowerCase())) {

				Constructor<? extends IConfigElement> constructor = clazz.getConstructor(String.class);

				return constructor.newInstance(line);

			}

		}

		log.warn("no config element found for line: '{}'");
		return null;
//		throw new ConfigElementNotFoundException("No config element found for " + line);
	}

}
