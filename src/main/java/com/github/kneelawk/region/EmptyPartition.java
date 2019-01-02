package com.github.kneelawk.region;

import java.io.DataOutputStream;
import java.io.IOException;

public class EmptyPartition implements Partition {
	private static final byte[] EMPTY = new byte[RegionValues.BYTES_PER_SECTOR];

	private int sectorCount;

	public EmptyPartition(int sectorCount) {
		this.sectorCount = sectorCount;
	}

	@Override
	public int getSectorCount() {
		return sectorCount;
	}

	@Override
	public void writeMetadata(int sectorNum, int[] offsets, int[] timestamps) {
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		for (int i = 0; i < sectorCount; i++) {
			output.write(EMPTY);
		}
	}

	@Override
	public String getPartitionType() {
		return RegionValues.EMPTY_PARTITION_TYPE_STRING;
	}

}
