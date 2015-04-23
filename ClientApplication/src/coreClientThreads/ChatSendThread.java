package coreClientThreads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.Message;
import coreClient.Global;
import coreClient.MessageQueue;

public class ChatSendThread extends Thread
{
	private Socket messageSenderSocket;
	private ObjectOutputStream outStream;
	private Message currentMessage;
	public MessageQueue messageQueue;

	private final static String serverIP = Global.serverIP;
	private final static int messageSendPort = Global.serverMsgPort;

	public ChatSendThread()
	{
		super();
		messageQueue = new MessageQueue();
		(new WriteUserInputThread(messageQueue)).start();
	}

	private void sendMessageObj() throws IOException
	{
		System.out.println("CST: Establishing a socket connection");
		messageSenderSocket = new Socket(serverIP, messageSendPort);
		System.out.println("CST: Intializing output Stream");
		outStream = new ObjectOutputStream(
				messageSenderSocket.getOutputStream());

		System.out.println("CST: Current Message " + currentMessage.destNick
				+ " with content " + currentMessage.content);
		System.out.println("CST: Sending message...");
		outStream.writeObject(currentMessage);
		outStream.flush();
		System.out.println("CST: Message Sent");
		return;
	}

	public void run()
	{
		System.out.println("Started Chat Sender Thread");
		/*
		 * For Feedback. One can have a server input coming in here saying true
		 * of false.Gives indication whether server was able to request a
		 * connection and currentFriend accepted it.
		 */

		while (true)
		{
			waitForNotify();
			deliverMessage();
		}
	}

	private void waitForNotify()
	{

		while (messageQueue.size() == 0)
		{
			synchronized (messageQueue)
			{
				try
				{
					messageQueue.wait();
				}
				catch (InterruptedException e)
				{
					continue;
				}
			}
		}
		return;
	}

	private void deliverMessage()
	{
		while (messageQueue.size() > 0)
		{
			currentMessage = messageQueue.getMessage();
			int retryCount = 3;
			while (retryCount > 0)
			{
				try
				{
					sendMessageObj();
					break;
				}
				catch (IOException ex)
				{
					retryCount--;
					System.out.println("CST: Retrying send ...");
					if (retryCount == 0)
					{
						System.out.println("CST: Unable to send to server.");
						return;
					}
				}
			}
		}
	}
}
