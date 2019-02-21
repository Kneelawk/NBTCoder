package com.github.kneelawk.nbtcoder.main;

import com.google.common.primitives.Booleans;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NBTCoderArgs {
	public static final PropertiesConfiguration APPLICATION_PROPERTIES = loadApplicationProperties();
	public static final String VERSION = APPLICATION_PROPERTIES.getString("version");
	public static final String USAGE = loadUsage();

	private static final String DEFAULT_NBT_FILE_PATTERN = APPLICATION_PROPERTIES.getString("pattern.nbtFile.default");
	private static final String DEFAULT_REGION_FILE_PATTERN = APPLICATION_PROPERTIES.getString("pattern.regionFile.default");

	private static PropertiesConfiguration loadApplicationProperties() {
		try {
			PropertiesConfiguration props = new PropertiesConfiguration();
			props.read(new InputStreamReader(NBTCoderArgs.class.getResourceAsStream("application.properties")));
			return props;
		} catch (IOException | ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to load application properties", e);
		}
	}

	private static String loadUsage() {
		try {
			return IOUtils.toString(NBTCoderArgs.class.getResource("usage.txt"), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to load application usage", e);
		}
	}

	private String[] args;

	private OperationMode mode;
	private boolean recursive;
	private boolean stripped;
	private NBTType nbtType;
	private String input;
	private String output;
	private PrintStream verboseStream;
	private String nbtFilePattern;
	private String regionFilePattern;

	public NBTCoderArgs(String[] args) {
		this.args = args;
	}

	public void parse() {
		ParsingState state = new ParsingState();
		state.parse(args);

		if (state.error) {
			System.err.println(USAGE);
			System.exit(-1);
		} else if (state.help) {
			System.out.println(USAGE);
			System.exit(0);
		} else if (state.version) {
			System.out.println(VERSION);
			System.exit(0);
		} else if ((state.humanReadable && state.nbt) || (!state.humanReadable && !state.nbt)) {
			System.err.println("One of --nbt or --human-readable must be specified, but not both.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (state.recursive && state.stripped) {
			System.err.println("-s and -R are not compatible with each other.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (state.region && state.stripped) {
			System.err.println("-s and -r are not compatible with each other.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (state.humanReadable && state.stripped && state.auto) {
			System.err.println("Auto detection is not possible with stripped human-readable input.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (state.recursive
				&& Booleans.countTrue(state.compressed, state.region, state.uncompressed) > 0) {
			System.err.println("-R is not compatible with -c, -r, or -u.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (isStream(state.input) && state.recursive) {
			System.err.println("Stream input is not compatible with the -R option.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (state.nbt && isStream(state.input) && state.auto) {
			System.err.println("Filename based autodetect functionality does not work when reading binary files from" +
					" streams. Try using -A.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (isStream(state.output) && state.recursive) {
			System.err.println("Stream output is not compatible with the -R option.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		recursive = state.recursive;
		stripped = state.stripped;

		if (state.verbose) {
			if (isStream(state.output)) {
				verboseStream = System.err;
			} else {
				verboseStream = System.out;
			}
		} else {
			verboseStream = new PrintStream(new NullOutputStream());
		}

		if (Booleans.countTrue(state.auto, state.autodetectStream, state.compressed, state.region, state.uncompressed) > 1) {
			System.err.println("Only one of -a, -A, -c, -r, or -u may be specified.");
			System.err.println(USAGE);
			System.exit(-1);
		} else if (Booleans.countTrue(state.auto, state.autodetectStream, state.compressed, state.region, state.uncompressed) < 1) {
			nbtType = NBTType.AUTO;
		} else {
			if (state.auto) {
				nbtType = NBTType.AUTO;
			} else if (state.autodetectStream) {
				nbtType = NBTType.AUTODETECT_STREAM;
			} else if (state.compressed) {
				nbtType = NBTType.COMPRESSED;
			} else if (state.region) {
				nbtType = NBTType.REGION;
			} else if (state.uncompressed) {
				nbtType = NBTType.UNCOMPRESSED;
			}
		}

		if (state.humanReadable) {
			mode = OperationMode.HUMAN_TO_NBT;
		} else if (state.nbt) {
			mode = OperationMode.NBT_TO_HUMAN;
		}

		if (state.input == null) {
			input = "-";
		} else {
			if ("-".equals(state.input)) {
				input = state.input;
			} else {
				Path inputPath = Paths.get(state.input).toAbsolutePath();
				if (!Files.exists(inputPath)) {
					System.err.println("Input file: " + state.input + " does not exist.");
					System.exit(-1);
				}
				if (state.recursive) {
					if (!Files.isDirectory(inputPath)) {
						System.err.println(
								"Recursive option is specified but file: " + state.input + " is not a directory.");
						System.exit(-1);
					}
					input = state.input;
				} else {
					input = state.input;
				}
			}
		}

		if (state.output == null) {
			output = "-";
		} else {
			output = state.output;
		}

		if (state.nbtFilePattern == null) {
			nbtFilePattern = DEFAULT_NBT_FILE_PATTERN;
		} else {
			nbtFilePattern = state.nbtFilePattern;
		}

		if (state.regionFilePattern == null) {
			regionFilePattern = DEFAULT_REGION_FILE_PATTERN;
		} else {
			regionFilePattern = state.regionFilePattern;
		}
	}

	public OperationMode getMode() {
		return mode;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public boolean isStripped() {
		return stripped;
	}

	public NBTType getNbtType() {
		return nbtType;
	}

	public String getInput() {
		return input;
	}

	public String getOutput() {
		return output;
	}

	public PrintStream getVerboseStream() {
		return verboseStream;
	}

	public String getNbtFilePattern() {
		return nbtFilePattern;
	}

	public String getRegionFilePattern() {
		return regionFilePattern;
	}

	private class ParsingState {
		boolean error = false;
		boolean help = false;
		boolean version = false;
		boolean recursive = false;
		boolean stripped = false;
		boolean humanReadable = false;
		boolean nbt = false;
		boolean auto = false;
		boolean autodetectStream = false;
		boolean compressed = false;
		boolean region = false;
		boolean uncompressed = false;
		boolean verbose = false;
		String input = null;
		boolean parsingInput = false;
		String output = null;
		boolean parsingOutput = false;
		String nbtFilePattern = null;
		boolean parsingNbtFilePattern = false;
		String regionFilePattern = null;
		boolean parsingRegionFilePattern = false;

		String currentArg;

		void parse(String[] args) {
			for (String arg : args) {
				currentArg = arg;
				if (parsingInput) {
					input = currentArg;
					parsingInput = false;
				} else if (parsingOutput) {
					output = currentArg;
					parsingOutput = false;
				} else if (parsingNbtFilePattern) {
					nbtFilePattern = currentArg;
					parsingNbtFilePattern = false;
				} else if (parsingRegionFilePattern) {
					regionFilePattern = currentArg;
					parsingRegionFilePattern = false;
				} else if (currentArg.startsWith("-")) {
					if (currentArg.startsWith("--")) {
						String argValue = null;
						if (currentArg.contains("=")) {
							argValue = currentArg.substring(currentArg.indexOf('=') + 1);
							currentArg = currentArg.substring(0, currentArg.indexOf('='));
						}
						switch (currentArg) {
						case "--help":
							help = true;
							break;
						case "--version":
							version = true;
							break;
						case "--recursive":
							recursive = true;
							break;
						case "--stripped":
							stripped = true;
							break;
						case "--human-readable":
							humanReadable = true;
							break;
						case "--nbt":
							nbt = true;
							break;
						case "--auto":
							auto = true;
							break;
						case "--autodetect-stream":
							autodetectStream = true;
							break;
						case "--compressed":
							compressed = true;
							break;
						case "--region":
							region = true;
							break;
						case "--uncompressed":
							uncompressed = true;
							break;
						case "--input":
							if (argValue != null) {
								input = argValue;
							} else {
								parsingInput = true;
							}
							break;
						case "--output":
							if (argValue != null) {
								output = argValue;
							} else {
								parsingOutput = true;
							}
							break;
						case "--nbt-file-pattern":
							if (argValue != null) {
								nbtFilePattern = argValue;
							} else {
								parsingNbtFilePattern = true;
							}
							break;
						case "--region-file-pattern":
							if (argValue != null) {
								regionFilePattern = argValue;
							} else {
								parsingRegionFilePattern = true;
							}
							break;
						case "--verbose":
							verbose = true;
							break;
						default:
							System.err.println("Unrecognized option: " + currentArg);
							error = true;
						}
					} else {
						currentArg = currentArg.substring(1);
						parseFlags();
					}
				} else {
					System.err.println("Unrecognized option: " + currentArg);
					error = true;
				}
			}
		}

		void parseFlags() {
			while (!currentArg.isEmpty()) {
				char flag = currentArg.charAt(0);
				currentArg = currentArg.substring(1);

				switch (flag) {
				case 'R':
					recursive = true;
					break;
				case 's':
					stripped = true;
					break;
				case 'h':
					humanReadable = true;
					break;
				case 'n':
					nbt = true;
					break;
				case 'a':
					auto = true;
					break;
				case 'A':
					autodetectStream = true;
					break;
				case 'c':
					compressed = true;
					break;
				case 'r':
					region = true;
					break;
				case 'u':
					uncompressed = true;
					break;
				case 'i':
					if (currentArg.isEmpty()) {
						parsingInput = true;
					} else {
						input = currentArg;
					}
					return;
				case 'o':
					if (currentArg.isEmpty()) {
						parsingOutput = true;
					} else {
						output = currentArg;
					}
					return;
				case 'v':
					verbose = true;
					break;
				default:
					System.err.println("Unrecognized option: -" + flag);
					error = true;
				}
			}
		}
	}

	private boolean isStream(String str) {
		return str == null || "-".equals(str);
	}

	public enum OperationMode {
		NBT_TO_HUMAN, HUMAN_TO_NBT
	}

	public enum NBTType {
		AUTO, AUTODETECT_STREAM, COMPRESSED, REGION, UNCOMPRESSED
	}
}
