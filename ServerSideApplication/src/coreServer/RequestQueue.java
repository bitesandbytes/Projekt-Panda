package coreServer;

import java.util.*;

public class RequestQueue
{
	private Queue<Request> requestQueue;

	public RequestQueue()
	{
		requestQueue = new LinkedList<Request>();
	}

	public void addRequest(Request request)
	{
		synchronized (requestQueue)
		{
			requestQueue.add(request);
		}
	}

	public Request removeRequest()
	{
		Request returnRequest = null;
		synchronized (requestQueue)
		{
			returnRequest = requestQueue.poll();
		}
		return returnRequest;
	}

	public boolean isEmpty()
	{
		synchronized (requestQueue)
		{
			return requestQueue.isEmpty();
		}
	}
}
