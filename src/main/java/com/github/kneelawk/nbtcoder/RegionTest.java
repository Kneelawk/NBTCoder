package com.github.kneelawk.nbtcoder;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.kneelawk.nbt.DefaultTagFactory;
import com.github.kneelawk.nbt.TagFactory;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;

public class RegionTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TagFactory factory = new DefaultTagFactory();
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder().build();
	}

}
