package com.github.kneelawk.region;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Partition {
	public int getSectorCount();

	public void writeMetadata(int sectorNum, int[] offsets, int[] timestamps);

	public void writeData(DataOutputStream output) throws IOException;

	public String getPartitionType();
}
