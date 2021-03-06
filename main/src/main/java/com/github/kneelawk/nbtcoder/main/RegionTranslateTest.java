package com.github.kneelawk.nbtcoder.main;

import com.github.kneelawk.nbtcoder.nbt.DefaultTagFactory;
import com.github.kneelawk.nbtcoder.nbt.Tag;
import com.github.kneelawk.nbtcoder.nbt.TagFactory;
import com.github.kneelawk.nbtcoder.region.*;
import com.google.common.collect.Lists;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RegionTranslateTest {

	public static void main(String[] args) throws IOException {
		TagFactory factory = new DefaultTagFactory();

		System.out.println("Loading...");
		RegionFile regionFile = RegionFileIO.readRegionFile(new FileInputStream("../r.0.0.mca"));
		List<Partition> loadedPartitions = regionFile.getPartitions();

		System.out.println("Copying...");
		List<Partition> newPartitions = Lists.newArrayList();
		for (Partition part : loadedPartitions) {
			if (part instanceof ChunkPartition) {
				ChunkPartition chunk = (ChunkPartition) part;

				int x = chunk.getX();
				int z = chunk.getZ();
				int timestamp = chunk.getTimestamp();
				byte compression = chunk.getCompressionType();

				Tag data = chunk.readTag(factory);

				Chunk newChunk = new Chunk.Builder().setCompressionType(compression).setTimestamp(timestamp).setX(x)
						.setZ(z).setPaddingData(chunk.getPaddingData()).build();
				newChunk.writeTag(data);

				newPartitions.add(newChunk);
			} else if (part instanceof EmptyPartition) {
				newPartitions.add(part);
			}
		}

		System.out.println("Writing...");
		RegionFileIO.writeRegionFile(new FileOutputStream("../r.0.0.mca.1"), new SimpleRegionFile(newPartitions, null));

		System.out.println("Done.");
	}

}
