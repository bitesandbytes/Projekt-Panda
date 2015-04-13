package coreServerThreads;

public class MessageToFileWriterThread extends Thread
{
	public void run()
	{
		System.out
				.println("Missed a client. Now writing into file on the server.");
	}
}
