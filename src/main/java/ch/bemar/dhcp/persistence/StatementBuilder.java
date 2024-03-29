package ch.bemar.dhcp.persistence;

import java.lang.reflect.Field;

import org.apache.maven.shared.utils.StringUtils;

import com.google.common.base.Strings;

import ch.bemar.dhcp.persistence.model.Id;
import ch.bemar.dhcp.util.ReflectionUtils;

public class StatementBuilder<T> {

	public String update(T entity) throws IllegalArgumentException, IllegalAccessException {

		Where whereTag = new Where();
		Comma commaTag = new Comma();

		StringBuilder update = new StringBuilder("UPDATE ");
		update.append(entity.getClass().getSimpleName());
		update.append(" set");

		StringBuilder where = new StringBuilder(" ");

		for (Field field : ReflectionUtils.getAllFields(entity.getClass())) {

			field.setAccessible(true);

			Id id = field.getAnnotation(Id.class);

			if (id != null) {

				where.append(whereTag).append(field.getName())
						.append(comparatorWithValue(getFieldValueForStatement(entity, field)));

			} else {

				update.append(commaTag).append(field.getName())
						.append(comparatorWithValue(getFieldValueForStatement(entity, field)));
			}

		}

		return cleanStatement(update.append(where).toString());
	}

	public String insert(T entity) throws IllegalArgumentException, IllegalAccessException {

		Comma commaFields = new Comma();
		Comma commaValues = new Comma();

		StringBuilder fields = new StringBuilder("INSERT INTO ");
		fields.append(entity.getClass().getSimpleName());
		fields.append("(");

		StringBuilder values = new StringBuilder("VALUES (");

		for (Field field : ReflectionUtils.getAllFields(entity.getClass())) {

			field.setAccessible(true);

			fields.append(commaFields).append(field.getName());

			values.append(commaValues).append(getFieldValueForStatement(entity, field));

		}

		fields.append(")");
		values.append(")");

		return cleanStatement(fields.append(values).toString());
	}

	public String delete(T entity) throws IllegalArgumentException, IllegalAccessException {

		Where whereTag = new Where();

		StringBuilder update = new StringBuilder("DELETE FROM ");
		update.append(entity.getClass().getSimpleName());

		StringBuilder where = new StringBuilder();

		for (Field field : ReflectionUtils.getAllFields(entity.getClass())) {

			field.setAccessible(true);

			Id id = field.getAnnotation(Id.class);

			if (id != null) {

				where.append(whereTag).append(field.getName())
						.append(comparatorWithValue(getFieldValueForStatement(entity, field)));

			}
		}

		return cleanStatement(update.append(where).toString());
	}

	public String select(T entity, boolean insertNull) throws IllegalArgumentException, IllegalAccessException {

		Where whereTag = new Where();

		StringBuilder select = new StringBuilder("SELECT * FROM ");
		select.append(entity.getClass().getSimpleName());

		StringBuilder where = new StringBuilder(" ");

		for (Field field : ReflectionUtils.getAllFields(entity.getClass())) {

			field.setAccessible(true);

			String value = getFieldValueForStatement(entity, field);

			if (!Strings.isNullOrEmpty(value) || insertNull) {

				where.append(whereTag).append(field.getName())
						.append(comparatorWithValue(getFieldValueForStatement(entity, field)));

			}

		}

		return cleanStatement(select.append(where).toString());
	}

	private String getFieldValueForStatement(Object instance, Field field)
			throws IllegalArgumentException, IllegalAccessException {

		field.setAccessible(true);
		Object value = field.get(instance);

		if (value != null) {

			if (field.getType().equals(String.class)) {

				return "'" + value + "'";

			}

			return value.toString();

		}

		return null;

	}

	private String comparatorWithValue(String value) {
		if (value == null) {
			return " IS " + value;
		}

		return " = " + value;
	}

	private String cleanStatement(String statement) {
		return StringUtils.replace(statement, "  ", " ").trim();
	}

}
