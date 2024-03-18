package ch.bemar.dhcp.config.element;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public abstract class ASingleString implements IConfigElement<String> {

	protected ASingleString(String configLine) {

		String[] tokens = StringUtils.split(configLine);
		if (tokens.length != 2) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " needs 1 parameter");
		}

		this.value = tokens[1].trim();

	}

	private String value;

	public String toString() {
		return this.getKeyWord() + " = " + this.value;
	}
}
