package com.github.kneelawk.nbtlanguage;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbtlanguage.NBTLanguageSystemParser.NbtFileContext;

public class NBTLanguageParser {
	public Tag parse(String str) throws IOException {
		return parse(CharStreams.fromString(str));
	}

	public Tag parse(Reader reader) throws IOException {
		return parse(CharStreams.fromReader(reader));
	}

	public Tag parse(InputStream is) throws IOException {
		return parse(CharStreams.fromStream(is));
	}

	public Tag parse(CharStream cs) throws IOException {
		NBTLanguageSystemLexer lex = new NBTLanguageSystemLexer(cs);
		NBTLanguageSystemParser parser = new NBTLanguageSystemParser(new BufferedTokenStream(lex));
		NBTLanguageBuilderListener builder = new NBTLanguageBuilderListener();
		NbtFileContext tree = parser.nbtFile();
		ParseTreeWalker.DEFAULT.walk(builder, tree);

		return builder.getRoot();
	}
}
