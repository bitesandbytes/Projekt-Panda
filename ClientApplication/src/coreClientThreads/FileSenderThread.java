package coreClientThreads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import coreClient.Global;

public class FileSenderThread extends Thread
{
	public static String filePath;
	public static String filename;
	private static SocketChannel socketChannel = null;
	private static String destIP;
	private final static int fileSendPort = Global.clientFilePort;

	public FileSenderThread(String path, String name, String dip)
	{
		super();
		filePath = path;
		filename = name;
		destIP = dip;
	}

	public void run()
	{
		socketChannel = null;
		try
		{
			socketChannel = SocketChannel.open();
			SocketAddress socketAddress = new InetSocketAddress(destIP,
					fileSendPort);
			socketChannel.connect(socketAddress);
			System.out
					.println("Connected. Sending filename | FileSenderThread.");

		}
		catch (IOException e)
		{
			System.out
					.println("Remote socket connection failed | FileSenderThread.");
			return;
		}
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(socketChannel
					.socket().getOutputStream());
			oos.writeUTF(filename);
		}
		catch (IOException e1)
		{
			System.out.println("Unable to write filename. Try again. ");
			return;
		}
		RandomAccessFile aFile = null;
		try
		{
			File file = new File(filePath);
			aFile = new RandomAccessFile(file, "r");
			FileChannel inChannel = aFile.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (inChannel.read(buffer) > 0)
			{
				buffer.flip();
				socketChannel.write(buffer);
				buffer.clear();
			}
			Thread.sleep(1000);
			System.out.println("File Successfully Sent | FileSenderThread.");
			socketChannel.close();
			aFile.close();
			return;
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Unable to locate File | FileSenderThread.");
		}
		catch (IOException e)
		{
			System.out.println("IOException | FileSenderThread.");

		}
		catch (InterruptedException e)
		{
			System.out.println("Thread Sleep Interrupted | FileSenderThread.");
		}
	}
}
