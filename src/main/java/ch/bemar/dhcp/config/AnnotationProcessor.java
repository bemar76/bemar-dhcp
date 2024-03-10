package ch.bemar.dhcp.config;

import java.lang.reflect.Field;

import ch.bemar.dhcp.config.element.HostConfiguration;

public class AnnotationProcessor {
	
	
	public static void printConfNames(Object obj) {
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfName.class)) {
				ConfName confName = field.getAnnotation(ConfName.class);
				System.out.println(field.getName() + " maps to " + confName.value());
			}
		}
	}

	public static void main(String[] args) {
		HostConfiguration config = new HostConfiguration();
		printConfNames(config);
	}
}