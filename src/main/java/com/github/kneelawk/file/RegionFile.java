package com.github.kneelawk.file;

import java.util.List;

import com.github.kneelawk.region.ChunkPartition;
import com.github.kneelawk.region.Partition;

public interface RegionFile extends NBTFile {
	public List<Partition> getPartitions();

	public ChunkPartition getChunk(int x, int z);
}
