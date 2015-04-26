package clientCoreThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import coreClient.Global;

public class FileReceiverThread extends Thread
{
	private static int listenPort = Global.clientFilePort;
	private static String destFolder = Global.defaultFileSavePath;
	private JTextArea msgBox;
	private String filename;

	public FileReceiverThread(JTextArea textBox)
	{
		super();
		this.msgBox = textBox;
	}

	public void run()
	{
		ServerSocketChannel serverSocketChannel = null;
		SocketChannel socketChannel = null;
		Global.Log("Opening serverSocketChannel | NewReceiverThread");
		try
		{
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket()
					.bind(new InetSocketAddress(listenPort));
		}
		catch (IOException e1)
		{
			Global.Log("Unable to bind | NewReceiverThread.");
			writeError();
			return;
		}

		Global.Log("severSocketChannel Bind successful | NewReceiverThread");
		while (true)
		{
			try
			{
				socketChannel = serverSocketChannel.accept();
				getFilename(socketChannel);
				readFromSocket(socketChannel, filename);
			}
			catch (IOException | ClassNotFoundException e)
			{
				Global.Log("Dropped a file transfer. Request transfer again.");
				writeError();
				continue;
			}
			writeSuccess();
		}

	}

	private void writeSuccess()
	{
		JOptionPane.showMessageDialog(Global.window.frmChatServerV,
				"File received : " + filename + "\nFull path: " + destFolder
						+ filename);
	}

	private void getFilename(SocketChannel socketChannel) throws IOException,
			ClassNotFoundException
	{
		Global.Log("Reading filename | NewFileReceiveThread.");
		ObjectInputStream ois = new ObjectInputStream(socketChannel.socket()
				.getInputStream());
		Global.Log("Got OIS | NewFileReceiveThread.");
		filename = (String) ois.readObject();
		Global.Log("filename = " + filename + " | NewFileReceiveThread.");
	}

	private void readFromSocket(SocketChannel socketChannel, String filename)
			throws IOException
	{
		Global.Log("Receiving file " + filename + " | NewFileReceiveThread.");
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
		Global.Log("File receive complete| NewFileReceiveThread.");
		fileChannel.close();
		socketChannel.close();
		file.close();
	}

	private void writeError()
	{
		synchronized (msgBox)
		{
			msgBox.append("\n" + filename + " transfer failed.");
		}
		return;
	}
}
