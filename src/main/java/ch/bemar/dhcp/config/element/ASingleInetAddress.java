package ch.bemar.dhcp.config.element;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.bemar.dhcp.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class ASingleInetAddress implements IConfigElement<InetAddress> {

	protected ASingleInetAddress(String configLine) throws UnknownHostException {

		String[] tokens =StringUtils.splitRespectsQuotes(configLine);
		if (tokens.length != 2) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " needs 1 parameter");
		}

		this.value = InetAddress.getByName(tokens[1].trim());

	}

	private final InetAddress value;

	public String toString() {
		return this.getKeyWord() + " = " + this.value;
	}

	public ASingleInetAddress(InetAddress value) {
		super();
		this.value = value;
	}

}
