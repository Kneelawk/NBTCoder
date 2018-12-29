package com.github.kneelawk.nbtlanguage;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

public class NBTLanguageParseException extends RuntimeException {

	private static final long serialVersionUID = 6365836743322003806L;

	private ParseTree erroringRule;

	public NBTLanguageParseException(ParseTree erroringRule) {
		super(fromParserRuleContext(erroringRule));
		this.erroringRule = erroringRule;
	}

	public NBTLanguageParseException(String message, ParseTree erroringRule) {
		super(message + ", " + fromParserRuleContext(erroringRule));
		this.erroringRule = erroringRule;
	}

	public NBTLanguageParseException(Throwable cause, ParseTree erroringRule) {
		super(fromParserRuleContext(erroringRule), cause);
		this.erroringRule = erroringRule;
	}

	public NBTLanguageParseException(String message, Throwable cause, ParseTree erroringRule) {
		super(message + ", " + fromParserRuleContext(erroringRule), cause);
		this.erroringRule = erroringRule;
	}

	public NBTLanguageParseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace, ParseTree erroringRule) {
		super(message + ", " + fromParserRuleContext(erroringRule), cause, enableSuppression, writableStackTrace);
		this.erroringRule = erroringRule;
	}

	public ParseTree getErroringRule() {
		return erroringRule;
	}

	private static String fromParserRuleContext(ParseTree tree) {
		String str = "";
		if (tree instanceof ParserRuleContext) {
			ParserRuleContext ctx = (ParserRuleContext) tree;
			Token start = ctx.getStart();
			Token end = ctx.getStop();
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
			str += " ";
		}
		String text = tree.getText();
		str += "(" + (text.length() > 30 ? text.substring(0, 30) + "..." : text) + ")";
		return str;
	}
}
