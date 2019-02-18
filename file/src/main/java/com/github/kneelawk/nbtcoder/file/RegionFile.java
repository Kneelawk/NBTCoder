package com.github.kneelawk.nbtcoder.file;

import com.github.kneelawk.nbtcoder.region.ChunkPartition;
import com.github.kneelawk.nbtcoder.region.Partition;

import java.util.List;

public interface RegionFile extends NBTFile {
	List<Partition> getPartitions();

	ChunkPartition getChunk(int x, int z);
}
