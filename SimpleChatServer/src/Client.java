public class Client
{
	private static ClientSendingThread sendMessagesThread;
	private static ClientReceivingThread rcvMessagesThread;
	public static Boolean connected = true;

	public static void main(String[] args) throws InterruptedException
	{
		sendMessagesThread = new ClientSendingThread();
		rcvMessagesThread = new ClientReceivingThread();
		rcvMessagesThread.start();
		Thread.sleep(5000);
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
