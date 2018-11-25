package com.github.kneelawk.region;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import com.github.kneelawk.nbt.NBTIO;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagFactory;

public class Chunk {
	private byte compressionType;
	private int x;
	private int z;
	private byte[] data;

	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
		data = new byte[0];
	}

	public Chunk(int x, int z, byte[] data) {
		this.x = x;
		this.z = z;
		this.data = data;
	}

	public Chunk(byte compressionType, int x, int z) {
		this.compressionType = compressionType;
		this.x = x;
		this.z = z;
		data = new byte[0];
	}

	public Chunk(byte compressionType, int x, int z, byte[] data) {
		this.compressionType = compressionType;
		this.x = x;
		this.z = z;
		this.data = data;
	}

	public Tag readData(TagFactory factory) throws IOException {
		DataInputStream in = null;

		switch (compressionType) {
		case RegionValues.DEFLATE_COMPRESSION:
			in = new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(data))));
			break;
		case RegionValues.GZIP_COMPRESSION:
			in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(data))));
			break;
		default:
			throw new IOException("Unknown chunk compression type: " + compressionType);
		}

		return NBTIO.read(in, factory);
	}

	public void writeData(Tag tag) throws IOException {
		ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
		DataOutputStream out = null;

		switch (compressionType) {
		case RegionValues.DEFLATE_COMPRESSION:
			out = new DataOutputStream(new DeflaterOutputStream(arrayOut));
			break;
		case RegionValues.GZIP_COMPRESSION:
			out = new DataOutputStream(new GZIPOutputStream(arrayOut));
			break;
		default:
			throw new IOException("Unknown chunk compression type: " + compressionType);
		}

		NBTIO.write(tag, out);

		data = arrayOut.toByteArray();
	}

	public byte getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(byte compressionType) {
		this.compressionType = compressionType;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void clearData() {
		data = new byte[0];
	}

	public int size() {
		return data.length;
	}
}
