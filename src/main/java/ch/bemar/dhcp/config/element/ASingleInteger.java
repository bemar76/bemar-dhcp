package ch.bemar.dhcp.config.element;



import ch.bemar.dhcp.util.StringUtils;
import lombok.Data;

@Data
public abstract class ASingleInteger implements IConfigElement<Integer> {

	protected ASingleInteger(String configLine) {

		String[] tokens = StringUtils.splitRespectsQuotes(configLine);
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
