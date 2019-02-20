package com.github.kneelawk.nbtcoder.file;

import com.github.kneelawk.nbtcoder.region.ChunkLocation;
import com.github.kneelawk.nbtcoder.region.ChunkPartition;
import com.github.kneelawk.nbtcoder.region.Partition;
import com.github.kneelawk.nbtcoder.region.RegionFile;

import java.util.List;
import java.util.Map;

public interface PartitionedFile extends NBTFile {
	List<Partition> getPartitions();

	ChunkPartition getChunk(ChunkLocation location);

	Map<ChunkLocation, Integer> getUnusedTimestamps();

	RegionFile getRegionFile();

	default ChunkPartition getChunk(int x, int z) {
		return getChunk(new ChunkLocation(x, z));
	}
}
