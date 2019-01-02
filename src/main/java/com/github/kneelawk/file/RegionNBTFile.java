package com.github.kneelawk.file;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.github.kneelawk.region.ChunkPartition;
import com.github.kneelawk.region.Partition;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RegionNBTFile implements NBTFile, RegionFile {
	private String filename;
	private List<Partition> partitions = Lists.newArrayList();
	private Map<Integer, ChunkPartition> chunks = Maps.newHashMap();

	public RegionNBTFile(String filename, Collection<Partition> partitions) {
		this.filename = filename;
		this.partitions.addAll(partitions);

		for (Partition part : partitions) {
			if (part instanceof ChunkPartition) {
				ChunkPartition chunk = (ChunkPartition) part;
				chunks.put(chunk.getX() + chunk.getZ() * 32, chunk);
			}
		}
	}

	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public String getFileType() {
		return NBTFileValues.REGION_FILE_TYPE_STRING;
	}

	@Override
	public List<Partition> getPartitions() {
		return partitions;
	}

	@Override
	public ChunkPartition getChunk(int x, int z) {
		return chunks.get(x + z * 32);
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setPartitions(Collection<Partition> partitions) {
		this.partitions.clear();
		this.partitions.addAll(partitions);
	}
}
