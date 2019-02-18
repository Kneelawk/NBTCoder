package com.github.kneelawk.nbtcoder.hexlanguage;

import com.github.kneelawk.nbtcoder.hexlanguage.HexLanguageSystemParser.DataContext;
import com.github.kneelawk.nbtcoder.utils.InternalExceptionErrorStrategy;
import com.github.kneelawk.nbtcoder.utils.InternalParseException;
import com.github.kneelawk.nbtcoder.utils.LanguageParseException;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class HexLanguageParser {
	public byte[] parse(String string) throws IOException {
		return parse(CharStreams.fromString(string));
	}

	public byte[] parse(Reader reader) throws IOException {
		return parse(CharStreams.fromReader(reader));
	}

	public byte[] parse(InputStream stream) throws IOException {
		return parse(CharStreams.fromStream(stream));
	}

	public byte[] parse(CharStream stream) throws IOException {
		try {
			HexLanguageSystemLexer lexer = new HexLanguageSystemLexer(stream);
			lexer.removeErrorListeners();
			HexLanguageSystemParser parser = new HexLanguageSystemParser(new BufferedTokenStream(lexer));
			parser.removeErrorListeners();
			parser.setErrorHandler(new InternalExceptionErrorStrategy());
			HexLanguageBuilderListener builder = new HexLanguageBuilderListener();
			DataContext ctx = parser.data();

			ParseTreeWalker.DEFAULT.walk(builder, ctx);

			return builder.getData();
		} catch (InternalParseException e) {
			throw e.toLanguageParseException();
		} catch (IllegalStateException e) {
			throw new LanguageParseException(e, null);
		}
	}
}
