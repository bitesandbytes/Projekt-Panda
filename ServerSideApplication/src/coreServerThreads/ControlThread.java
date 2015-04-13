package coreServerThreads;

import java.io.IOException;
import java.net.ServerSocket;

import coreServer.UserMap;

public class ControlThread extends Thread
{
	private static final int loginListenPort = 3400;
	private UserMap userMap = null;
	private ServerSocket loginServerSocket;

	public ControlThread(UserMap userMap)
	{
		this.userMap = userMap;
	}

	public void run()
	{
		try
		{
			loginServerSocket = new ServerSocket(loginListenPort);
		}
		catch (IOException e)
		{
			System.out
					.println("Unable to bind to the given port. Close any app using that port. \nTerminating Server.");
			System.exit(0);
		}

		while (true)
		{
			System.out.println("Waiting for new connection.");
			synchronized (userMap)
			{
				try
				{
					new LoginThread(userMap, loginServerSocket.accept())
							.start();
				}
				catch (IOException e)
				{
					System.out
							.println("Unable to accept connections. \nDebug.");
				}
			}
		}
	}
}
