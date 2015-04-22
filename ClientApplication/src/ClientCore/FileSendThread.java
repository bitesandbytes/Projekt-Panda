package ClientCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class FileSendThread extends Thread {
	public static String filePath;
	private static SocketChannel socketChannel = null;
	private final static String destIP = "10.42.0.27";
	private final static int fileSendPort = 4500;
	
	public FileSendThread(String f){
		super();
		filePath = f;
	}
	
	public void run(){
		socketChannel = null;
		try
		{
			socketChannel = SocketChannel.open();
			SocketAddress socketAddress = new InetSocketAddress(destIP,
					fileSendPort);
			socketChannel.connect(socketAddress);
			System.out.println("Connected... Now sending the file");

		} catch (IOException e)
		{
			System.out.println("Unable to connect to socket");
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
			System.out.println("End of file reached..");
			socketChannel.close();
			aFile.close();
		} catch (FileNotFoundException e)
		{
			System.out.println("Unable to locate File");
		} catch (IOException e)
		{
			System.out.println("IOException");
		} catch (InterruptedException e)
		{
			System.out.println("InterruptedException");
		}
	}
}
