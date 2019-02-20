package com.github.kneelawk.nbtcoder.file;

import com.github.kneelawk.nbtcoder.region.ChunkLocation;
import com.github.kneelawk.nbtcoder.region.ChunkPartition;
import com.github.kneelawk.nbtcoder.region.Partition;
import com.github.kneelawk.nbtcoder.region.RegionFile;

import java.util.List;
import java.util.Map;

public class RegionNBTFile implements NBTFile, PartitionedFile {
	private String filename;
	private RegionFile regionFile;

	public RegionNBTFile(String filename, RegionFile regionFile) {
		this.filename = filename;
		this.regionFile = regionFile;
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
		return regionFile.getPartitions();
	}

	@Override
	public ChunkPartition getChunk(ChunkLocation location) {
		return regionFile.getChunk(location);
	}

	@Override
	public Map<ChunkLocation, Integer> getUnusedTimestamps() {
		return regionFile.getUnusedTimestamps();
	}

	@Override
	public RegionFile getRegionFile() {
		return regionFile;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setChunk(ChunkLocation location, ChunkPartition partition) {
		regionFile.setChunk(location, partition);
	}
}
