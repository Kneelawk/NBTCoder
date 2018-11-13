package com.github.kneelawk.nbtcoder;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStreams;

import com.github.kneelawk.nbt.Tag;

public class NBTLanguageParser {
	public Tag parse(InputStream is) throws IOException {
		NBTCoderLexer lex = new NBTCoderLexer(CharStreams.fromStream(is));
		NBTCoderParser parser = new NBTCoderParser(new BufferedTokenStream(lex));
		NBTCoderBuilderListener builder = new NBTCoderBuilderListener();
		parser.addParseListener(builder);

		parser.nbtFile();

		return builder.getRoot();
	}
}
