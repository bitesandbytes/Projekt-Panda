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
		System.out.println("Starting file request listener thread.");
		try
		{
			serverSocket = new ServerSocket(fileRequestListenPort);
		}
		catch (IOException e1)
		{
			System.out.println("Unable to bind to 4400. Terminate any servers binding to that port.");
			return;
		}
		System.out.println("Got file server socket.");
		while(true)
		{
			try
			{
				(new FileRequestHandlerThread(userMap, serverSocket.accept())).start();
				System.out.println("Got new file request. | FileRequestListenerThread");
			}
			catch (IOException e)
			{
				System.out.println("Dropped a request.");
				continue;
			}
		}
	}
}
