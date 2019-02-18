package com.github.kneelawk.nbtcoder.nbtlanguage;

import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguageSystemParser.NbtFileContext;
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
		try {
			NBTLanguageSystemLexer lex = new NBTLanguageSystemLexer(cs);
			lex.removeErrorListeners();
			NBTLanguageSystemParser parser = new NBTLanguageSystemParser(new BufferedTokenStream(lex));
			parser.removeErrorListeners();
			parser.setErrorHandler(new InternalExceptionErrorStrategy());
			NBTLanguageBuilderListener builder = new NBTLanguageBuilderListener();
			NbtFileContext tree = parser.nbtFile();

			ParseTreeWalker.DEFAULT.walk(builder, tree);

			return builder.getRoot();
		} catch (InternalParseException e) {
			throw e.toLanguageParseException();
		} catch (IllegalStateException e) {
			throw new LanguageParseException(e, null);
		}
	}
}
