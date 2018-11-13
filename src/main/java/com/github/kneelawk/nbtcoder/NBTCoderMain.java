package com.github.kneelawk.nbtcoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.kneelawk.nbt.Tag;

public class NBTCoderMain {

	public static void main(String[] args) {
		NBTLanguageParser parser = new NBTLanguageParser();
		try (FileInputStream fis = new FileInputStream(new File("test-input.txt"))) {
			Tag tag = parser.parse(fis);
			System.out.println("Tag name: " + tag.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
