package com.github.kneelawk.nbtcoder;

public class IncompatibleTagTypeException extends Exception {

	private static final long serialVersionUID = 328856091755386628L;

	public IncompatibleTagTypeException() {
		super();
	}

	public IncompatibleTagTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IncompatibleTagTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompatibleTagTypeException(String message) {
		super(message);
	}

	public IncompatibleTagTypeException(Throwable cause) {
		super(cause);
	}

}
