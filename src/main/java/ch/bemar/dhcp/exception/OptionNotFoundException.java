package ch.bemar.dhcp.exception;

public class OptionNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3009963920064719254L;

	public OptionNotFoundException() {
		super();

	}

	public OptionNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public OptionNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public OptionNotFoundException(String message) {
		super(message);

	}

	public OptionNotFoundException(Throwable cause) {
		super(cause);

	}

}
