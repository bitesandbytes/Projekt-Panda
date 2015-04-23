package coreClientThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import common.FileControlPacket;

import coreClient.Global;

public class NewFileReceiveThread extends Thread
{
	private static int listenPort = Global.clientFilePort;
	private static String destFolder = Global.defaultFileSavePath;
	public NewFileReceiveThread()
	{
		super();
	}
	
	public void run()
	{
		String filename = null;
		while(true)
		{
			try
			{
				filename = getFilename();
				readFromSocket(getNewServerSocketChannel(), filename);
			}
			catch (ClassNotFoundException | IOException e)
			{
				continue;
			}
		}
	}
	
	
	private String getFilename() throws IOException, ClassNotFoundException
	{
		ServerSocket controlServer = new ServerSocket(listenPort);
		Socket clientSocket = controlServer.accept();
		ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
		FileControlPacket rcvPacket = (FileControlPacket) ois.readObject();
		clientSocket.close();
		controlServer.setReuseAddress(true);
		controlServer.close();
		return rcvPacket.fileName;
	}
	
	private SocketChannel getNewServerSocketChannel() throws IOException
	{
		ServerSocketChannel serverSocketChannel = null;
		SocketChannel socketChannel = null;
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(listenPort));
		socketChannel = serverSocketChannel.accept();
		System.out.println("Receiving file from "
				+ socketChannel.getRemoteAddress());
		serverSocketChannel.close();
		return socketChannel;
	}
	private void readFromSocket(SocketChannel socketChannel, String filename) throws IOException
	{
		RandomAccessFile file = null;
		file = new RandomAccessFile(destFolder + filename, "rw");
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
