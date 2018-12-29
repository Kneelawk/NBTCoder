package com.github.kneelawk.utils;

import java.io.IOException;

import org.antlr.v4.runtime.tree.ParseTree;

public class LanguageParseException extends IOException {
	private static final long serialVersionUID = -6169973718483952732L;

	private ParseTree erroringRule;

	public LanguageParseException(ParseTree erroringRule) {
		this.erroringRule = erroringRule;
	}

	public LanguageParseException(String message, ParseTree erroringRule) {
		super(message);
		this.erroringRule = erroringRule;
	}

	public LanguageParseException(Throwable cause, ParseTree erroringRule) {
		super(cause);
		this.erroringRule = erroringRule;
	}

	public LanguageParseException(String message, Throwable cause, ParseTree erroringRule) {
		super(message, cause);
		this.erroringRule = erroringRule;
	}

	public ParseTree getErroringRule() {
		return erroringRule;
	}

}
