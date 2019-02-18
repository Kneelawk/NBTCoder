package com.github.kneelawk.nbtcoder.filelanguage;

import com.github.kneelawk.nbtcoder.file.NBTFile;
import com.github.kneelawk.nbtcoder.filelanguage.NBTFileLanguageSystemParser.NbtFileContext;
import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguageParser;
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

public class NBTFileLanguageParser {
	private NBTLanguageParser nbtParser;

	public NBTFileLanguageParser(NBTLanguageParser nbtParser) {
		this.nbtParser = nbtParser;
	}

	public NBTFile parse(String string) throws IOException {
		return parse(CharStreams.fromString(string));
	}

	public NBTFile parse(Reader reader) throws IOException {
		return parse(CharStreams.fromReader(reader));
	}

	public NBTFile parse(InputStream stream) throws IOException {
		return parse(CharStreams.fromStream(stream));
	}

	public NBTFile parse(CharStream stream) throws IOException {
		try {
			NBTFileLanguageSystemLexer lexer = new NBTFileLanguageSystemLexer(stream);
			lexer.removeErrorListeners();
			NBTFileLanguageSystemParser parser = new NBTFileLanguageSystemParser(new BufferedTokenStream(lexer));
			parser.removeErrorListeners();
			parser.setErrorHandler(new InternalExceptionErrorStrategy());
			NBTFileLanguageBuilderListener builder = new NBTFileLanguageBuilderListener(nbtParser);
			NbtFileContext tree = parser.nbtFile();

			ParseTreeWalker.DEFAULT.walk(builder, tree);

			return builder.getFile();
		} catch (InternalParseException e) {
			throw e.toLanguageParseException();
		} catch (IllegalStateException e) {
			throw new LanguageParseException(e, null);
		}
	}
}
