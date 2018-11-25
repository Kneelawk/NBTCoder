package com.github.kneelawk.region;

public class Partition {
	private PartitionType type;
	private Chunk chunk;
	private int sectorNumber;
	private int sectorCount;

	public Partition(PartitionType type, int sectorNumber, int sectorCount) {
		this.type = type;
		this.sectorNumber = sectorNumber;
		this.sectorCount = sectorCount;
	}

	public Partition(PartitionType type, Chunk chunk, int sectorNumber, int sectorCount) {
		this.type = type;
		this.chunk = chunk;
		this.sectorNumber = sectorNumber;
		this.sectorCount = sectorCount;
	}

	public PartitionType getType() {
		return type;
	}

	public void setType(PartitionType type) {
		this.type = type;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	public int getSectorNumber() {
		return sectorNumber;
	}

	public void setSectorNumber(int sectorNumber) {
		this.sectorNumber = sectorNumber;
	}

	public int getSectorCount() {
		return sectorCount;
	}

	public void setSectorCount(int sectorCount) {
		this.sectorCount = sectorCount;
	}
}
