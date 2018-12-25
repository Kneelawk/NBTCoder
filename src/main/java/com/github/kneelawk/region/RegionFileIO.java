package com.github.kneelawk.region;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.kneelawk.nbt.TagFactory;

public class RegionFileIO {
	private static final int PADDING_SIZE = 64;
	private static final byte[] PADDING = new byte[PADDING_SIZE];
	private static final byte[] EMPTY = new byte[RegionValues.BYTES_PER_SECTOR];

	public static List<Partition> readRegionFile(InputStream is, TagFactory factory) throws IOException {
		return readRegionFile(new DataInputStream(new BufferedInputStream(is)), factory);
	}

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

		int[] timestamps = new int[RegionValues.INTS_PER_SECTOR];
		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			timestamps[i] = input.readInt();
		}

		List<Partition> partitions = new ArrayList<>();

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

			ChunkPartition chunk = new ChunkPartition(type, loc.getX(), loc.getZ(), data, timestamps[sectorNum]);

			// Align to the sector borders.
			// The extra -5 is because of the 4 bytes of length data and 1 byte of
			// compression data read beforehand
			// The (length - 1) is because that's the actual amount of bytes read
			input.skipBytes(loc.getSectorCount() * RegionValues.BYTES_PER_SECTOR - (length - 1) - 5);

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
			int sectorCount = part.getSectorCount();
			if (part instanceof ChunkPartition) {
				ChunkPartition chunk = (ChunkPartition) part;
				int location = chunk.getZ() * 32 + chunk.getX();
				offsets[location] = ((sectorNum << 8) & 0xFFFFFF00) | (sectorCount & 0xFF);
				timestamps[location] = chunk.getTimestamp();
			}
			sectorNum += sectorCount;
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
			if (part instanceof ChunkPartition) {
				ChunkPartition chunk = (ChunkPartition) part;
				int length = chunk.size();
				output.writeInt(length + 1);
				output.writeByte(chunk.getCompressionType());
				output.write(chunk.getData(), 0, length);

				// pad the bytes to the nearest sector boundary
				int remaining = RegionValues.BYTES_PER_SECTOR * chunk.getSectorCount() - length - 5;
				for (; remaining > PADDING_SIZE; remaining -= PADDING_SIZE) {
					output.write(PADDING);
				}
				for (; remaining > 0; remaining--) {
					output.writeByte(0);
				}
			} else {
				int sectors = part.getSectorCount();
				for (int i = 0; i < sectors; i++) {
					output.write(EMPTY);
				}
			}
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
