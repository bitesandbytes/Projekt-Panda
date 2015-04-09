public class Client
{
	public static ClientSendingThread sendMessagesThread;
	public static Boolean connected = true;

	public static void main(String[] args) throws InterruptedException
	{
		sendMessagesThread = new ClientSendingThread();
		sendMessagesThread.start();
		endSending();
	}

	private static void endSending() throws InterruptedException
	{
		synchronized (Client.connected)
		{
			while (Client.connected)
			{
				Client.connected.wait();
			}
			System.exit(0);
		}
	}
}
