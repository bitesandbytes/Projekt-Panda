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
		this.notify();
	}

	public Message getMessage()
	{
		Message returnMessage = null;
		synchronized (messageQueue)
		{
			returnMessage = messageQueue.poll();
		}
		return returnMessage;
	}

	public int size()
	{
		return messageQueue.size();
	}

}
