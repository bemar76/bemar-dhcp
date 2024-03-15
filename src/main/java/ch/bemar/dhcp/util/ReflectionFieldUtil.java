package ch.bemar.dhcp.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionFieldUtil {

	private ReflectionFieldUtil() {
	}

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

//	public static void main(String[] args) {
//		// Beispielverwendung mit einer hypothetischen Klasse und deren Oberklasse
//		Field[] allFields = getAllFields(DeineKlasse.class);
//		for (Field field : allFields) {
//			System.out.println("Feld: " + field.getName() + ", Typ: " + field.getType());
//		}
//	}
}
