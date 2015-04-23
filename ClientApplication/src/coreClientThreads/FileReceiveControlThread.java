package coreClientThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import common.FileControlPacket;
import coreClient.Global;

public class FileReceiveControlThread extends Thread
{
	private Socket serverComSocket;
	private ServerSocket serverSocket;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private FileControlPacket controlPacket;
	private final static int fileControlReceivePort = Global.clientFilePort;
	private String receiveFileName;

	public FileReceiveControlThread()
	{
		super();
	}

	private void receiveFileControlPacket() throws IOException,
			ClassNotFoundException
	{
		System.out.println("FRCT: Accepting connection");
		serverComSocket = serverSocket.accept();
		System.out.println("FRCT: Initializing input Stream");
		inStream = new ObjectInputStream(serverComSocket.getInputStream());
		System.out.println("FRCT: Attempting to read FileControlPacket Object");
		controlPacket = (FileControlPacket) inStream.readObject();
		return;
	}

	private void sendFileControlPacket() throws IOException
	{
		System.out.println("FRCT: Initializing output Stream");
		outStream = new ObjectOutputStream(serverComSocket.getOutputStream());
		System.out.println("FRCT: Writing controlPacket");
		outStream.writeObject(controlPacket);
		outStream.flush();
		System.out.println("FRCT: Closing Socket");
		serverComSocket.close();
		return;
	}

	public void run()
	{
		try
		{
			serverSocket = new ServerSocket(fileControlReceivePort);
		}
		catch (IOException e)
		{
			System.out.println("Unable to bind to " + fileControlReceivePort);
			System.exit(0);
		}
		System.out.println("Started FileReceiveControlThread");
		boolean hasFailed = false;
		while (true)
		{
			int retryCount = 3;
			while (retryCount > 0)
			{
				try
				{
					receiveFileControlPacket();
					sendFileControlPacket();
				}
				catch (IOException e)
				{
					if (retryCount == 0)
					{
						System.out
								.println("Dropping file request | FileReceiveControlThread.");
						hasFailed = true;
						break;
					}
					else
					{
						retryCount--;
						continue;
					}
				}
				catch (ClassNotFoundException e)
				{
					System.out
							.println("Dropping file request | FileReceiveControlThread.");
					hasFailed = true;
					break;
				}
			}
			if (hasFailed)
				continue;
			receiveFileName = controlPacket.fileName;
			(new FileReceiverThread(receiveFileName)).start();
			break;
		}
	}
}
