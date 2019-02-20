package com.github.kneelawk.nbtcoder.region;

import java.io.DataOutputStream;
import java.io.IOException;

import static com.github.kneelawk.nbtcoder.region.RegionValues.BYTES_PER_SECTOR;

public class EmptyPartition implements Partition {
	// In java, all arrays are 0 initialized
	private static final byte[] EMPTY = new byte[BYTES_PER_SECTOR];
	private static final int PADDING_SIZE = 64;
	private static final byte[] PADDING = new byte[PADDING_SIZE];

	private int sectorCount;
	private byte[] paddingData;

	public EmptyPartition(int sectorCount, byte[] paddingData) {
		this.sectorCount = sectorCount;
		this.paddingData = paddingData;
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
		// pad the bytes to the nearest sector boundary
		int remaining = BYTES_PER_SECTOR * sectorCount;

		// we start by writing the padding data
		if (paddingData != null && paddingData.length > 0) {
			if (paddingData.length < remaining) {
				remaining -= paddingData.length;
				output.write(paddingData);
			} else {
				output.write(paddingData, 0, remaining);
				return;
			}
		}

		// finish padding by putting a bunch of 0s
		for (; remaining > BYTES_PER_SECTOR; remaining -= BYTES_PER_SECTOR) {
			output.write(EMPTY);
		}
		for (; remaining > PADDING_SIZE; remaining -= PADDING_SIZE) {
			output.write(PADDING);
		}
		for (; remaining > 0; remaining--) {
			output.writeByte(0);
		}
	}

	@Override
	public byte[] getPaddingData() {
		return paddingData;
	}

	@Override
	public boolean hasPaddingData() {
		return paddingData != null && paddingData.length > 0;
	}

	@Override
	public String getPartitionType() {
		return RegionValues.EMPTY_PARTITION_TYPE_STRING;
	}

}
