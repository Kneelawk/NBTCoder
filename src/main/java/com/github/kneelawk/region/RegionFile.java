package com.github.kneelawk.region;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import com.github.kneelawk.nbt.TagFactory;

public class RegionFile {
	private Map<Integer, Chunk> chunks;
	private List<Partition> partitions;
	private int[] timestamps;

	public RegionFile() {
		chunks = new LinkedHashMap<>();
		partitions = new ArrayList<>();
		timestamps = new int[RegionValues.INTS_PER_SECTOR];
	}

	public void readStream(InputStream is, TagFactory factory) throws IOException {
		read(new DataInputStream(new BufferedInputStream(is)), factory);
	}

	public void read(DataInputStream input, TagFactory factory) throws IOException {
		// keys are stored in ascending order
		Map<Integer, ChunkLoc> offsetMap = new TreeMap<>();

		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			int offset = input.readInt();
			if (offset != 0) {
				ChunkLoc loc = new ChunkLoc(offset, i);
				offsetMap.put(loc.getSectorNumber(), loc);
			}
		}

		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			timestamps[i] = input.readInt();
		}

		partitions.add(new Partition(PartitionType.RESERVED, 0, 2));

		int sectorsRead = 2;
		Iterator<Integer> it = offsetMap.keySet().iterator();
		while (it.hasNext()) {
			int sectorNum = it.next();

			// skip to the correct sector
			if (sectorsRead < sectorNum) {
				input.skipBytes((sectorNum - sectorsRead) * RegionValues.BYTES_PER_SECTOR);
				partitions.add(new Partition(PartitionType.EMPTY, sectorsRead, sectorNum - sectorsRead));
				sectorsRead = sectorNum;
			}

			ChunkLoc loc = offsetMap.get(sectorNum);

			int length = input.readInt();
			byte type = input.readByte();

			byte[] data = new byte[length];
			input.readFully(data);

			DataInputStream dis = null;

			switch (type) {
			case RegionValues.GZIP_COMPRESSION:
				System.err.println("Type: gzip");
				dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(data))));
				break;
			case RegionValues.DEFLATE_COMPRESSION:
				System.err.println("Type: deflate");
				dis = new DataInputStream(
						new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(data))));
				break;
			default:
				throw new IOException("Unknown chunk compression type: " + type);
			}

			// Align to the sector borders.
			// The extra -5 is because of the 4 bytes of length data and 1 byte of
			// compression data read beforehand
			input.skipBytes(loc.getSectorCount() * RegionValues.BYTES_PER_SECTOR - length - 5);

			sectorsRead += loc.getSectorCount();

			Chunk chunk = new Chunk(type, loc.getX(), loc.getZ());
			chunk.read(dis, factory);

			partitions.add(new Partition(PartitionType.CHUNK, chunk, loc.getSectorNumber(), loc.getSectorCount()));

			chunks.put(loc.getLocation(), chunk);
		}
	}

	public boolean hasChunk(int x, int z) {
		return chunks.containsKey(x + z * 32);
	}

	public Chunk getChunk(int x, int z) {
		return chunks.get(x + z * 32);
	}

	public List<Partition> getPartitions() {
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
