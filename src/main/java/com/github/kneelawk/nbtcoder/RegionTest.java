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
import com.github.kneelawk.region.ChunkPartition;
import com.github.kneelawk.region.EmptyPartition;
import com.github.kneelawk.region.Partition;
import com.github.kneelawk.region.RegionFileIO;
import com.github.kneelawk.region.RegionValues;
import com.github.kneelawk.utils.ByteArrayUtils;

public class RegionTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TagFactory factory = new DefaultTagFactory();
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder().build();
		PrintStream out = new PrintStream(new FileOutputStream("../r.0.0.mca.txt"));
		out.println("(file");
		out.println("(properties");
		out.println("name=r.0.0.mca");
		out.println("type=region");
		out.println(")");

		System.out.println("Loading...");

		List<Partition> partitions = RegionFileIO.readRegionFile(new FileInputStream("../r.0.0.mca"));

		System.out.println("Printing...");

		int size = partitions.size();
		for (int i = 0; i < size; i++) {
			Partition part = partitions.get(i);

			out.println("(partition");

			out.println("(properties");
			if (part instanceof EmptyPartition) {
				out.println("type=empty");
				out.println("size=" + part.getSectorCount());
				out.println(")");
			} else {
				ChunkPartition chunk = (ChunkPartition) part;
				out.println("type=chunk");
				out.println("timestamp=" + chunk.getTimestamp());
				out.println("compression="
						+ (chunk.getCompressionType() == RegionValues.DEFLATE_COMPRESSION ? "deflate" : "gzip"));
				out.println("x=" + chunk.getX());
				out.println("z=" + chunk.getZ());
				out.println(")");
				out.println("(data");
				out.println(printer.print(chunk.readTag(factory)));
				out.println(")");
				if (chunk.hasPaddingData()) {
					byte[] paddingData = chunk.getPaddingData();
					out.println("(padding");
					out.println(ByteArrayUtils.toHex(paddingData, chunk.size()));
					out.println(")");
				}
			}
			out.println(")");
			System.out.println("Partition " + i + " / " + size);
		}

		out.println(")");

		System.out.println("Done.");
		out.close();
	}

}
