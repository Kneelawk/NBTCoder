package com.github.kneelawk.nbtcoder.region;

import java.util.List;
import java.util.Map;

/**
 * Created by Kneelawk on 2/20/19.
 */
public interface RegionFile {
	Map<ChunkLocation, Integer> getUnusedTimestamps();

	List<Partition> getPartitions();

	ChunkPartition getChunk(ChunkLocation location);

	void setChunk(ChunkLocation location, ChunkPartition partition);
}
