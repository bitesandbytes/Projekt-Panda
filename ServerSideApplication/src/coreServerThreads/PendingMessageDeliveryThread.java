package coreServerThreads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import common.Message;

import coreServer.UserMap;

public class PendingMessageDeliveryThread extends Thread
{
	private Queue<Message> messageQueue;
	private Queue<Message> secondaryQueue;
	private UserMap userMap;
	private Message curMessage;
	private Socket clientSocket;
	private ObjectOutputStream oos;
	private String destIP;
	private int msgPort = 2400;

	public PendingMessageDeliveryThread(UserMap userMap)
	{
		this.messageQueue = new LinkedList<Message>();
		this.secondaryQueue = new LinkedList<Message>();
		this.userMap = userMap;
	}

	public void run()
	{
		synchronized (userMap)
		{
			while (true)
			{
				curMessage = null;
				while (!messageQueue.isEmpty())
				{
					curMessage = messageQueue.poll();
					try
					{
						deliverCurrentMessage();
					}
					catch (IOException anyException)
					{
						secondaryQueue.add(curMessage);
					}
				}
				messageQueue = secondaryQueue;
				secondaryQueue = new LinkedList<Message>();
				try
				{
					userMap.wait();
				}
				catch (InterruptedException e)
				{
					continue;
				}
			}
		}
	}

	private void deliverCurrentMessage() throws IOException
	{

		synchronized (userMap)
		{
			destIP = userMap.getCurrentIP(curMessage.destNick);
		}
		clientSocket = new Socket(destIP, msgPort);
		oos = new ObjectOutputStream(clientSocket.getOutputStream());
		oos.writeObject(curMessage);
		clientSocket.close();
		return;
	}
}
