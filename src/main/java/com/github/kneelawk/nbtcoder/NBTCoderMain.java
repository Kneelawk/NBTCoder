package com.github.kneelawk.nbtcoder;

import java.io.IOException;
import java.io.PrintStream;

import com.github.kneelawk.nbt.DefaultTagFactory;
import com.github.kneelawk.nbt.NBTIO;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagFactory;

public class NBTCoderMain {
	public static final String VERSION = "0.0.1-SNAPSHOT";

	public static void main(String[] args) {
		NBTCoderArgs argsObj = new NBTCoderArgs(args);
		argsObj.parse();

		TagFactory factory = new DefaultTagFactory();

		switch (argsObj.getMode()) {
		case NBT_TO_HUMAN: {
			Tag tag = null;
			if (argsObj.isCompressed()) {
				try {
					tag = NBTIO.readCompressedStream(argsObj.getInput(), factory);
				} catch (IOException e) {
					System.err.println("Unable to parse this NBT file.");
					e.printStackTrace();
					System.exit(-1);
				}
			} else {
				try {
					tag = NBTIO.readStream(argsObj.getInput(), factory);
				} catch (IOException e) {
					System.err.println("Unable to parse this NBT file.");
					e.printStackTrace();
					System.exit(-1);
				}
			}

			NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder().build();
			String str = printer.print(tag);

			PrintStream out = new PrintStream(argsObj.getOutput());
			out.println(str);
			out.close();
			break;
		}
		case HUMAN_TO_NBT:
			NBTLanguageParser parser = new NBTLanguageParser();
			Tag tag = null;
			try {
				tag = parser.parse(argsObj.getInput());
			} catch (IOException e) {
				System.err.println("Unable to parse.");
				e.printStackTrace();
				System.exit(-1);
			}

			if (argsObj.isCompressed()) {
				try {
					NBTIO.writeCompressedStream(tag, argsObj.getOutput());
				} catch (IOException e) {
					System.err.println("Unable to write this NBT file.");
					e.printStackTrace();
					System.exit(-1);
				}
			} else {
				try {
					NBTIO.writeStream(tag, argsObj.getOutput());
				} catch (IOException e) {
					System.err.println("Unable to write this NBT file.");
					e.printStackTrace();
					System.exit(-1);
				}
			}
			break;
		}
	}
}
