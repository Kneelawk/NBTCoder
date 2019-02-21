package com.github.kneelawk.nbtcoder.file;

import com.github.kneelawk.nbtcoder.nbt.NBTIO;
import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbt.TagFactory;
import com.github.kneelawk.nbtcoder.region.RegionFileIO;

import java.io.*;

public class NBTFileIO {
	public static SimpleNBTFile readSimpleNBTStream(String filename, InputStream is, boolean compressed,
													TagFactory factory) throws IOException {
		Tag tag;
		if (compressed) {
			tag = NBTIO.readCompressedStream(is, factory);
		} else {
			tag = NBTIO.readStream(is, factory);
		}
		return new SimpleNBTFile(filename, tag, compressed);
	}

	public static SimpleNBTFile readSimpleNBTFile(File file, boolean compressed, TagFactory factory)
			throws IOException {
		return readSimpleNBTFile(file.getName(), file, compressed, factory);
	}

	public static SimpleNBTFile readSimpleNBTFile(String filename, File file, boolean compressed, TagFactory factory)
			throws IOException {
		Tag tag;
		if (compressed) {
			tag = NBTIO.readCompressedFile(file, factory);
		} else {
			tag = NBTIO.readFile(file, factory);
		}
		return new SimpleNBTFile(filename, tag, compressed);
	}

	public static RegionNBTFile readRegionNBTStream(String filename, InputStream is) throws IOException {
		return new RegionNBTFile(filename, RegionFileIO.readRegionFile(is));
	}

	public static RegionNBTFile readRegionNBTFile(String filename, File file) throws IOException {
		return readRegionNBTStream(filename, new FileInputStream(file));
	}

	public static RegionNBTFile readRegionNBTFile(File file) throws IOException {
		return readRegionNBTFile(file.getName(), file);
	}

	public static void writeSimpleNBTStream(SimpleFile sf, OutputStream os) throws IOException {
		if (sf.isCompressed()) {
			NBTIO.writeCompressedStream(sf.getData(), os);
		} else {
			NBTIO.writeStream(sf.getData(), os);
		}
	}

	public static void writeSimpleNBTFile(SimpleFile sf, File file) throws IOException {
		if (sf.isCompressed()) {
			NBTIO.writeCompressedFile(sf.getData(), file);
		} else {
			NBTIO.writeFile(sf.getData(), file);
		}
	}

	public static void writeRegionNBTStream(PartitionedFile rf, OutputStream os) throws IOException {
		RegionFileIO.writeRegionFile(os, rf.getRegionFile());
	}

	public static void writeRegionNBTFile(PartitionedFile rf, File file) throws IOException {
		writeRegionNBTStream(rf, new FileOutputStream(file));
	}

	public static void writeNBTStream(NBTFile nbtFile, OutputStream os) throws IOException {
		if (nbtFile instanceof SimpleFile) {
			writeSimpleNBTStream((SimpleFile) nbtFile, os);
		} else if (nbtFile instanceof PartitionedFile) {
			writeRegionNBTStream((PartitionedFile) nbtFile, os);
		} else {
			throw new IllegalArgumentException("Unknown NBTFile type: " + nbtFile.getClass());
		}
	}

	public static void writeNBTFile(NBTFile nbtFile, File file) throws IOException {
		if (nbtFile instanceof SimpleFile) {
			writeSimpleNBTFile((SimpleFile) nbtFile, file);
		} else if (nbtFile instanceof PartitionedFile) {
			writeRegionNBTFile((PartitionedFile) nbtFile, file);
		} else {
			throw new IllegalArgumentException("Unknown NBTFile type: " + nbtFile.getClass());
		}
	}
}
