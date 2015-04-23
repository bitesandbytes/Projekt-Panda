package serverMain;

import coreServer.RequestQueue;
import coreServer.UserMap;
import coreServerThreads.ControlThread;
import coreServerThreads.FileRequestListenerThread;
import coreServerThreads.MessageReceiverThread;
import coreServerThreads.RequestProcessorThread;

public class ServerMain
{
	public static UserMap userMap = new UserMap();
	public static RequestQueue queue = new RequestQueue();

	public static void main(String[] args)
	{
		ControlThread controlThread = new ControlThread(userMap);
		MessageReceiverThread messageReciever = new MessageReceiverThread(
				queue);
		RequestProcessorThread messageSender = new RequestProcessorThread(
				queue, userMap);
		FileRequestListenerThread fileRequests = new FileRequestListenerThread(userMap);
		controlThread.start();
		messageReciever.start();
		messageSender.start();
		fileRequests.start();
	}
}
