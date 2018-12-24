package com.github.kneelawk.region;

public class RegionValues {
	public static final byte GZIP_COMPRESSION = 1;
	public static final byte DEFLATE_COMPRESSION = 2;

	public static final int BYTES_PER_SECTOR = 4096;
	public static final int INTS_PER_SECTOR = BYTES_PER_SECTOR / 4;
}