package com.github.kneelawk.hexlanguage;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.github.kneelawk.hexlanguage.HexLanguageSystemParser.DataContext;
import com.github.kneelawk.utils.InternalParseException;

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
		HexLanguageSystemLexer lexer = new HexLanguageSystemLexer(stream);
		HexLanguageSystemParser parser = new HexLanguageSystemParser(new BufferedTokenStream(lexer));
		HexLanguageBuilderListener builder = new HexLanguageBuilderListener();
		DataContext ctx = parser.data();

		try {
			ParseTreeWalker.DEFAULT.walk(builder, ctx);
		} catch (InternalParseException e) {
			throw e.toLanguageParseException();
		}

		return builder.getData();
	}
}
