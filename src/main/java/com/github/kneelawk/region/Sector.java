package com.github.kneelawk.region;

public class Sector {
	private boolean other;
	private ChunkData data;

	public Sector() {
		other = false;
		data = null;
	}

	public Sector(boolean other) {
		this.other = other;
		data = null;
	}

	public Sector(ChunkData data) {
		other = false;
		this.data = data;
	}

	public Sector(boolean other, ChunkData data) {
		this.other = other;
		this.data = data;
	}

	public ChunkData getData() {
		return data;
	}

	public void setData(ChunkData data) {
		this.data = data;
	}

	public boolean isOther() {
		return other;
	}

	public void setOther(boolean other) {
		this.other = other;
	}

	public boolean isEmpty() {
		return data == null && !other;
	}
}