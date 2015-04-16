package serverMain;

import coreServer.RequestQueue;
import coreServer.UserMap;
import coreServerThreads.ControlThread;
import coreServerThreads.MessageReceiverThread;
import coreServerThreads.MessageSenderThread;
import coreServerThreads.PendingMessageDeliveryThread;

public class ServerLoginListener
{
	public static UserMap userMap = new UserMap();
	public static RequestQueue queue = new RequestQueue();

	public static void main(String[] args)
	{
		ControlThread controlThread = new ControlThread(userMap);
		MessageReceiverThread messageRetriever = new MessageReceiverThread(
				queue);
		MessageSenderThread messageSender = new MessageSenderThread(queue,
				userMap);
		PendingMessageDeliveryThread pendingMessageDeliverThread = new PendingMessageDeliveryThread(
				userMap);
		controlThread.start();
		messageRetriever.start();
		messageSender.start();
		pendingMessageDeliverThread.start();
	}
}
