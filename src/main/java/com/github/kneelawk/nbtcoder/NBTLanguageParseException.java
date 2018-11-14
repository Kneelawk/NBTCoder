package com.github.kneelawk.nbtcoder;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class NBTLanguageParseException extends RuntimeException {

	private static final long serialVersionUID = 6365836743322003806L;

	private ParserRuleContext erroringRule;

	public NBTLanguageParseException(ParserRuleContext erroringRule) {
		super(fromParserRuleContext(erroringRule));
		this.erroringRule = erroringRule;
	}

	public NBTLanguageParseException(String message, ParserRuleContext erroringRule) {
		super(message + ", " + fromParserRuleContext(erroringRule));
		this.erroringRule = erroringRule;
	}

	public NBTLanguageParseException(Throwable cause, ParserRuleContext erroringRule) {
		super(fromParserRuleContext(erroringRule), cause);
		this.erroringRule = erroringRule;
	}

	public NBTLanguageParseException(String message, Throwable cause, ParserRuleContext erroringRule) {
		super(message + ", " + fromParserRuleContext(erroringRule), cause);
		this.erroringRule = erroringRule;
	}

	public NBTLanguageParseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace, ParserRuleContext erroringRule) {
		super(message + ", " + fromParserRuleContext(erroringRule), cause, enableSuppression, writableStackTrace);
		this.erroringRule = erroringRule;
	}

	public ParserRuleContext getErroringRule() {
		return erroringRule;
	}

	private static String fromParserRuleContext(ParserRuleContext ctx) {
		Token start = ctx.getStart();
		Token end = ctx.getStop();
		String str = "";
		if (start.getLine() == end.getLine()) {
			str = start.getLine() + ":";
			if (start.getStartIndex() == start.getStopIndex() && end.getStartIndex() == end.getStopIndex()
					&& start.getStartIndex() == end.getStartIndex()) {
				str += start.getStartIndex();
			} else {
				str += start.getStartIndex() + "-" + end.getStopIndex();
			}
		} else {
			str = start.getLine() + ":" + start.getStartIndex() + "-" + end.getLine() + ":" + end.getStopIndex();
		}
		str += " (" + ctx.getText() + ")";
		return str;
	}
}
