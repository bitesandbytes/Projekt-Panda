package clientCoreThreads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.Message;
import coreClient.Global;
import coreClient.MessageQueue;

public class ChatSendThread extends Thread
{
	private Socket msgSendSocket;
	private ObjectOutputStream outStream;
	private Message curMessage;
	public MessageQueue msgQueue;

	private final static String serverIP = Global.serverIP;
	private final static int messageSendPort = Global.serverMsgPort;

	public ChatSendThread()
	{
		super();
		msgQueue = Global.msgQueue;
	}

	private void sendMessageObj() throws IOException
	{
		Global.Log("CST: Establishing a socket connection");
		msgSendSocket = new Socket(serverIP, messageSendPort);
		Global.Log("CST: Intializing output Stream");
		outStream = new ObjectOutputStream(msgSendSocket.getOutputStream());

		Global.Log("CST: Current Message " + curMessage.destNick
				+ " with content " + curMessage.content);
		Global.Log("CST: Sending message...");
		outStream.writeObject(curMessage);
		outStream.flush();
		Global.Log("CST: Message Sent");
		return;
	}

	public void run()
	{
		Global.Log("Started Chat Sender Thread");
		while (true)
		{
			waitForNotify();
			deliverMessage();
		}
	}

	private void waitForNotify()
	{

		while (msgQueue.size() == 0)
		{
			synchronized (msgQueue)
			{
				try
				{
					msgQueue.wait();
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
		while (msgQueue.size() > 0)
		{
			curMessage = msgQueue.getMessage();
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
					Global.Log("CST: Retrying send ...");
					if (retryCount == 0)
					{
						Global.Log("CST: Unable to send to server.");
						return;
					}
				}
			}
		}
	}
}
