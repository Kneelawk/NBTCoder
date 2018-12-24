package com.github.kneelawk.nbtcoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import com.github.kneelawk.nbt.DefaultTagFactory;
import com.github.kneelawk.nbt.TagFactory;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;
import com.github.kneelawk.region.Chunk;
import com.github.kneelawk.region.Partition;
import com.github.kneelawk.region.PartitionType;
import com.github.kneelawk.region.RegionFileIO;
import com.github.kneelawk.region.RegionValues;

public class RegionTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TagFactory factory = new DefaultTagFactory();
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder().build();
		PrintStream out = new PrintStream(new FileOutputStream("../r.-1.-2.mca.txt"));
		out.println("(file");
		out.println("(properties");
		out.println("name=r.-1.-2.mca");
		out.println("type=region");
		out.println(")");

		System.out.println("Loading...");

		List<Partition> partitions = RegionFileIO.readRegionFile(new FileInputStream("../r.-1.-2.mca"), factory);

		System.out.println("Printing...");

		int size = partitions.size();
		for (int i = 0; i < size; i++) {
			Partition part = partitions.get(i);

			out.println("(partition");

			PartitionType type = part.getType();

			out.println("(properties");
			if (type == PartitionType.EMPTY) {
				out.println("type=empty");
				out.println("size=" + part.getSectorCount());
				out.println(")");
			} else if (type == PartitionType.RESERVED) {
				out.println("type=reserved");
				out.println("size=" + part.getSectorCount());
				out.println(")");
			} else {
				Chunk c = part.getChunk();
				out.println("type=chunk");
				out.println("timestamp=" + c.getTimestamp());
				out.println("compression="
						+ (c.getCompressionType() == RegionValues.DEFLATE_COMPRESSION ? "deflate" : "gzip"));
				out.println("x=" + c.getX());
				out.println("z=" + c.getZ());
				out.println(")");
				out.println("(data");
				out.println(printer.print(c.readData(factory)));
				out.println(")");
			}
			out.println(")");
			System.out.println("Partition " + i + " / " + size);
		}

		out.println(")");

		System.out.println("Done.");
		out.close();
	}

}
