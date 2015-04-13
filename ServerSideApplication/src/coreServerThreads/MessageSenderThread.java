package coreServerThreads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import coreServer.Request;
import coreServer.RequestQueue;
import coreServer.UserMap;

public class MessageSenderThread extends Thread
{
	private RequestQueue queue;
	private UserMap userMap;
	private Socket client;
	private ObjectOutputStream oos;
	private final int controlPort = 3500;
	private final int msgPort = 2500;
	private String destIP;
	private Request currentRequest;
	
	public MessageSenderThread(RequestQueue queue, UserMap map)
	{
		this.queue = queue;
		this.userMap = map;
	}

	public void run()
	{
		while (true)
		{
			currentRequest = null;
			while (currentRequest == null)
			{
				synchronized (queue)
				{
					try
					{
						queue.wait();
					}
					catch (InterruptedException e)
					{
						break;
					}
				}
			}
			currentRequest = queue.removeRequest();

			if (currentRequest == null)
				continue;

			else if (currentRequest.isMessage)
				writeMsg();

			else
				writeControl();
		}
	}

	private void writeMsg()
	{
		if (currentRequest.retryCount < 0)
		{
			System.out
					.println("Dropped a message packet. Retry failed 3 times.");
			return;
		}

		destIP = userMap.getCurrentIP(currentRequest.msgContent.destNick);
		currentRequest.retryCount--;
		if (destIP == null)
		{
			// TODO:: Write messages to file.
			return;
		}
		else
		{
			try
			{
				client = new Socket(destIP, msgPort);
			}
			catch (IOException e)
			{
				// Should never happen.
			}
		}
		try
		{
			oos = new ObjectOutputStream(client.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println("Unable to obtain OOS. Enqueueing again.");
			queue.addRequest(currentRequest);
			return;
		}
		try
		{
			oos.writeObject(currentRequest.msgContent);
		}
		catch (IOException e)
		{
			System.out.println("OOS failed. Trying again.");
			queue.addRequest(currentRequest);
		}
		try
		{
			client.close();
		}
		catch (IOException e)
		{
			System.out.println("Client Socket close failed.");
			return;
		}
	}

	private void writeControl()
	{
		if (currentRequest.retryCount < 0)
		{
			System.out
					.println("Dropped a message packet. Retry failed 3 times.");
			return;
		}

		destIP = userMap.getCurrentIP(currentRequest.controlObj.nick);
		currentRequest.retryCount--;

		if (destIP == null)
		{
			// TODO:: Write messages to file.
			return;
		}

		else
		{
			try
			{
				client = new Socket(destIP, controlPort);
			}
			catch (IOException e)
			{
				// Should never happen.
			}
		}
		try
		{
			oos = new ObjectOutputStream(client.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println("Unable to obtain OOS. Enqueueing again.");
			queue.addRequest(currentRequest);
			return;
		}
		try
		{
			oos.writeObject(currentRequest.controlObj);
		}
		catch (IOException e)
		{
			System.out.println("OOS failed. Trying again.");
			queue.addRequest(currentRequest);
		}
		try
		{
			client.close();
		}
		catch (IOException e)
		{
			System.out.println("Client Socket close failed.");
			return;
		}
		return;
	}
}
