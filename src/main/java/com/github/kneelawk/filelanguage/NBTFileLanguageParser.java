package com.github.kneelawk.filelanguage;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.github.kneelawk.file.NBTFile;
import com.github.kneelawk.filelanguage.NBTFileLanguageSystemParser.NbtFileContext;
import com.github.kneelawk.nbtlanguage.NBTLanguageParser;
import com.github.kneelawk.utils.InternalExceptionErrorStrategy;
import com.github.kneelawk.utils.InternalParseException;
import com.github.kneelawk.utils.LanguageParseException;

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
