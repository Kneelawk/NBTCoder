package com.github.kneelawk.nbtcoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.github.kneelawk.filelanguage.NBTFileLanguageParser;
import com.github.kneelawk.filelanguage.NBTFileLanguagePrinter;
import com.github.kneelawk.hexlanguage.HexLanguagePrinter;
import com.github.kneelawk.nbt.DefaultTagFactory;
import com.github.kneelawk.nbt.TagFactory;
import com.github.kneelawk.nbtcoder.NBTCoderArgs.OperationMode;
import com.github.kneelawk.nbtlanguage.NBTLanguageParser;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;

public class NBTCoderMain {

	public static void main(String[] args) {
		NBTCoderArgs argsObj = new NBTCoderArgs(args);
		argsObj.parse();

		TagFactory factory = new DefaultTagFactory();

		NBTLanguagePrinter nbtPrinter = new NBTLanguagePrinter.Builder().build();
		HexLanguagePrinter hexPrinter = new HexLanguagePrinter.Builder().build();
		NBTFileLanguagePrinter filePrinter = new NBTFileLanguagePrinter(nbtPrinter, hexPrinter);

		NBTLanguageParser nbtParser = new NBTLanguageParser();
		NBTFileLanguageParser fileParser = new NBTFileLanguageParser(nbtParser);

	}

	private static void convertDirectory(Path baseIn, Path baseOut, OperationMode mode,
			NBTFileLanguagePrinter filePrinter, NBTFileLanguageParser fileParser, TagFactory factory)
			throws IOException {
		Stream<Path> walk = Files.walk(baseIn);
		long count = walk.count();
	}
}
