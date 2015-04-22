package coreServerThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.FileControlPacket;
import coreServer.UserMap;

public class FileRequestHandlerThread extends Thread
{
	private static int destPort = 4500;
	private Socket srcSocket;
	private UserMap userMap;
	private ObjectInputStream srcOIS;
	private FileControlPacket controlPacket;
	private int retryCount = 3;
	private ObjectOutputStream srcOOS;

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
		String destIP = userMap.getCurrentIP(controlPacket.payload);
		if(destIP == null)
		{
			writeSrc(new FileControlPacket(true, "0.0.0.0", true));
			return;
		}
		else
		{
			//TODO:: write controlPacket to client at destIP, destport as 4500.
			try
			{
				Socket destSocket = new Socket(destIP, destPort);
			}
			catch (IOException e)
			{
				System.out.println();
			}
			
			//TODO:: Receive acknowledgement.
			//TODO:: Write a file control packet to client with open socket.
		}
	}
	
	private void writeSrc(FileControlPacket packet)
	{
		try
		{
			srcOOS = new ObjectOutputStream(srcSocket.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println("Unable to get OOS for src. Dropping request.");
			return;
		}
		controlPacket.isServer = true;
		controlPacket.payload = "0.0.0.0";
		controlPacket.isIP = true;
		
		while (retryCount > 0)
		{
			try
			{
				srcOOS.writeObject(controlPacket);
				srcSocket.close();
				return;
			}
			catch (IOException e)
			{
				retryCount--;
				if(retryCount == 0)
				{
					System.out.println("Unable to write to src. Dropping request.");
					return;
				}
				continue;
			}
		}
		return;
		
	}
}
