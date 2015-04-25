package coreClient;

import java.util.LinkedList;
import java.util.Queue;

import common.Message;

public class MessageQueue
{

	private Queue<Message> messageQueue;

	public MessageQueue()
	{
		messageQueue = new LinkedList<Message>();
	}

	public void addMessage(Message message)
	{
		
		synchronized (messageQueue)
		{
			messageQueue.add(message);
		}
		System.out.println("Added msg : " + message.sourceNick + "->"
				+ message.destNick + ":" + message.content);
	}

	public Message getMessage()
	{
		Message returnMessage = null;
		synchronized (messageQueue)
		{
			returnMessage = messageQueue.poll();
		}
		System.out.println("Removed msg : " + returnMessage.sourceNick + "->"
				+ returnMessage.destNick + ":" + returnMessage.content);
		return returnMessage;
	}

	public int size()
	{
		int size = 0;
		synchronized (messageQueue)
		{
			size = messageQueue.size();
		}
		return size;
	}

}
