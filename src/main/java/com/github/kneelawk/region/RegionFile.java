package com.github.kneelawk.region;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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
	private List<Sector> sectors;
	private int[] timestamps;

	public RegionFile() {
		chunks = new LinkedHashMap<>();
		sectors = new ArrayList<>();
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

		sectors.add(new Sector(true));

		for (int i = 0; i < RegionValues.INTS_PER_SECTOR; i++) {
			timestamps[i] = input.readInt();
		}

		sectors.add(new Sector(true));

		int sectorsRead = 2;
		Iterator<Integer> it = offsetMap.keySet().iterator();
		while (it.hasNext()) {
			int sectorNum = it.next();
			while (sectorsRead < sectorNum) {
				System.err.println("Skipping sector: " + sectorsRead);
				input.skipBytes(RegionValues.BYTES_PER_SECTOR);
				sectorsRead++;
				sectors.add(new Sector());
			}

			ChunkLoc loc = offsetMap.get(sectorNum);

			System.err.println("Reading chunk: " + loc);

			int length = input.readInt();
			byte type = input.readByte();

			System.err.println("Length: " + Math.ceil(((double) length) / ((double) RegionValues.BYTES_PER_SECTOR)));

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

			input.skipBytes(loc.getSectorCount() * RegionValues.BYTES_PER_SECTOR - length - 5);

			sectorsRead += loc.getSectorCount();

			Chunk chunk = new Chunk(type, loc.getX(), loc.getZ());
			chunk.read(dis, factory);

			ChunkData chunkData = new ChunkData(loc.getOffset(), loc.getLocation(), chunk);

			for (int i = 0; i < loc.getSectorCount(); i++) {
				sectors.add(new Sector(chunkData));
			}

			chunks.put(loc.getLocation(), chunk);
		}
	}

	public boolean hasChunk(int x, int z) {
		return chunks.containsKey(x + z * 32);
	}

	public Chunk getChunk(int x, int z) {
		return chunks.get(x + z * 32);
	}

	public List<Sector> getSectors() {
		return sectors;
	}

	public Collection<Chunk> getChunks() {
		return chunks.values();
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
