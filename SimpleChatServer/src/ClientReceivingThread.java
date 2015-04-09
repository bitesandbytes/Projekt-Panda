import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientReceivingThread extends Thread
{
	private String curMessage;
	private final int destPort = 2500;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectInputStream inStream;
	
	ClientReceivingThread()
	{
		super();
		curMessage = null;
		new Scanner(System.in);
		try {
			serverSocket = new ServerSocket(destPort);
		} catch (IOException e) {
			stopRunning();
			e.printStackTrace();
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
			System.out.println("Unable to connect to destination. Terminating application.");
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
		synchronized(Client.connected)
		{
			Client.connected = true;
		}
		curMessage = "";
		while(true)
		{
			
			try
			{
				curMessage = (String) inStream.readObject();
				System.out.println("sauce :"+curMessage);
			}
			catch (IOException | ClassNotFoundException e)
			{
				System.out.println("Unable to read String from Client. Terminating application.");
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
		synchronized(Client.connected)
		{
			Client.connected = false;
			Client.connected.notify();
		}
	}
}
