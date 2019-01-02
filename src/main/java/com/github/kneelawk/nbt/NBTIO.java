package com.github.kneelawk.nbt;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		return read(new DataInputStream(is), factory);
	}

	public static TagCompound readStreamCompound(InputStream is, TagFactory factory) throws IOException {
		return readCompound(new DataInputStream(is), factory);
	}

	public static void writeStream(Tag tag, OutputStream os) throws IOException {
		write(tag, new DataOutputStream(os));
	}

	public static Tag readCompressedStream(InputStream is, TagFactory factory) throws IOException {
		return read(new DataInputStream(new GZIPInputStream(is)), factory);
	}

	public static TagCompound readCompressedStreamCompound(InputStream is, TagFactory factory) throws IOException {
		return readCompound(new DataInputStream(new GZIPInputStream(is)), factory);
	}

	public static void writeCompressedStream(Tag tag, OutputStream os) throws IOException {
		write(tag, new DataOutputStream(new GZIPOutputStream(os)));
	}

	public static Tag readFile(File file, TagFactory factory) throws IOException {
		return read(new DataInputStream(new FileInputStream(file)), factory);
	}

	public static TagCompound readFileCompound(File file, TagFactory factory) throws IOException {
		return readCompound(new DataInputStream(new FileInputStream(file)), factory);
	}

	public static void writeFile(Tag tag, File file) throws IOException {
		write(tag, new DataOutputStream(new FileOutputStream(file)));
	}

	public static Tag readCompressedFile(File file, TagFactory factory) throws IOException {
		return read(new DataInputStream(new GZIPInputStream(new FileInputStream(file))), factory);
	}

	public static TagCompound readCompressedFileCompound(File file, TagFactory factory) throws IOException {
		return readCompound(new DataInputStream(new GZIPInputStream(new FileInputStream(file))), factory);
	}

	public static void writeCompressedFile(Tag tag, File file) throws IOException {
		write(tag, new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file))));
	}
}
