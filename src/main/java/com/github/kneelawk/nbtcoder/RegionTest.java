package com.github.kneelawk.nbtcoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

import com.github.kneelawk.nbt.DefaultTagFactory;
import com.github.kneelawk.nbt.TagCompound;
import com.github.kneelawk.nbt.TagFactory;
import com.github.kneelawk.nbt.TagList;
import com.github.kneelawk.nbtlanguage.NBTLanguagePrinter;
import com.github.kneelawk.region.Chunk;
import com.github.kneelawk.region.RegionFile;
import com.github.kneelawk.region.RegionValues;

public class RegionTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TagFactory factory = new DefaultTagFactory();
		NBTLanguagePrinter printer = new NBTLanguagePrinter.Builder().build();
		RegionFile file = new RegionFile();
		file.readStream(new FileInputStream("../r.-1.-2.mca"), factory);
		PrintStream out = new PrintStream(new FileOutputStream("../r.-1.-2.mca.txt"));
		TagCompound root = new TagCompound();
//		out.println("!file");
//		out.println("name=r.-1.-2.mca");
//		out.println("type=region");
		root.putString("name", "r.-1.-2.mca");
		root.putString("type", "region");

		TagList<TagCompound> chunksTag = new TagList<>("chunks");
		root.put(chunksTag);

		Collection<Chunk> chunks = file.getChunks();
		int size = chunks.size();
		int i = 0;
		for (Iterator<Chunk> it = chunks.iterator(); it.hasNext(); i++) {
			TagCompound chunkTag = new TagCompound();
			chunksTag.add(chunkTag);
			Chunk c = it.next();
//			out.println("!chunk");
//			out.println(
//					"compression=" + (c.getCompressionType() == RegionValues.DEFLATE_COMPRESSION ? "deflate" : "gzip"));
//			out.println("x=" + c.getX());
//			out.println("z=" + c.getZ());
			chunkTag.putString("compression",
					(c.getCompressionType() == RegionValues.DEFLATE_COMPRESSION ? "deflate" : "gzip"));
			chunkTag.putInt("x", c.getX());
			chunkTag.putInt("z", c.getZ());
//			out.println("!data");
			TagCompound data = new TagCompound("data");
			chunkTag.put(data);
//			out.println(printer.print(c.getTag()));
			data.put(c.getTag());
			System.out.println("Chunk " + i + " / " + size);
		}

		System.out.println("Printing...");
		out.println(printer.print(root));
		System.out.println("Done.");
		out.close();
	}

}
