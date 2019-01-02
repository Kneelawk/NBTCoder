package com.github.kneelawk.region;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.kneelawk.utils.ByteArrayUtils;
import com.google.common.collect.Lists;

public class RegionFileIO {

	public static List<Partition> readRegionFile(InputStream is) throws IOException {
		return readRegionFile(new DataInputStream(new BufferedInputStream(is)));
	}

	public static List<Partition> readRegionFile(DataInputStream input) throws IOException {
		// keys are stored in ascending order
		Map<Integer, ChunkLoc> offsetMap = new TreeMap<>();

		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			int offset = input.readInt();
			if (offset != 0) {
				ChunkLoc loc = new ChunkLoc(offset, i);
				offsetMap.put(loc.getSectorNumber(), loc);
			}
		}

		int[] timestamps = new int[RegionValues.INTS_PER_SECTOR];
		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			timestamps[i] = input.readInt();
		}

		List<Partition> partitions = Lists.newArrayList();

		int sectorsRead = 2;
		Iterator<Integer> it = offsetMap.keySet().iterator();
		while (it.hasNext()) {
			int sectorNum = it.next();

			// skip to the correct sector
			if (sectorsRead < sectorNum) {
				input.skipBytes((sectorNum - sectorsRead) * RegionValues.BYTES_PER_SECTOR);
				partitions.add(new EmptyPartition(sectorNum - sectorsRead));
				sectorsRead = sectorNum;
			} else if (sectorsRead > sectorNum) {
				throw new IOException("Skipped sectors");
			}

			ChunkLoc loc = offsetMap.get(sectorNum);

			int length = input.readInt();
			byte type = input.readByte();

			if (length > RegionValues.BYTES_PER_SECTOR * loc.getSectorCount()) {
				throw new IOException("Invalid chunk data length (is longer than its allocated sectors)");
			}

			// Mojang reads length - 1 bytes for some reason
			byte[] data = new byte[length - 1];
			input.readFully(data);

			// Align to the sector borders.
			// The extra -5 is because of the 4 bytes of length data and 1 byte of
			// compression data read beforehand
			// The (length - 1) is because that's the actual amount of bytes read
			// These may be garbage bytes but they're still important
			byte[] paddingData = new byte[loc.getSectorCount() * RegionValues.BYTES_PER_SECTOR - (length - 1) - 5];
			input.readFully(paddingData);

			// where is the data in this array
			int lastPaddingByte = ByteArrayUtils.lastNonZeroByte(paddingData);
			if (lastPaddingByte == -1) {
				// don't keep the extra data if its empty
				paddingData = null;
			} else if (lastPaddingByte < paddingData.length - 1) {
				// shrink the array to just the data because it will get padded with 0s when
				// written again
				byte[] newPaddingData = new byte[lastPaddingByte + 1];
				System.arraycopy(paddingData, 0, newPaddingData, 0, lastPaddingByte + 1);
				paddingData = newPaddingData;
			}

			Chunk chunk = new Chunk(type, loc.getX(), loc.getZ(), data, paddingData,
					timestamps[loc.getLocation()]);

			sectorsRead += loc.getSectorCount();

			partitions.add(chunk);
		}

		return partitions;
	}

	public static void writeRegionFile(OutputStream os, List<Partition> partitions) throws IOException {
		writeRegionFile(new DataOutputStream(os), partitions);
	}

	public static void writeRegionFile(DataOutputStream output, List<Partition> partitions) throws IOException {
		int[] offsets = new int[RegionValues.INTS_PER_SECTOR];
		int[] timestamps = new int[RegionValues.INTS_PER_SECTOR];

		// start at sector 2 because the first two sectors are reserved
		int sectorNum = 2;
		for (Partition part : partitions) {
			part.writeMetadata(sectorNum, offsets, timestamps);
			sectorNum += part.getSectorCount();
		}

		// write chunk offsets
		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			output.writeInt(offsets[i]);
		}

		// write timestamps
		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			output.writeInt(timestamps[i]);
		}

		for (Partition part : partitions) {
			part.writeData(output);
		}
	}

	private static class ChunkLoc {
		private int offset;
		private int location;

		public ChunkLoc(int offset, int location) {
			this.offset = offset;
			this.location = location;
		}

		public int getSectorNumber() {
			return offset >> 8;
		}

		public int getSectorCount() {
			return offset & 0xFF;
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
