package com.github.kneelawk.region;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.github.kneelawk.nbt.NBTIO;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagFactory;

public class Chunk {
	private byte compressionType;
	private int x;
	private int z;
	private Tag tag;

	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public Chunk(int x, int z, Tag tag) {
		this.x = x;
		this.z = z;
		this.tag = tag;
	}

	public Chunk(byte compressionType, int x, int z) {
		this.compressionType = compressionType;
		this.x = x;
		this.z = z;
	}

	public Chunk(byte compressionType, int x, int z, Tag tag) {
		this.compressionType = compressionType;
		this.x = x;
		this.z = z;
		this.tag = tag;
	}

	public void read(DataInputStream dis, TagFactory factory) throws IOException {
		tag = NBTIO.read(dis, factory);
	}

	public void write(DataOutputStream dos) throws IOException {
		NBTIO.write(tag, dos);
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

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
