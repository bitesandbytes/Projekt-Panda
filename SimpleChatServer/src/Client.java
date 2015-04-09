public class Client
{
	public static ClientSendingThread sendMessagesThread;
	public static ClientReceivingThread rcvMessagesThread;
	public static Boolean connected = true;

	public static void main(String[] args) throws InterruptedException
	{
		sendMessagesThread = new ClientSendingThread();
		rcvMessagesThread.start();
		Thread.sleep(50);
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
