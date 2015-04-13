package coreServerThreads;

import java.io.IOException;
import java.net.ServerSocket;

import coreServer.RequestQueue;

public class MessageReceiverThread extends Thread
{
	private static final int msgListenPort = 2400;
	private ServerSocket msgServerSocket;
	private RequestQueue queue;

	public MessageReceiverThread(RequestQueue queue)
	{
		super();
		this.queue = queue;
	}

	public void run()
	{
		System.out.println("Listening for Messages @ port 2400.");
		try
		{
			msgServerSocket = new ServerSocket(msgListenPort);
		}
		catch (IOException e)
		{
			System.out
					.println("Unable to bind to the given port. Close any app using that port. \nTerminating Server.");
			System.exit(0);
		}
		while (true)
		{
			try
			{
				(new msgReceiveWorkerThread(msgServerSocket.accept(), queue))
						.start();
			}
			catch (IOException e)
			{
				System.out.println("Dropped a request.");
				continue;
			}
		}
	}
}
