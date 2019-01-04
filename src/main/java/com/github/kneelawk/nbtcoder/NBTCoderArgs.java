package com.github.kneelawk.nbtcoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.google.common.primitives.Booleans;

public class NBTCoderArgs {
	public static final Properties APPLICATION_PROPERTIES = loadApplicationProperties();
	public static final String VERSION = APPLICATION_PROPERTIES.getProperty("version");
	public static final String USAGE = loadUsage();

	private static Properties loadApplicationProperties() {
		try {
			Properties props = new Properties();
			props.load(NBTCoderArgs.class.getResourceAsStream("application.properties"));
			return props;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String loadUsage() {
		try {
			return IOUtils.toString(NBTCoderArgs.class.getResource("usage.txt"), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String[] args;

	private OperationMode mode;
	private boolean recursive;
	private boolean stripped;
	private NBTType nbtType;
	private String input;
	private String output;

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
			System.err.println("One of --nbt or --human-readable must be specified, not both.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (state.recursive && state.stripped) {
			System.err.println("-s and -r are not compatible with each other.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (state.recursive
				&& Booleans.countTrue(state.compressed, state.region, state.stripped, state.uncompressed) > 0) {
			System.err.println("-R is not compatible with -c, -r, -s, or -u.");
			System.err.println(USAGE);
			System.exit(-1);
		}

		if (isStream(state.input) && Booleans.countTrue(state.auto, state.recursive) > 0) {
			System.err.println("Stream input is not compatible with the -a or -R option.");
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

		if (Booleans.countTrue(state.auto, state.autoDetectNoSuffix, state.compressed, state.region,
				state.uncompressed) > 1) {
			System.err.println("Only one of -a, -A, -c, -r, or -u may be specified.");
			System.err.println(USAGE);
			System.exit(-1);
		} else if (Booleans.countTrue(state.auto, state.autoDetectNoSuffix, state.compressed, state.region,
				state.uncompressed) < 1) {
			nbtType = NBTType.AUTO;
		} else {
			if (state.auto) {
				nbtType = NBTType.AUTO;
			} else if (state.autoDetectNoSuffix) {
				nbtType = NBTType.AUTO_DETECT_NO_SUFFIX;
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

	private class ParsingState {
		boolean error = false;
		boolean help = false;
		boolean version = false;
		boolean recursive = false;
		boolean stripped = false;
		boolean humanReadable = false;
		boolean nbt = false;
		boolean auto = false;
		boolean autoDetectNoSuffix = false;
		boolean compressed = false;
		boolean region = false;
		boolean uncompressed = false;
		String input = null;
		boolean parsingInput = false;
		String output = null;
		boolean parsingOutput = false;

		String currentArg;

		void parse(String[] args) {
			for (int argIndex = 0; argIndex < args.length; argIndex++) {
				currentArg = args[argIndex];
				if (parsingInput) {
					input = currentArg;
					parsingInput = false;
				} else if (parsingOutput) {
					output = currentArg;
					parsingOutput = false;
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
						case "--auto-detect-no-suffix":
							autoDetectNoSuffix = true;
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
					autoDetectNoSuffix = true;
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

	public static enum OperationMode {
		NBT_TO_HUMAN, HUMAN_TO_NBT
	}

	public static enum NBTType {
		AUTO, AUTO_DETECT_NO_SUFFIX, COMPRESSED, REGION, UNCOMPRESSED
	}
}
