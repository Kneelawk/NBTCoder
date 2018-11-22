package com.github.kneelawk.nbtcoder;

import it.jnrpe.yaclp.CommandLine;
import it.jnrpe.yaclp.HelpFormatter;
import it.jnrpe.yaclp.IOption;
import it.jnrpe.yaclp.OptionBuilder;
import it.jnrpe.yaclp.Parser;
import it.jnrpe.yaclp.ParserBuilder;
import it.jnrpe.yaclp.ParsingException;

public class NBTCoderMain {
	public static final String VERSION = "0.0.1-SNAPSHOT";

	public static void main(String[] args) {
		IOption modeOption = OptionBuilder.forMutuallyExclusiveOption()
				.withOptions(
						OptionBuilder.forOption("-h", "--human-readable")
								.description("Input file is in human-readable format").build(),
						OptionBuilder.forOption("-n", "--nbt").description("Input file is in NBT format").build())
				.build();

		Parser p = ParserBuilder.forOptionsBasedCli().withOption(
				OptionBuilder.forMutuallyExclusiveOption()
						.withOptions(OptionBuilder.forOption("--help").description("Print this help message").build(),
								OptionBuilder.forOption("--version").description("Print this program's version")
										.build(),
								modeOption)
						.description("Operation mode").mandatory(true).build(),
				OptionBuilder.forOption("-i", "--in-file").requires(modeOption)
						.description("Input file or '-' for stdin").build(),
				OptionBuilder.forOption("-o", "--out-file").requires(modeOption)
						.description("Output file or '-' for stdout").build())
				.build();

		HelpFormatter formatter = new HelpFormatter("NBTCoder", p);

		CommandLine cli = null;
		try {
			cli = p.parse(args);
		} catch (ParsingException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
			formatter.printUsage(System.err);
			formatter.printHelp(System.err);
			System.exit(-1);
		}

		if (cli.hasCommand("--help")) {
			formatter.printUsage(System.out);
			formatter.printHelp(System.out);
			System.exit(0);
		}

		if (cli.hasCommand("--version")) {
			System.out.println("NBTCoder version " + VERSION);
		}
	}
}
