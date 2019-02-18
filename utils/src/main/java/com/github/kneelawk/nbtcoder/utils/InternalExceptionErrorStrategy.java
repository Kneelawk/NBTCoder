package com.github.kneelawk.nbtcoder.utils;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

public class InternalExceptionErrorStrategy extends DefaultErrorStrategy {
	@Override
	public void recover(Parser recognizer, RecognitionException e) {
		for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
			context.exception = e;
		}

		// create messages and throw exceptions
		if (e instanceof NoViableAltException) {
			NoViableAltException nvae = (NoViableAltException) e;
			TokenStream tokens = recognizer.getInputStream();
			String input;
			if (tokens != null) {
				if (nvae.getStartToken().getType() == Token.EOF)
					input = "<EOF>";
				else
					input = tokens.getText(nvae.getStartToken(), e.getOffendingToken());
			} else {
				input = "<unknown input>";
			}
			String msg = "no viable alternative at input " + escapeWSAndQuote(input);
			throw new InternalParseException(msg, e, new TextLocation(e.getOffendingToken()));
		} else if (e instanceof InputMismatchException) {
			String msg = "mismatched input " + getTokenErrorDisplay(e.getOffendingToken()) + " expecting "
					+ e.getExpectedTokens().toString(recognizer.getVocabulary());
			throw new InternalParseException(msg, e, new TextLocation(e.getOffendingToken()));
		} else if (e instanceof FailedPredicateException) {
			String ruleName = recognizer.getRuleNames()[recognizer.getContext().getRuleIndex()];
			String msg = "rule " + ruleName + " " + e.getMessage();
			throw new InternalParseException(msg, e, new TextLocation(e.getOffendingToken()));
		} else {
			throw new InternalParseException(e.getMessage(), e, new TextLocation(e.getOffendingToken()));
		}
	}

	@Override
	public Token recoverInline(Parser recognizer) throws RecognitionException {
		InputMismatchException e = new InputMismatchException(recognizer);
		for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
			context.exception = e;
		}

		String msg = "mismatched input " + getTokenErrorDisplay(e.getOffendingToken()) + " expecting "
				+ e.getExpectedTokens().toString(recognizer.getVocabulary());
		throw new InternalParseException(msg, e, new TextLocation(e.getOffendingToken()));
	}

	@Override
	public void sync(Parser recognizer) {
	}
}
