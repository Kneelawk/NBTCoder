package com.github.kneelawk.nbtcoder.region;

import java.util.Objects;

/**
 * Created by Kneelawk on 2/20/19.
 */
public class ChunkLocation implements Comparable<ChunkLocation> {
	private int location;

	public ChunkLocation(int location) {
		if (location < 0 || location >= 1024) {
			throw new IllegalArgumentException("Combined location values must be between 0 and 1023 (inclusive)");
		}
		this.location = location;
	}

	public ChunkLocation(int x, int z) {
		if (x < 0 || x >= 32 || z < 0 || z >= 32) {
			throw new IllegalArgumentException("Location values must be between 0 and 31 (inclusive)");
		}

		location = x + z * 32;
	}

	public int getLocation() {
		return location;
	}

	public int getX() {
		return location % 32;
	}

	public int getZ() {
		return location / 32;
	}

	@Override
	public int compareTo(ChunkLocation o) {
		return location - o.location;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChunkLocation that = (ChunkLocation) o;
		return location == that.location;
	}

	@Override
	public int hashCode() {
		return Objects.hash(location);
	}
}
