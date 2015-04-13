package coreServerThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import common.Message;

import coreServer.Request;
import coreServer.RequestQueue;

public class msgReceiveWorkerThread extends Thread
{
	private Socket clientSocket;
	private ObjectInputStream ois;
	private Message msgObj;
	private RequestQueue queue;
	private int retryCount;

	public msgReceiveWorkerThread(Socket client, RequestQueue queue)
	{
		super();
		this.clientSocket = client;
		this.queue = queue;
		this.retryCount = 3;
	}

	public void run()
	{
		try
		{
			ois = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch (IOException e)
		{
			System.out
					.println("Couldn't get OIS from client socket. Terminating thread.");
			return;
		}

		while (retryCount > 0)
		{
			try
			{
				msgObj = (Message) ois.readObject();
				break;
			}
			catch (ClassNotFoundException | IOException e)
			{
				System.out
						.println("Invalid object received from client. Retrying..");
				retryCount--;
			}
		}

		queue.addRequest(new Request(msgObj));

		queue.notify();

		return;
	}

}
