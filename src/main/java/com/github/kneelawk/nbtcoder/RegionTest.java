package com.github.kneelawk.nbtcoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.github.kneelawk.file.NBTFile;
import com.github.kneelawk.file.NBTFileIO;
import com.github.kneelawk.filelanguage.NBTFileLanguagePrinter;
import com.github.kneelawk.hexlanguage.HexLanguagePrinter;
import com.github.kneelawk.nbt.DefaultTagFactory;
import com.github.kneelawk.nbt.TagFactory;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;

public class RegionTest {

	public static final String FILENAME = "r.0.0.mca";
	public static final String IN_FILE = "../" + FILENAME;
	public static final String OUT_FILE = "../" + FILENAME + ".txt";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TagFactory factory = new DefaultTagFactory();
		NBTLanguagePrinter nbtPrinter = new NBTLanguagePrinter.Builder().build();
		HexLanguagePrinter hexPrinter = new HexLanguagePrinter.Builder().build();
		NBTFileLanguagePrinter printer = new NBTFileLanguagePrinter(nbtPrinter, hexPrinter);

		System.out.println("Loading...");
		NBTFile nbtFile = NBTFileIO.readAutomaticDetectedStream(FILENAME, new FileInputStream(IN_FILE), factory);

		PrintStream out = new PrintStream(new FileOutputStream(OUT_FILE));

		System.out.println("Printing...");
		out.println(printer.print(nbtFile, factory));

		System.out.println("Done.");
		out.close();
	}

}
