package ch.bemar.dhcp.exception;

public class ConfigElementNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3009963920064719254L;

	public ConfigElementNotFoundException() {
		super();

	}

	public ConfigElementNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public ConfigElementNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public ConfigElementNotFoundException(String message) {
		super(message);

	}

	public ConfigElementNotFoundException(Throwable cause) {
		super(cause);

	}

}
