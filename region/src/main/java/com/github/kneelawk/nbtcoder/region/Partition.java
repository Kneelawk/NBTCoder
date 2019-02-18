package com.github.kneelawk.nbtcoder.region;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Partition {
	int getSectorCount();

	void writeMetadata(int sectorNum, int[] offsets, int[] timestamps);

	void writeData(DataOutputStream output) throws IOException;

	String getPartitionType();
}
