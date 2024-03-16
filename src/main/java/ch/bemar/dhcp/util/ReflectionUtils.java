package ch.bemar.dhcp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class ReflectionUtils {

	private ReflectionUtils() {
	}

	/**
	 * returns all fields in child and super class
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getAllFields(Class<?> clazz) {	
		List<Field> fields = new ArrayList<>();
		Class<?> currentClass = clazz;

		// Durchlaufen der Klassenhierarchie bis zur obersten Klasse
		while (currentClass != null) {
			// Füge alle deklarierten Felder der aktuellen Klasse zur Liste hinzu
			Field[] declaredFields = currentClass.getDeclaredFields();
			for (Field field : declaredFields) {
				fields.add(field);
			}
			// Gehe zur Oberklasse
			currentClass = currentClass.getSuperclass();
		}

		// Konvertiere die Liste in ein Array und gebe es zurück
		return fields.toArray(new Field[0]);
	}

	public static <T> Set<Class<? extends T>> findImplementations(Class<T> clazz) {
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath())
				.setScanners(new SubTypesScanner(false)));

		Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);
		return subTypes.stream().filter(aClass -> !Modifier.isAbstract(aClass.getModifiers()))
				.collect(Collectors.toSet());
	}
}
