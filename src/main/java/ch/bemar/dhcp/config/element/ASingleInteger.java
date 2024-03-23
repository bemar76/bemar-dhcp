package ch.bemar.dhcp.config.element;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public abstract class ASingleInteger implements IConfigElement<Integer> {

	protected ASingleInteger(String configLine) {

		String[] tokens = StringUtils.split(configLine);
		if (tokens.length != 2) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " needs 1 parameter");
		}

		this.value = Integer.valueOf(tokens[1]);

	}

	public ASingleInteger(int value) {
		this.value = value;
	}

	private Integer value;

	public String toString() {
		return this.getKeyWord() + " = " + this.value;
	}
}
