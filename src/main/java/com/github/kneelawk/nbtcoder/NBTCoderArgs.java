package com.github.kneelawk.nbtcoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

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
	private boolean compressed;
	private InputStream input;
	private OutputStream output;

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

		if (state.humanReadable) {
			mode = OperationMode.HUMAN_TO_NBT;
		} else if (state.nbt) {
			mode = OperationMode.NBT_TO_HUMAN;
		}

		compressed = state.compressed;

		if (state.input == null) {
			input = System.in;
		} else {
			if ("-".equals(state.input)) {
				input = System.in;
			} else {
				File inputFile = new File(state.input);
				if (!inputFile.exists()) {
					System.err.println("Input file: " + state.input + " does not exist.");
					System.exit(-1);
				}
				try {
					input = new FileInputStream(inputFile);
				} catch (FileNotFoundException e) {
					System.err.println("Error opening input file: " + state.input);
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}

		if (state.output == null) {
			output = System.out;
		} else {
			if ("-".equals(state.output)) {
				output = System.out;
			} else {
				try {
					output = new FileOutputStream(new File(state.output));
				} catch (FileNotFoundException e) {
					System.err.println("Error opening output file: " + state.output);
					System.err.println("It is likely that a parent directory for this file does not exist.");
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	public OperationMode getMode() {
		return mode;
	}

	public boolean isCompressed() {
		return compressed;
	}

	public InputStream getInput() {
		return input;
	}

	public OutputStream getOutput() {
		return output;
	}

	private class ParsingState {
		boolean error = false;
		boolean help = false;
		boolean version = false;
		boolean humanReadable = false;
		boolean nbt = false;
		boolean compressed = false;
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
						switch (currentArg) {
						case "--help":
							help = true;
							break;
						case "--version":
							version = true;
							break;
						case "--human-readable":
							humanReadable = true;
							break;
						case "--nbt":
							nbt = true;
							break;
						case "--compressed":
							compressed = true;
							break;
						case "--input":
							parsingInput = true;
							break;
						case "--output":
							parsingOutput = true;
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
				case 'h':
					humanReadable = true;
					break;
				case 'n':
					nbt = true;
					break;
				case 'c':
					compressed = true;
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

	public static enum OperationMode {
		NBT_TO_HUMAN, HUMAN_TO_NBT
	}
}
