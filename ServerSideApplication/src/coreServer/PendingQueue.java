package coreServer;

import java.util.LinkedList;
import java.util.Queue;

import common.Message;

public class PendingQueue
{
	private static Queue<Message> primaryQueue = new LinkedList<Message>();
	private static Queue<Message> secondaryQueue = new LinkedList<Message>();

	public void addNewItem(Message m)
	{
		primaryQueue.add(m);
	}

	public void addPendingItem(Message m)
	{
		secondaryQueue.add(m);
	}

	public Message getNewPendingItem()
	{
		if (secondaryQueue.isEmpty())
			return primaryQueue.poll();
		else
			return secondaryQueue.poll();
	}

	public int getSize()
	{
		return (primaryQueue.size() + secondaryQueue.size());
	}
}
