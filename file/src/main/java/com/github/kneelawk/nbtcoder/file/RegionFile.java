package com.github.kneelawk.nbtcoder.file;

import java.util.List;

import com.github.kneelawk.nbtcoder.region.ChunkPartition;
import com.github.kneelawk.nbtcoder.region.Partition;

public interface RegionFile extends NBTFile {
	public List<Partition> getPartitions();

	public ChunkPartition getChunk(int x, int z);
}
