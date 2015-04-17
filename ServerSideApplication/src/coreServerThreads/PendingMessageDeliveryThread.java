package coreServerThreads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.Message;
import coreServer.PendingQueue;
import coreServer.User;

public class PendingMessageDeliveryThread extends Thread
{
	private PendingQueue pendingQueue;
	private Message curMessage;
	private Socket clientSocket;
	private ObjectOutputStream oos;
	private String destIP;
	private int msgPort = 2500;

	public PendingMessageDeliveryThread(User user, String destIP)
	{
		this.destIP = destIP;
		this.pendingQueue = user.pendingMessageQueue;
	}

	public void run()
	{
		synchronized (pendingQueue)
		{
			int maxSends = pendingQueue.getSize();
			while (maxSends > 0)
			{
				curMessage = pendingQueue.getNewPendingItem();
				if (curMessage == null)
					break;
				try
				{
					deliverCurrentMessage();
				}
				catch (IOException anyException)
				{
					pendingQueue.addPendingItem(curMessage);
				}
				System.out.println("Delivered a pending message");
				maxSends--;
			}
		}
	}

	private void deliverCurrentMessage() throws IOException
	{
		clientSocket = new Socket(destIP, msgPort);
		oos = new ObjectOutputStream(clientSocket.getOutputStream());
		oos.writeObject(curMessage);
		clientSocket.close();
		return;
	}
}
