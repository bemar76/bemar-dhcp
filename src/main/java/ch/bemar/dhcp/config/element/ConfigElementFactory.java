package ch.bemar.dhcp.config.element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import ch.bemar.dhcp.config.ConfigName;
import ch.bemar.dhcp.exception.ConfigElementNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigElementFactory {

	private static Set<Class<? extends IConfigElement>> elements;

	static {
		elements = findAllConfigElementClasses();
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

	private static Set<Class<? extends IConfigElement>> findAllConfigElementClasses() {
		Class<?> interfaceToFind = IConfigElement.class;

		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath())
				.setScanners(new SubTypesScanner(false)));

		// Finde alle Untertypen im Classpath
		Set<Class<? extends IConfigElement>> implementors = reflections
				.getSubTypesOf((Class<IConfigElement>) interfaceToFind);

		Set<Class<? extends IConfigElement>> nonAbstractImplementors = implementors.stream()
				.filter(clazz -> !Modifier.isAbstract(clazz.getModifiers())).collect(Collectors.toSet());

		return nonAbstractImplementors;

	}

}
