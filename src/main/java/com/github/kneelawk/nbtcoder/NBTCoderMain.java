package com.github.kneelawk.nbtcoder;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.stream.Stream;

import com.github.kneelawk.file.NBTFile;
import com.github.kneelawk.file.NBTFileIO;
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

		if (argsObj.isRecursive()) {
			try {
				convertDirectory(Paths.get(argsObj.getInput()), Paths.get(argsObj.getOutput()), argsObj.getMode(),
						filePrinter, fileParser, factory, argsObj.getVerboseStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}

	private static void convertDirectory(Path baseIn, Path baseOut, OperationMode mode,
			NBTFileLanguagePrinter filePrinter, NBTFileLanguageParser fileParser, TagFactory factory,
			PrintStream verboseStream) throws IOException {
		verboseStream.println("Counting files...");
		long count = Files.walk(baseIn).count();
		verboseStream.println("Converting files...");
		try (Stream<Path> walk = Files.walk(baseIn)) {
			long current = 1;
			for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
				Path path = it.next();
				Path relative = baseIn.relativize(path);
				Path out = baseOut.resolve(relative);
				verboseStream.println(String.format("%d / %d: %s", current, count, path));
				if (Files.isDirectory(path)) {
					Files.createDirectories(out);
				} else if (Files.isRegularFile(path)) {
					try {
						if (OperationMode.HUMAN_TO_NBT.equals(mode)) {
							NBTFile file = fileParser.parse(Files.newInputStream(path, StandardOpenOption.READ));
							Path nOut = baseOut.resolve(file.getFilename());
							NBTFileIO.writeNBTFile(file, nOut.toFile());
						} else {
							NBTFile file = NBTFileIO.readAutomaticDetectedFile(relative.toString(), path.toFile(),
									factory);
							try (PrintStream outStream = new PrintStream(
									Files.newOutputStream(out, StandardOpenOption.WRITE, StandardOpenOption.CREATE))) {
								outStream.print(filePrinter.print(file, factory));
							}
						}
					} catch (Exception e) {
						Files.copy(path, out, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
					}
				}
				current++;
			}
			verboseStream.println("Done.");
		}
	}
}
