package com.github.kneelawk.region;

public class ChunkData {
	private int offset;
	private int location;
	private Chunk chunk;

	public ChunkData(int offset, int location, Chunk chunk) {
		this.offset = offset;
		this.location = location;
		this.chunk = chunk;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSectorNumber() {
		return offset >> 8;
	}

	public void setSectorNumber(int sectorNumber) {
		offset = (sectorNumber << 8) | (offset & 0xFF);
	}

	public int getSectorCount() {
		return offset & 0xFF;
	}

	public void setSectorCount(int sectorCount) {
		offset = (offset & 0xFFFFFF00) | (sectorCount & 0xFF);
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	public int getX() {
		return location % 32;
	}

	public int getZ() {
		return location / 32;
	}
}