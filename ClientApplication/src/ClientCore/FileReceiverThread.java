package ClientCore;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class FileReceiverThread extends Thread
{
	private static int listenPort = 4500;
	//Default File Path
	private static String fileDestination = "/home/akshay/Documents/";
	private static String fileName;

	public FileReceiverThread(String fn)
	{
		super();
		fileName = fn;
	}

	public void run()
	{
		SocketChannel socketChannel = null;
		try
		{
			socketChannel = getNewServerSocketChannel();
			readFromSocket(socketChannel);
		}
		catch (IOException e)
		{
			System.out.println("Failed to receive file. Try agian.");
			return;
		}
		fileName = null;
		(new FileReceiveControlThread()).start(); 
	}

	public SocketChannel getNewServerSocketChannel() throws IOException
	{

		ServerSocketChannel serverSocketChannel = null;
		SocketChannel socketChannel = null;
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(listenPort));
		socketChannel = serverSocketChannel.accept();
		System.out.println("Receiving file from "
				+ socketChannel.getRemoteAddress());
		return socketChannel;
	}

	public void readFromSocket(SocketChannel socketChannel) throws IOException
	{
		RandomAccessFile file = null;
		file = new RandomAccessFile(fileDestination+fileName, "rw");
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		FileChannel fileChannel = file.getChannel();
		while (socketChannel.read(buffer) > 0)
		{
			buffer.flip();
			fileChannel.write(buffer);
			buffer.clear();
		}
		fileChannel.close();
		socketChannel.close();
		file.close();
	}
}