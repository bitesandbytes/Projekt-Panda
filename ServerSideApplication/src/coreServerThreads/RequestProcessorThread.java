package coreServerThreads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.Message;

import coreServer.PendingQueue;
import coreServer.Request;
import coreServer.RequestQueue;
import coreServer.UserMap;

public class RequestProcessorThread extends Thread
{
	private RequestQueue queue;
	private UserMap userMap;
	private Socket client;
	private ObjectOutputStream oos;
	private final int msgPort = 2500;
	private String destIP;
	private Request currentRequest;
	private PendingQueue pendingQueue;

	public RequestProcessorThread(RequestQueue queue, UserMap map)
	{
		this.queue = queue;
		this.userMap = map;
	}

	public void run()
	{
		System.out.println("Started request processor thread.");
		while (true)
		{
			currentRequest = null;
			synchronized (queue)
			{
				while (queue.isEmpty())
				{
					try
					{
						queue.wait();
					}
					catch (InterruptedException e)
					{
						break;
					}
					System.out.println("Woke up request processor thread.");
				}

				while (!queue.isEmpty())
				{
					currentRequest = queue.removeRequest();

					if (currentRequest == null)
						break;
					else
						writeMsg();
				}
			}
		}
	}

	private void writeMsg()
	{
		synchronized (userMap)
		{
			pendingQueue = userMap
					.getPendingMessageQueueFor(currentRequest.msgContent.destNick);
			destIP = userMap.getCurrentIP(currentRequest.msgContent.destNick);
		}

		if (pendingQueue == null)
		{
			System.out.println("Unauthorized access.");
		}

		if (currentRequest.retryCount < 0 || destIP == null)
		{
			System.out.println("Adding to pending queue.");
			synchronized (pendingQueue)
			{
				pendingQueue.addPendingItem(currentRequest.msgContent);
			}
			return;
		}

		currentRequest.retryCount--;
		try
		{
			writeObj(currentRequest.msgContent);
		}
		catch (IOException e)
		{
			pendingQueue.addPendingItem(currentRequest.msgContent);
			return;
		}
		return;
	}

	private void writeObj(Message message) throws IOException
	{
		client = new Socket(destIP, msgPort);
		oos = new ObjectOutputStream(client.getOutputStream());
		oos.writeObject(message);
		client.close();
		return;
	}
}