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

public class ChunkPartition implements Partition {
	private static final int PADDING_SIZE = 64;
	private static final byte[] PADDING = new byte[PADDING_SIZE];

	public static class Builder {
		private byte compressionType;
		private int x;
		private int z;
		private byte[] data;
		private int timestamp;

		public Builder() {
		}

		public Builder(byte compressionType, int x, int z, byte[] data, int timestamp) {
			this.compressionType = compressionType;
			this.x = x;
			this.z = z;
			this.data = data;
			this.timestamp = timestamp;
		}

		public ChunkPartition build() {
			return new ChunkPartition(compressionType, x, z, data, timestamp);
		}

		public byte getCompressionType() {
			return compressionType;
		}

		public Builder setCompressionType(byte compressionType) {
			this.compressionType = compressionType;
			return this;
		}

		public int getX() {
			return x;
		}

		public Builder setX(int x) {
			this.x = x;
			return this;
		}

		public int getZ() {
			return z;
		}

		public Builder setZ(int z) {
			this.z = z;
			return this;
		}

		public byte[] getData() {
			return data;
		}

		public Builder setData(byte[] data) {
			this.data = data;
			return this;
		}

		public int getTimestamp() {
			return timestamp;
		}

		public Builder setTimestamp(int timestamp) {
			this.timestamp = timestamp;
			return this;
		}
	}

	private byte compressionType;
	private int x;
	private int z;
	private byte[] data;
	private int timestamp;

	public ChunkPartition(byte compressionType, int x, int z, byte[] data, int timestamp) {
		this.compressionType = compressionType;
		this.x = x;
		this.z = z;
		this.data = data;
		this.timestamp = timestamp;
	}

	public Tag readTag(TagFactory factory) throws IOException {
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

	public void writeTag(Tag tag) throws IOException {
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

		out.close();

		data = arrayOut.toByteArray();
	}

	@Override
	public int getSectorCount() {
		return (size() - 1) / RegionValues.BYTES_PER_SECTOR + 1;
	}

	@Override
	public void writeMetadata(int sectorNum, int[] offsets, int[] timestamps) {
		int location = z * 32 + x;
		offsets[location] = ((sectorNum << 8) & 0xFFFFFF00) | (getSectorCount() & 0xFF);
		timestamps[location] = timestamp;
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(data.length + 1);
		output.writeByte(compressionType);
		output.write(data);

		// pad the bytes to the nearest sector boundary
		int remaining = RegionValues.BYTES_PER_SECTOR * getSectorCount() - data.length - 5;
		for (; remaining > PADDING_SIZE; remaining -= PADDING_SIZE) {
			output.write(PADDING);
		}
		for (; remaining > 0; remaining--) {
			output.writeByte(0);
		}
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

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
}
