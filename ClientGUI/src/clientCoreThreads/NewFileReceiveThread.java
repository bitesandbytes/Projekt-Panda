package clientCoreThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

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
		ServerSocketChannel serverSocketChannel = null;
		SocketChannel socketChannel = null;
		System.out.println("Opening serverSocketChannel | NewReceiverThread");
		try
		{
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket()
					.bind(new InetSocketAddress(listenPort));
		}
		catch (IOException e1)
		{
			System.out.println("Unable to bind | NewReceiverThread.");
			return;
		}

		System.out
				.println("severSocketChannel Bind successful | NewReceiverThread");
		while (true)
		{
			try
			{
				socketChannel = serverSocketChannel.accept();
				filename = getFilename(socketChannel);
				readFromSocket(socketChannel, filename);
			}
			catch (IOException | ClassNotFoundException e)
			{
				System.out
						.println("Dropped a file transfer. Request transfer again.");
				continue;
			}
		}
	}

	private String getFilename(SocketChannel socketChannel) throws IOException, ClassNotFoundException
	{
		System.out.println("Reading filename | NewFileReceiveThread.");
		ObjectInputStream ois = new ObjectInputStream(socketChannel.socket()
				.getInputStream());
		System.out.println("Got OIS | NewFileReceiveThread.");
		String filename = (String) ois.readObject();
		System.out.println("ReadUTF done | NewFileReceiveThread.");
		System.out.println("Reading filename | NewFileReceiveThread.");
		return filename;
	}

	private void readFromSocket(SocketChannel socketChannel, String filename)
			throws IOException
	{
		System.out.println("Receiving file "+filename+" | NewFileReceiveThread.");
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
		System.out.println("File receive complete| NewFileReceiveThread.");
		fileChannel.close();
		socketChannel.close();
		file.close();
	}
}
