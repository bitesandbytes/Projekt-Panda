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

	private UserMap userMap;

	private Socket srcSocket;
	private Socket destSocket;

	private ObjectInputStream srcOIS;
	private ObjectInputStream destOIS;

	private ObjectOutputStream srcOOS;
	private ObjectOutputStream destOOS;

	private FileControlPacket controlPacket;

	private int retryCount = 3;

	private String destIP;

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
				if (retryCount == 0)
				{
					System.out
							.println("Class Cast Exception. Dropping request.");
					return;
				}
				continue;
			}
		}
		retryCount = 3;
		destIP = userMap.getCurrentIP(controlPacket.payload);
		if (destIP == null)
		{
			writeSrc(new FileControlPacket(true, "0.0.0.0", true));
			return;
		}
		else
		{
			while (retryCount > 0)
			{
				try
				{
					controlPacket = contactDestClient();
					break;
				}
				catch (Exception e)
				{
					if (retryCount == 0)
					{
						writeSrc(new FileControlPacket(true, "0.0.0.0", true));
						return;
					}
					else
						continue;
				}
			}
			writeSrc(new FileControlPacket(true, destIP, true));
		}
	}

	// Writes "packet" to srcSocket.
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
				if (retryCount == 0)
				{
					System.out
							.println("Unable to write to src. Dropping request.");
					return;
				}
				continue;
			}
		}
		return;

	}

	// Writes "controlPacket" to destSocket and returns the received packet.
	private FileControlPacket contactDestClient() throws Exception
	{
		destSocket = new Socket(destIP, destPort);
		destOOS = new ObjectOutputStream(destSocket.getOutputStream());
		destOOS.writeObject(controlPacket);
		destOIS = new ObjectInputStream(destSocket.getInputStream());
		FileControlPacket receivedPacket = (FileControlPacket) destOIS
				.readObject();
		return receivedPacket;
	}
}
