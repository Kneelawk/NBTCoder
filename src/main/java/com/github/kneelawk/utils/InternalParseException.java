package com.github.kneelawk.utils;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

public class InternalParseException extends ParseCancellationException {

	private static final long serialVersionUID = 6365836743322003806L;

	private TextLocation erroringLocation;

	public InternalParseException(TextLocation erroringLocation) {
		this.erroringLocation = erroringLocation;
	}

	public InternalParseException(ParseTree erroringRule) {
		this.erroringLocation = new TextLocation(erroringRule);
	}

	public InternalParseException(String message, TextLocation erroringLocation) {
		super(message);
		this.erroringLocation = erroringLocation;
	}

	public InternalParseException(String message, ParseTree erroringRule) {
		super(message);
		this.erroringLocation = new TextLocation(erroringRule);
	}

	public InternalParseException(Throwable cause, TextLocation erroringLocation) {
		super(cause);
		this.erroringLocation = erroringLocation;
	}

	public InternalParseException(Throwable cause, ParseTree erroringRule) {
		super(cause);
		this.erroringLocation = new TextLocation(erroringRule);
	}

	public InternalParseException(String message, Throwable cause, TextLocation erroringLocation) {
		super(message, cause);
		this.erroringLocation = erroringLocation;
	}

	public InternalParseException(String message, Throwable cause, ParseTree erroringRule) {
		super(message, cause);
		this.erroringLocation = new TextLocation(erroringRule);
	}

	public TextLocation getErroringLocation() {
		return erroringLocation;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + ", " + erroringLocation.toString();
	}

	public LanguageParseException toLanguageParseException() {
		return new LanguageParseException(this, erroringLocation);
	}
}
