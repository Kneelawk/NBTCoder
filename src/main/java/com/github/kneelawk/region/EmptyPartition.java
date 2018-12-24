package com.github.kneelawk.region;

public class EmptyPartition implements Partition {

	private int sectorCount;

	public EmptyPartition(int sectorCount) {
		this.sectorCount = sectorCount;
	}

	@Override
	public int getSectorCount() {
		return sectorCount;
	}

}
