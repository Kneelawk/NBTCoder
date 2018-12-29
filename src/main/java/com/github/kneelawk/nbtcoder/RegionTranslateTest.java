package com.github.kneelawk.nbtcoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kneelawk.nbt.DefaultTagFactory;
import com.github.kneelawk.nbt.Tag;
import com.github.kneelawk.nbt.TagFactory;
import com.github.kneelawk.region.ChunkPartition;
import com.github.kneelawk.region.EmptyPartition;
import com.github.kneelawk.region.Partition;
import com.github.kneelawk.region.RegionFileIO;

public class RegionTranslateTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TagFactory factory = new DefaultTagFactory();

		System.out.println("Loading...");
		List<Partition> loadedPartitions = RegionFileIO.readRegionFile(new FileInputStream("../r.-1.-2.mca"));

		System.out.println("Copying...");
		List<Partition> newPartitions = new ArrayList<>();
		for (Partition part : loadedPartitions) {
			if (part instanceof ChunkPartition) {
				ChunkPartition chunk = (ChunkPartition) part;

				int x = chunk.getX();
				int z = chunk.getZ();
				int timestamp = chunk.getTimestamp();
				byte compression = chunk.getCompressionType();

				Tag data = chunk.readTag(factory);

				ChunkPartition newChunk = new ChunkPartition.Builder().setCompressionType(compression)
						.setTimestamp(timestamp).setX(x).setZ(z).build();
				newChunk.writeTag(data);

				newPartitions.add(newChunk);
			} else if (part instanceof EmptyPartition) {
				newPartitions.add(part);
			}
		}

		System.out.println("Writing...");
		RegionFileIO.writeRegionFile(new FileOutputStream("../r.-1.-2.mca.3"), newPartitions);

		System.out.println("Done.");
	}

}
