package clientCoreThreads;

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

import javax.swing.JButton;
import javax.swing.JTextArea;

import coreClient.Global;

public class FileSenderThread extends Thread
{
	public String filePath;
	public String filename;
	private SocketChannel socketChannel = null;
	private String destIP;
	private final int fileSendPort = Global.clientFilePort;
	private JTextArea msgBox;
	private JButton fileButton;

	public FileSenderThread(String path, String name, String dip,
			JTextArea msgBox, JButton fileButton)
	{
		super();
		this.filePath = path;
		this.filename = name;
		this.destIP = dip;
		this.msgBox = msgBox;
		this.fileButton = fileButton;
	}

	@SuppressWarnings("resource")
	public void run()
	{
		fileButton.setEnabled(false);
		socketChannel = null;
		try
		{
			socketChannel = SocketChannel.open();
			SocketAddress socketAddress = new InetSocketAddress(destIP,
					fileSendPort);
			socketChannel.connect(socketAddress);
			Global.Log("Connected. Sending filename | FileSenderThread.");

		}
		catch (IOException e)
		{
			Global.Log("Remote socket connection failed | FileSenderThread.");
			writeError();
			return;
		}
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(socketChannel
					.socket().getOutputStream());
			oos.writeObject(filename);
		}
		catch (IOException e1)
		{
			Global.Log("Unable to write filename. Try again. ");
			writeError();
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
			Global.Log("File Successfully Sent | FileSenderThread.");
			socketChannel.close();
			aFile.close();
			Global.window.enableFileTransfer();
			return;
		}
		catch (FileNotFoundException e)
		{
			Global.Log("Unable to locate File | FileSenderThread.");
			writeError();
			return;
		}
		catch (IOException e)
		{
			Global.Log("IOException | FileSenderThread.");
			writeError();
			return;
		}
		catch (InterruptedException e)
		{
			Global.Log("Thread Sleep Interrupted | FileSenderThread.");
			writeError();
			return;
		}
	}

	private void writeError()
	{
		synchronized (msgBox)
		{
			msgBox.append("\n" + filename + " transfer failed. Try again.");
		}
		Global.window.enableFileTransfer();
	}
}
