package ch.bemar.dhcp.exception;

public class TypeNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3009963920064719254L;

	public TypeNotFoundException() {
		super();

	}

	public TypeNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public TypeNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public TypeNotFoundException(String message) {
		super(message);

	}

	public TypeNotFoundException(Throwable cause) {
		super(cause);

	}

}
