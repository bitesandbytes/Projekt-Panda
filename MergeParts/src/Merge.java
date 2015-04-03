/*
 * Author : J S Suhas
 * Project Panda - Chat Server + File Sharing application.
 * Merge Class - Merges multiple pieces of a file with piece size 'splitSize'.
 */

import java.io.*;

public class Merge
{
	//Merges pieces of file 'filename' with atmost 'splitSize' bytes in each piece.
	public static void mergeAtReceiver(String filename, int splitSize) throws IOException
	{
		FileInputStream in = null;
		FileOutputStream out = null;
		File readFile = null;
		File writeFile = null;
		writeFile = new File(filename);
		out = new FileOutputStream(writeFile);
		byte[] readData = new byte[splitSize];
		int partNumber = 0;
		int bytesRead = 0;
		while (true)
		{
			try
			{
				readFile = new File(filename + ".part" + partNumber);
				in = new FileInputStream(readFile);
				bytesRead = in.read(readData);
				out.write(readData, 0, bytesRead);
				in.close();
				partNumber++;
			}
			catch (FileNotFoundException fEx)
			{
				break;
			}
		}
		out.close();
	}
}
