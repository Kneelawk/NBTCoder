package com.github.kneelawk.nbtcoder.nbtlanguage;

public class TagParseException extends Exception {

	private static final long serialVersionUID = 5971021202992052937L;

	public TagParseException() {
	}

	public TagParseException(String message) {
		super(message);
	}

	public TagParseException(Throwable cause) {
		super(cause);
	}

	public TagParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public TagParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
