package com.github.kneelawk.filelanguage;

import java.util.Properties;

import com.github.kneelawk.file.NBTFile;
import com.github.kneelawk.nbt.Tag;

public class NBTFileLanguagePrinter {
	public String print(NBTFile file) {
		StringBuilder sb = new StringBuilder();
		printNBTFile(file, sb);
		return sb.toString();
	}

	private void printNBTFile(NBTFile file, StringBuilder sb) {

	}

	private void printProperties(Properties props, StringBuilder sb) {

	}

	private void printData(Tag data, StringBuilder sb) {

	}
}
