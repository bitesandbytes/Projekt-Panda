import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientReceivingThread extends Thread
{
	private String rcvMessage;
	private final int listenPort = 2500;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectInputStream inStream;

	ClientReceivingThread()
	{
		super();
		rcvMessage = null;
		try
		{
			serverSocket = new ServerSocket(listenPort);
		}
		catch (IOException e)
		{
			System.exit(0);
		}
	}

	public void run()
	{
		try
		{
			clientSocket = serverSocket.accept();
		}
		catch (IOException e)
		{
			System.out
					.println("Unable to bind to listener. Terminating application.");
			stopRunning();
			return;
		}
		try
		{
			inStream = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch (IOException e)
		{
			System.out.println("Unable to get OOS.");
			stopRunning();
			return;
		}
		synchronized (Client.connected)
		{
			Client.connected = true;
		}
		while (true)
		{

			try
			{
				rcvMessage = (String) inStream.readObject();
				System.out.println("bolji :" + rcvMessage);
			}
			catch (IOException | ClassNotFoundException e)
			{
				System.out
						.println("Unable to read String from Client. Terminating application.");
				stopRunning();
				try
				{
					clientSocket.close();
				}
				catch (IOException e1)
				{
					stopRunning();
					break;
				}
				break;
			}
		}
		return;
	}

	public void stopRunning()
	{
		synchronized (Client.connected)
		{
			Client.connected = false;
			Client.connected.notify();
		}
		return;
	}
}
