package com.github.kneelawk.nbtcoder.region;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Kneelawk on 2/19/19.
 */
public class SimpleRegionFile implements RegionFile {
	private Map<ChunkLocation, Integer> unusedTimestamps = Maps.newTreeMap();
	private List<Partition> partitions = Lists.newArrayList();
	private Map<ChunkLocation, ChunkPartition> chunks = Maps.newHashMap();

	public SimpleRegionFile(Collection<Partition> partitions, Map<ChunkLocation, Integer> unusedTimestamps) {
		this.partitions.addAll(partitions);
		if (unusedTimestamps != null) {
			this.unusedTimestamps.putAll(unusedTimestamps);
		}

		for (Partition part : partitions) {
			if (part instanceof ChunkPartition) {
				ChunkPartition chunk = (ChunkPartition) part;
				chunks.put(new ChunkLocation(chunk.getX(), chunk.getZ()), chunk);
			}
		}
	}

	@Override
	public Map<ChunkLocation, Integer> getUnusedTimestamps() {
		return unusedTimestamps;
	}

	@Override
	public List<Partition> getPartitions() {
		return partitions;
	}

	@Override
	public ChunkPartition getChunk(ChunkLocation location) {
		return chunks.get(location);
	}

	@Override
	public void setChunk(ChunkLocation location, ChunkPartition partition) {
		ChunkPartition oldPartition = chunks.put(location, partition);

		if (oldPartition == null) {
			partitions.add(partition);
			unusedTimestamps.remove(location);
		} else {
			int index = partitions.indexOf(oldPartition);
			partitions.set(index, partition);
		}
	}
}
