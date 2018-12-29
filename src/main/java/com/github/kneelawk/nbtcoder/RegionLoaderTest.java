package com.github.kneelawk.nbtcoder;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.github.kneelawk.file.RegionNBTFile;
import com.github.kneelawk.filelanguage.NBTFileLanguageParser;
import com.github.kneelawk.nbtlanguage.NBTLanguageParser;
import com.github.kneelawk.region.RegionFileIO;

public class RegionLoaderTest {

	public static void main(String[] args) throws IOException {
		NBTLanguageParser nbtParser = new NBTLanguageParser();
		NBTFileLanguageParser parser = new NBTFileLanguageParser(nbtParser);

		System.out.println("Parsing...");
		RegionNBTFile file = (RegionNBTFile) parser.parse(new FileReader("../r.-1.-2.mca.txt"));

		System.out.println("Writing...");
		RegionFileIO.writeRegionFile(new FileOutputStream("../r.-1.-2.mca.2"), file.getPartitions());

		System.out.println("Done.");
	}

}
