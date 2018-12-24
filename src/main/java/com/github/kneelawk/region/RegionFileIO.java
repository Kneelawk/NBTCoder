package com.github.kneelawk.region;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.kneelawk.nbt.TagFactory;

public class RegionFileIO {
	public static List<Partition> readRegionFile(DataInputStream input, TagFactory factory) throws IOException {
		// keys are stored in ascending order
		Map<Integer, ChunkLoc> offsetMap = new TreeMap<>();

		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			int offset = input.readInt();
			if (offset != 0) {
				ChunkLoc loc = new ChunkLoc(offset, i);
				offsetMap.put(loc.getSectorNumber(), loc);
			}
		}
		
		

		List<Partition> partitions = new ArrayList<>();

		return partitions;
	}

	private static class ChunkLoc {
		private int offset;
		private int location;

		public ChunkLoc(int offset, int location) {
			this.offset = offset;
			this.location = location;
		}

		public int getOffset() {
			return offset;
		}

		public int getSectorNumber() {
			return offset >> 8;
		}

		public int getSectorCount() {
			return offset % 8;
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
		public String toString() {
			return "ChunkLoc(getSectorNumber()=" + getSectorNumber() + ", getSectorCount()=" + getSectorCount()
					+ ", getX()=" + getX() + ", getZ()=" + getZ() + ")";
		}
	}
}
