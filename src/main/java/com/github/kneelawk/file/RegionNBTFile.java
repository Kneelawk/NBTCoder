package com.github.kneelawk.file;

import java.util.List;

import com.github.kneelawk.region.Partition;

public class RegionNBTFile implements NBTFile {
	private String filename;
	private List<Partition> partitions;

	public RegionNBTFile(String filename, List<Partition> partitions) {
		this.filename = filename;
		this.partitions = partitions;
	}

	@Override
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<Partition> getPartitions() {
		return partitions;
	}

	public void setPartitions(List<Partition> partitions) {
		this.partitions = partitions;
	}
}
