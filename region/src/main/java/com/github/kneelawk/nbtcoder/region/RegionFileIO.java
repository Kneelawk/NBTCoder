package com.github.kneelawk.nbtcoder.region;

import com.github.kneelawk.nbtcoder.utils.ByteArrayUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RegionFileIO {

	public static RegionFile readRegionFile(InputStream is) throws IOException {
		return readRegionFile(new DataInputStream(new BufferedInputStream(is)));
	}

	public static RegionFile readRegionFile(DataInputStream input) throws IOException {
		// keys are stored in ascending order
		Map<Integer, HeaderValue> offsetMap = new TreeMap<>();

		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			int offset = input.readInt();
			if (offset != 0) {
				HeaderValue loc = new HeaderValue(offset, i);
				offsetMap.put(loc.getSectorNumber(), loc);
			}
		}

		Map<ChunkLocation, Integer> unusedTimestamps = Maps.newHashMap();
		int[] timestamps = new int[RegionValues.INTS_PER_SECTOR];
		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			int timestamp = input.readInt();
			if (timestamp != 0) {
				timestamps[i] = timestamp;
				unusedTimestamps.put(new ChunkLocation(i), timestamps[i]);
			}
		}

		List<Partition> partitions = Lists.newArrayList();

		int sectorsRead = 2;
		for (int sectorNum : offsetMap.keySet()) {
			// skip to the correct sector
			if (sectorsRead < sectorNum) {
				// These may be garbage bytes but they're still important
				byte[] paddingData = new byte[(sectorNum - sectorsRead) * RegionValues.BYTES_PER_SECTOR];
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

				partitions.add(new EmptyPartition(sectorNum - sectorsRead, paddingData));
				sectorsRead = sectorNum;
			} else if (sectorsRead > sectorNum) {
				throw new IOException("Skipped sectors beyond the requested sector (This usually means the region file is corrupt)");
			}

			HeaderValue loc = offsetMap.get(sectorNum);

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

			Chunk chunk = new Chunk(type, loc.getX(), loc.getZ(), data, paddingData, timestamps[loc.getLocation()]);

			sectorsRead += loc.getSectorCount();

			partitions.add(chunk);

			unusedTimestamps.remove(new ChunkLocation(loc.getLocation()));
		}

		// grab any data left over at the end of the region
		int firstByte;
		if ((firstByte = input.read()) != -1) {
			// keep expanding paddingData with extra data until we have everything
			int sectorCount = 0;
			byte[] paddingData = null;
			do {
				byte[] newPaddingData = new byte[RegionValues.BYTES_PER_SECTOR * (sectorCount + 1)];
				// the `sectorCount + 1` is outside of parentheses on purpose
				input.readFully(newPaddingData, 1 + RegionValues.BYTES_PER_SECTOR * sectorCount, RegionValues.BYTES_PER_SECTOR - 1);
				newPaddingData[RegionValues.BYTES_PER_SECTOR * sectorCount] = (byte) (firstByte & 0xFF);

				if (paddingData != null) {
					System.arraycopy(paddingData, 0, newPaddingData, 0, RegionValues.BYTES_PER_SECTOR * sectorCount);
				}

				paddingData = newPaddingData;

				sectorCount++;
			} while ((firstByte = input.read()) != -1);

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

			partitions.add(new EmptyPartition(sectorCount, paddingData));
		}

		return new SimpleRegionFile(partitions, unusedTimestamps);
	}

	public static void writeRegionFile(OutputStream os, RegionFile regionFile) throws IOException {
		writeRegionFile(new DataOutputStream(os), regionFile);
	}

	public static void writeRegionFile(DataOutputStream output, RegionFile regionFile) throws IOException {
		int[] offsets = new int[RegionValues.INTS_PER_SECTOR];
		int[] timestamps = new int[RegionValues.INTS_PER_SECTOR];

		List<Partition> partitions = regionFile.getPartitions();
		Map<ChunkLocation, Integer> unusedTimestamps = regionFile.getUnusedTimestamps();

		// add the unused timestamps
		for (Map.Entry<ChunkLocation, Integer> entry : unusedTimestamps.entrySet()) {
			timestamps[entry.getKey().getLocation()] = entry.getValue();
		}

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

	private static class HeaderValue {
		private int offset;
		private int location;

		public HeaderValue(int offset, int location) {
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
			return "HeaderValue(getSectorNumber()=" + getSectorNumber() + ", getSectorCount()=" + getSectorCount()
					+ ", getX()=" + getX() + ", getZ()=" + getZ() + ")";
		}
	}
}
