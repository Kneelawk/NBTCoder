package com.github.kneelawk.nbtcoder.main;

import com.github.kneelawk.nbtcoder.file.*;
import com.github.kneelawk.nbtcoder.filelanguage.NBTFileLanguageParser;
import com.github.kneelawk.nbtcoder.filelanguage.NBTFileLanguagePrinter;
import com.github.kneelawk.nbtcoder.hexlanguage.HexLanguagePrinter;
import com.github.kneelawk.nbtcoder.main.NBTCoderArgs.NBTType;
import com.github.kneelawk.nbtcoder.main.NBTCoderArgs.OperationMode;
import com.github.kneelawk.nbtcoder.nbt.*;
import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguageParser;
import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguagePrinter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

		NBTFileTypeDetector detector = new NBTFileTypeDetector(Pattern.compile(argsObj.getNbtFilePattern()), Pattern.compile(argsObj.getRegionFilePattern()));

		int exitCode = 0;

		OperationMode operationMode = argsObj.getMode();
		NBTType nbtType = argsObj.getNbtType();

		if (argsObj.isRecursive()) {
			try {
				convertDirectory(Paths.get(argsObj.getInput()), Paths.get(argsObj.getOutput()), operationMode,
						nbtType, filePrinter, fileParser, factory, detector, argsObj.getVerboseStream());
			} catch (IOException e) {
				e.printStackTrace();
				exitCode = -1;
			}
		} else {
			InputStream in = null;
			OutputStream out = null;
			try {
				// initialize the input stream
				in = getInputStream(argsObj);

				// convert the data
				if (OperationMode.HUMAN_TO_NBT.equals(operationMode)) {
					if (argsObj.isStripped()) {
						Tag tag = nbtParser.parse(in);
						if (NBTType.COMPRESSED.equals(nbtType)) {
							// initialize the output stream
							out = getOutputStream(argsObj);

							NBTIO.writeCompressedStream(tag, out);
						} else if (NBTType.UNCOMPRESSED.equals(nbtType)) {
							// initialize the output stream
							out = getOutputStream(argsObj);

							NBTIO.writeStream(tag, out);
						} else {
							System.err.println(
									"Only compressed and uncompressed NBT output formats are compatible with stripped human-readable input.");
							exitCode = -1;
						}
					} else {
						NBTFile file = fileParser.parse(in);
						if (NBTType.AUTO.equals(nbtType)
								|| NBTType.AUTODETECT_STREAM.equals(nbtType)) {
							// initialize the output stream
							out = getOutputStream(argsObj);

							NBTFileIO.writeNBTStream(file, out);
						} else if (file instanceof PartitionedFile) {
							if (NBTType.REGION.equals(nbtType)) {
								// initialize the output stream
								out = getOutputStream(argsObj);

								NBTFileIO.writeNBTStream(file, out);
							} else {
								System.err.println("It is not possible to convert a region file to a non-region file.");
								exitCode = -1;
							}
						} else if (file instanceof SimpleFile) {
							if (NBTType.REGION.equals(nbtType)) {
								System.err.println("It is not possible to convert a non-region file to a region file.");
								exitCode = -1;
							} else if (NBTType.COMPRESSED.equals(nbtType)) {
								// initialize the output stream
								out = getOutputStream(argsObj);

								NBTIO.writeCompressedStream(((SimpleFile) file).getData(), out);
							} else if (NBTType.UNCOMPRESSED.equals(nbtType)) {
								// initialize the output stream
								out = getOutputStream(argsObj);

								NBTIO.writeStream(((SimpleFile) file).getData(), out);
							}
						}
					}
				} else {
					NBTFile file = null;
					String filename = Paths.get(argsObj.getInput()).getFileName().toString();
					if (NBTType.AUTO.equals(nbtType)) {
						file = detector.readFilenameDetectedStream(filename, in, factory);
					} else if (NBTType.AUTODETECT_STREAM.equals(nbtType)) {
						file = NBTFileTypeDetector.readStreamDetectedStream(filename, in, factory);
					} else if (NBTType.REGION.equals(nbtType)) {
						file = NBTFileIO.readRegionNBTStream(filename, in);
					} else if (NBTType.COMPRESSED.equals(nbtType)) {
						file = NBTFileIO.readSimpleNBTStream(filename, in, true, factory);
					} else if (NBTType.UNCOMPRESSED.equals(nbtType)) {
						file = NBTFileIO.readSimpleNBTStream(filename, in, false, factory);
					}

					if (file instanceof SimpleFile && ((SimpleFile) file).getData().getId() == NBTValues.TAG_END) {
						System.err.println(
								"The loaded NBT file consisted of a single TAG_END, this usually indicates that the file was not loaded correctly.");
						exitCode = -1;
					} else {
						if (argsObj.isStripped()) {
							if (file instanceof SimpleFile) {
								// initialize the output stream
								out = getOutputStream(argsObj);

								PrintStream ps = new PrintStream(out);
								ps.print(nbtPrinter.print(((SimpleFile) file).getData()));
								ps.close();
							} else {
								System.err.println(
										"Converting a region file to stripped human-readable NBT is not supported.");
								exitCode = -1;
							}
						} else {
							// initialize the output stream
							out = getOutputStream(argsObj);

							PrintStream ps = new PrintStream(out);
							ps.print(filePrinter.print(file, factory));
							ps.close();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				exitCode = -1;
			} finally {
				// close the input stream if it was opened by the application
				if (in != null && in != System.in) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// close the output stream if it was opened by the application
				if (out != null && out != System.out) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		System.exit(exitCode);
	}

	private static InputStream getInputStream(NBTCoderArgs argsObj) throws IOException {
		if ("-".equals(argsObj.getInput())) {
			return System.in;
		} else {
			return Files.newInputStream(Paths.get(argsObj.getInput()));
		}
	}

	private static OutputStream getOutputStream(NBTCoderArgs argsObj) throws IOException {
		if ("-".equals(argsObj.getOutput())) {
			return System.out;
		} else {
			return Files.newOutputStream(Paths.get(argsObj.getOutput()));
		}
	}

	private static void convertDirectory(Path baseIn, Path baseOut, OperationMode mode, NBTType detectionType,
										 NBTFileLanguagePrinter filePrinter, NBTFileLanguageParser fileParser, TagFactory factory,
										 NBTFileTypeDetector detector, PrintStream verboseStream) throws IOException {
		verboseStream.println("Counting files...");
		long count = Files.walk(baseIn).count();
		verboseStream.println("Converting files...");
		try (Stream<Path> walk = Files.walk(baseIn)) {
			// walk the path tree without the use of lambdas so as to keep this function's
			// context variables
			long current = 1;
			for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
				Path path = it.next();
				Path relative = baseIn.relativize(path);
				Path out = baseOut.resolve(relative);
				verboseStream.println(String.format("%d / %d: %s", current, count, path));
				if (Files.isDirectory(path)) {
					Files.createDirectories(out);
				} else if (Files.isRegularFile(path)) {
					try {
						// attempt to convert the file to/from nbt
						if (OperationMode.HUMAN_TO_NBT.equals(mode)) {
							NBTFile file = fileParser.parse(Files.newInputStream(path));
							Path nOut = baseOut.resolve(file.getFilename());
							NBTFileIO.writeNBTFile(file, nOut.toFile());
						} else {
							NBTFile file;
							if (NBTType.AUTODETECT_STREAM.equals(detectionType)) {
								file = NBTFileTypeDetector.readStreamDetectedFile(relative.toString(), path.toFile(),
										factory);
							} else {
								file = detector.readFilenameDetectedFile(relative.toString(), path.toFile(), factory);
							}
							try (PrintStream outStream = new PrintStream(Files.newOutputStream(out))) {
								outStream.print(filePrinter.print(file, factory));
							}
						}
					} catch (Exception e) {
						// An error occurred while converting the file. Perhaps it was not meant to be
						// converted?
						Files.copy(path, out, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
					}
				}
				current++;
			}
			verboseStream.println("Done.");
		}
	}
}
