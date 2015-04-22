package coreServerThreads;

import java.io.IOException;
import java.net.ServerSocket;

import coreServer.UserMap;

public class FileRequestListenerThread extends Thread
{
	private static int fileRequestListenPort = 4400;
	private UserMap userMap;
	private ServerSocket serverSocket;
	
	public FileRequestListenerThread(UserMap userMap)
	{
		this.userMap = userMap;
	}
	
	public void run()
	{
		try
		{
			serverSocket = new ServerSocket(fileRequestListenPort);
		}
		catch (IOException e1)
		{
			System.out.println("Unable to bind to 4400. Terminate any servers binding to that port.");
			return;
		}
		while(true)
		{
			try
			{
				(new FileRequestHandlerThread(userMap, serverSocket.accept())).start();
			}
			catch (IOException e)
			{
				System.out.println("Dropped a request.");
				continue;
			}
		}
	}
}
