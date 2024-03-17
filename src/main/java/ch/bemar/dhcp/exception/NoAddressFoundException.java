package ch.bemar.dhcp.exception;

public class NoAddressFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3009963920064719254L;

	public NoAddressFoundException() {
		super();

	}

	public NoAddressFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public NoAddressFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public NoAddressFoundException(String message) {
		super(message);

	}

	public NoAddressFoundException(Throwable cause) {
		super(cause);

	}

}
