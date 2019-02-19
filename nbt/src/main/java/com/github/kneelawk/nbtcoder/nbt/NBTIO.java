package com.github.kneelawk.nbtcoder.nbt;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.output.CloseShieldOutputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTIO {
	public static Tag read(DataInput in, TagFactory factory) throws IOException {
		return NBTInternalUtils.readNamedTag(in, factory);
	}

	public static TagCompound readCompound(DataInput in, TagFactory factory) throws IOException {
		Tag tag = NBTInternalUtils.readNamedTag(in, factory);
		if (tag.getId() != NBTValues.TAG_COMPOUND) {
			throw new IOException("The root tag is not a named compound tag");
		}
		return (TagCompound) tag;
	}

	public static void write(Tag tag, DataOutput out) throws IOException {
		NBTInternalUtils.writeNamedTag(tag, out);
	}

	public static Tag readStream(InputStream is, TagFactory factory) throws IOException {
		try (DataInputStream in = new DataInputStream(new CloseShieldInputStream(is))) {
			return read(in, factory);
		}
	}

	public static TagCompound readStreamCompound(InputStream is, TagFactory factory) throws IOException {
		try (DataInputStream in = new DataInputStream(new CloseShieldInputStream(is))) {
			return readCompound(in, factory);
		}
	}

	public static void writeStream(Tag tag, OutputStream os) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new CloseShieldOutputStream(os))) {
			write(tag, out);
		}
	}

	public static Tag readCompressedStream(InputStream is, TagFactory factory) throws IOException {
		try (DataInputStream in = new DataInputStream(new GZIPInputStream(new CloseShieldInputStream(is)))) {
			return read(in, factory);
		}
	}

	public static TagCompound readCompressedStreamCompound(InputStream is, TagFactory factory) throws IOException {
		try (DataInputStream in = new DataInputStream(new GZIPInputStream(new CloseShieldInputStream(is)))) {
			return readCompound(in, factory);
		}
	}

	public static void writeCompressedStream(Tag tag, OutputStream os) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new CloseShieldOutputStream(os)))) {
			write(tag, out);
		}
	}

	public static Tag readFile(File file, TagFactory factory) throws IOException {
		try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
			return read(in, factory);
		}
	}

	public static TagCompound readFileCompound(File file, TagFactory factory) throws IOException {
		try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
			return readCompound(in, factory);
		}
	}

	public static void writeFile(Tag tag, File file) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
			write(tag, out);
		}
	}

	public static Tag readCompressedFile(File file, TagFactory factory) throws IOException {
		try (DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(file)))) {
			return read(in, factory);
		}
	}

	public static TagCompound readCompressedFileCompound(File file, TagFactory factory) throws IOException {
		try (DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(file)))) {
			return readCompound(in, factory);
		}
	}

	public static void writeCompressedFile(Tag tag, File file) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)))) {
			write(tag, out);
		}
	}
}
