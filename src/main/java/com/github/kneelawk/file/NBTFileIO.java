package com.github.kneelawk.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.github.kneelawk.nbt.NBTIO;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagFactory;
import com.github.kneelawk.region.RegionFileIO;

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

	public static void writeRegionNBTStream(RegionFile rf, OutputStream os) throws IOException {
		RegionFileIO.writeRegionFile(os, rf.getPartitions());
	}

	public static void writeRegionNBTFile(RegionFile rf, File file) throws IOException {
		writeRegionNBTStream(rf, new FileOutputStream(file));
	}

	public static NBTFile readAutomaticDetectedStream(String filename, InputStream is, TagFactory factory)
			throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		bis.mark(3072);
		try {
			return readSimpleNBTStream(filename, bis, true, factory);
		} catch (IOException e) {
			try {
				bis.reset();
				return readSimpleNBTStream(filename, bis, false, factory);
			} catch (IOException e1) {
				try {
					bis.reset();
					return readRegionNBTStream(filename, bis);
				} catch (IOException e2) {
					throw new IOException("Unable to detect NBT file type");
				}
			}
		}
	}

	public static NBTFile readAutomaticDetectedFile(String filename, File file, TagFactory factory) throws IOException {
		return readAutomaticDetectedStream(filename, new FileInputStream(file), factory);
	}

	public static NBTFile readAutomaticDetectedFile(File file, TagFactory factory) throws IOException {
		return readAutomaticDetectedFile(file.getName(), file, factory);
	}

	public static void writeNBTStream(NBTFile nbtFile, OutputStream os) throws IOException {
		if (nbtFile instanceof SimpleFile) {
			writeSimpleNBTStream((SimpleFile) nbtFile, os);
		} else if (nbtFile instanceof RegionFile) {
			writeRegionNBTStream((RegionFile) nbtFile, os);
		} else {
			throw new IllegalArgumentException("Unknown NBTFile type: " + nbtFile.getClass());
		}
	}

	public static void writeNBTFile(NBTFile nbtFile, File file) throws IOException {
		if (nbtFile instanceof SimpleFile) {
			writeSimpleNBTFile((SimpleFile) nbtFile, file);
		} else if (nbtFile instanceof RegionFile) {
			writeRegionNBTFile((RegionFile) nbtFile, file);
		} else {
			throw new IllegalArgumentException("Unknown NBTFile type: " + nbtFile.getClass());
		}
	}
}
