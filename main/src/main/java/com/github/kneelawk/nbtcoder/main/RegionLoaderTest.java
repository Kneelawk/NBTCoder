package com.github.kneelawk.nbtcoder.main;

import com.github.kneelawk.nbtcoder.file.NBTFile;
import com.github.kneelawk.nbtcoder.file.NBTFileIO;
import com.github.kneelawk.nbtcoder.filelanguage.NBTFileLanguageParser;
import com.github.kneelawk.nbtcoder.nbtlanguage.NBTLanguageParser;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class RegionLoaderTest {

	public static void main(String[] args) throws IOException {
		NBTLanguageParser nbtParser = new NBTLanguageParser();
		NBTFileLanguageParser parser = new NBTFileLanguageParser(nbtParser);

		System.out.println("Parsing...");
		NBTFile file = parser.parse(new FileReader("../r.-1.-2.mca.txt"));

		System.out.println("Writing...");
		NBTFileIO.writeNBTStream(file, new FileOutputStream("../r.-1.-2.mca.2"));

		System.out.println("Done.");
	}

}
