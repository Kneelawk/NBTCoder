package com.github.kneelawk.file;

import java.util.List;

import com.github.kneelawk.region.Partition;

public class RegionNBTFile implements NBTFile {
	private List<Partition> partitions;

	public RegionNBTFile(List<Partition> partitions) {
		this.partitions = partitions;
	}

	public List<Partition> getPartitions() {
		return partitions;
	}

	public void setPartitions(List<Partition> partitions) {
		this.partitions = partitions;
	}
}
