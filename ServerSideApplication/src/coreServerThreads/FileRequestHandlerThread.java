package coreServerThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import common.FileControlPacket;
import coreServer.UserMap;

public class FileRequestHandlerThread extends Thread
{
	private Socket srcSocket;
	private UserMap userMap;
	private ObjectInputStream srcOIS;
	private FileControlPacket controlPacket;
	private int retryCount = 3;

	public FileRequestHandlerThread(UserMap map, Socket srcSocket)
	{
		this.userMap = map;
		this.srcSocket = srcSocket;
	}

	public void run()
	{
		try
		{
			srcOIS = new ObjectInputStream(srcSocket.getInputStream());
		}
		catch (IOException e)
		{
			System.out
					.println("Unable to get OIS for src socket. Dropping request.");
			return;
		}
		while (retryCount > 0)
		{
			try
			{
				controlPacket = (FileControlPacket) srcOIS.readObject();
				break;
			}
			catch (ClassNotFoundException | IOException e)
			{
				retryCount--;
				if(retryCount == 0)
				{
					System.out.println("Unable to cast input data. Dropping request.");
					return;
				}
				continue;
			}
		}
		retryCount = 3;
	}
}
