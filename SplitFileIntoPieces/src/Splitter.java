/*
 * Author : J S Suhas
 * Project Panda - Chat Server + File Sharing application.
 * Splitter Class - Splits a file into pieces with size 'splitSize'.
 */

import java.io.*;
import java.nio.file.*;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class Splitter
{
	//Splits 'filename' into pieces with size atmost 'splitSize' bytes.
	public static void splitAtSender(String filename, int splitSize) throws IOException
	{
		final Path file = Paths.get(filename).toRealPath();
		final String filenameBase = file.getFileName().toString();
		final byte[] buf = new byte[splitSize];

		int partNumber = 0;
		Path part;
		int bytesRead;
		byte[] toWrite;

		try (final InputStream in = Files.newInputStream(file);)
		{
			while ((bytesRead = in.read(buf)) != -1)
			{
				part = file.resolveSibling(filenameBase + ".part" + partNumber);
				toWrite = bytesRead == splitSize ? buf : Arrays.copyOf(buf, bytesRead);
				Files.write(part, toWrite, StandardOpenOption.CREATE_NEW);
				partNumber++;
			}
		}
	}
}
