package com.github.kneelawk.nbtcoder.utils;

import java.io.IOException;

public class LanguageParseException extends IOException {
	private static final long serialVersionUID = -6169973718483952732L;

	private TextLocation erroringLocation;

	public LanguageParseException(TextLocation erroringRule) {
		this.erroringLocation = erroringRule;
	}

	public LanguageParseException(String message, TextLocation erroringRule) {
		super(message);
		this.erroringLocation = erroringRule;
	}

	public LanguageParseException(Throwable cause, TextLocation erroringRule) {
		super(cause);
		this.erroringLocation = erroringRule;
	}

	public LanguageParseException(String message, Throwable cause, TextLocation erroringRule) {
		super(message, cause);
		this.erroringLocation = erroringRule;
	}

	public TextLocation getErroringLocation() {
		return erroringLocation;
	}

}
