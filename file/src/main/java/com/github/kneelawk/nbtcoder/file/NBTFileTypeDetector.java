package com.github.kneelawk.nbtcoder.file;

import com.github.kneelawk.nbtcoder.file.*;
import com.github.kneelawk.nbtcoder.nbt.NBTValues;
import com.github.kneelawk.nbtcoder.nbt.TagFactory;
import org.apache.commons.io.input.CloseShieldInputStream;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kneelawk on 2/20/19.
 */
public class NBTFileTypeDetector {
	private Pattern nbtFilePattern;
	private Pattern regionFilePattern;

	public NBTFileTypeDetector(Pattern nbtFilePattern, Pattern regionFilePattern) {
		this.nbtFilePattern = nbtFilePattern;
		this.regionFilePattern = regionFilePattern;
	}

	public NBTFileType parseFilename(String filename) {
		Matcher nbtFileMatch = nbtFilePattern.matcher(filename);
		Matcher regionFileMatch = regionFilePattern.matcher(filename);
		if (nbtFileMatch.matches()) {
			return NBTFileType.SIMPLE_FILE;
		} else if (regionFileMatch.matches()) {
			return NBTFileType.REGION_FILE;
		} else {
			return NBTFileType.UNKNOWN;
		}
	}

	public NBTFile readFilenameDetectedStream(String filename, InputStream is, TagFactory factory) throws IOException {
		NBTFileType type = parseFilename(filename);
		switch (type) {
		case SIMPLE_FILE:
			return readStreamDetectedSimpleNBTStream(filename, is, factory);
		case REGION_FILE:
			return NBTFileIO.readRegionNBTStream(filename, is);
		default:
			throw new IOException("Unable to detect NBT file type");
		}
	}

	public NBTFile readFilenameDetectedFile(String filename, File file, TagFactory factory) throws IOException {
		NBTFileType type = parseFilename(filename);
		switch (type) {
		case SIMPLE_FILE:
			return readStreamDetectedSimpleNBTFile(filename, file, factory);
		case REGION_FILE:
			return NBTFileIO.readRegionNBTFile(filename, file);
		default:
			throw new IOException("Unable to detect NBT file type");
		}
	}

	public NBTFile readFilenameDetectedFile(File file, TagFactory factory) throws IOException {
		NBTFileType type = parseFilename(file.getName());
		switch (type) {
		case SIMPLE_FILE:
			return readStreamDetectedSimpleNBTFile(file, factory);
		case REGION_FILE:
			return NBTFileIO.readRegionNBTFile(file);
		default:
			throw new IOException("Unable to detect NBT file type");
		}
	}

	public static NBTFile readStreamDetectedStream(String filename, InputStream is, TagFactory factory)
			throws IOException {
		try (BufferedInputStream bis = new BufferedInputStream(new CloseShieldInputStream(is))) {
			bis.mark(4096);
			try {
				return NBTFileIO.readSimpleNBTStream(filename, bis, true, factory);
			} catch (IOException e) {
				bis.reset();
				SimpleNBTFile file;
				try {
					file = NBTFileIO.readSimpleNBTStream(filename, bis, false, factory);
				} catch (IOException e1) {
					bis.reset();
					try {
						return NBTFileIO.readRegionNBTStream(filename, bis);
					} catch (IOException e2) {
						IOException e3 = new IOException("Unable to detect NBT file type", e2);
						e3.addSuppressed(e1);
						e3.addSuppressed(e);
						throw e3;
					}
				}

				// A valid file shouldn't consist entirely of a TAG_END
				if (file.getData().getId() == NBTValues.TAG_END) {
					bis.reset();
					try {
						return NBTFileIO.readRegionNBTStream(filename, bis);
					} catch (IOException e2) {
						IOException e3 = new IOException("Unable to detect NBT file type", e2);
						e3.addSuppressed(e);
						throw e3;
					}
				} else {
					return file;
				}
			}
		}
	}

	public static NBTFile readStreamDetectedFile(String filename, File file, TagFactory factory) throws IOException {
		try {
			return NBTFileIO.readSimpleNBTFile(filename, file, true, factory);
		} catch (IOException e) {
			try {
				return NBTFileIO.readRegionNBTFile(filename, file);
			} catch (IOException e1) {
				try {
					SimpleFile nbtFile = NBTFileIO.readSimpleNBTFile(filename, file, false, factory);
					if (nbtFile.getData().getId() == NBTValues.TAG_END) {
						throw new IOException("File consists of a single TAG_END");
					}
					return nbtFile;
				} catch (IOException e2) {
					IOException e3 = new IOException("Unable to detect NBT file type", e2);
					e3.addSuppressed(e1);
					e3.addSuppressed(e);
					throw e3;
				}
			}
		}
	}

	public static NBTFile readStreamDetectedFile(File file, TagFactory factory) throws IOException {
		return readStreamDetectedFile(file.getName(), file, factory);
	}

	public static SimpleNBTFile readStreamDetectedSimpleNBTStream(String filename, InputStream is,
														   TagFactory factory) throws IOException {
		try (BufferedInputStream bis = new BufferedInputStream(new CloseShieldInputStream(is))) {
			bis.mark(1024);
			try {
				return NBTFileIO.readSimpleNBTStream(filename, bis, true, factory);
			} catch (IOException e) {
				bis.reset();
				try {
					SimpleNBTFile nbtFile = NBTFileIO.readSimpleNBTStream(filename, bis, false, factory);
					if (nbtFile.getData().getId() == NBTValues.TAG_END) {
						throw new IOException("File consists of a single TAG_END");
					}
					return nbtFile;
				} catch (IOException e1) {
					IOException e2 = new IOException("Unable to detect NBT file type", e1);
					e2.addSuppressed(e);
					throw e;
				}
			}
		}
	}

	public static SimpleNBTFile readStreamDetectedSimpleNBTFile(String filename, File file, TagFactory factory)
			throws IOException {
		try {
			return NBTFileIO.readSimpleNBTFile(filename, file, true, factory);
		} catch (IOException e) {
			try {
				SimpleNBTFile nbtFile = NBTFileIO.readSimpleNBTFile(filename, file, false, factory);
				if (nbtFile.getData().getId() == NBTValues.TAG_END) {
					throw new IOException("File consists of a single TAG_END");
				}
				return nbtFile;
			} catch (IOException e1) {
				IOException e2 = new IOException("Unable to detect NBT file type", e1);
				e2.addSuppressed(e);
				throw e2;
			}
		}
	}

	public static SimpleNBTFile readStreamDetectedSimpleNBTFile(File file, TagFactory factory) throws IOException {
		return readStreamDetectedSimpleNBTFile(file.getName(), file, factory);
	}

	public enum NBTFileType {
		SIMPLE_FILE, REGION_FILE, UNKNOWN
	}
}
