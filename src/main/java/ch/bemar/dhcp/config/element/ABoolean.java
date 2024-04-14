package ch.bemar.dhcp.config.element;



import ch.bemar.dhcp.util.StringUtils;
import lombok.Data;

@Data
public abstract class ABoolean implements IConfigElement<Boolean> {

	protected ABoolean(String configLine) {

		String[] tokens = StringUtils.splitRespectsQuotes(configLine);
		if (tokens.length != 2) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " needs 1 parameter");
		}

		if ("on".equalsIgnoreCase(tokens[1].trim()) || "true".equalsIgnoreCase(tokens[1].trim())) {

			value = true;

		} else {

			value = false;
		}

	}

	private Boolean value;

	public String toString() {
		return this.getKeyWord() + " = " + this.value;
	}
}
