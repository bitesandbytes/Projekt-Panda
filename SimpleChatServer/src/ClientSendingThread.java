import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientSendingThread extends Thread
{
	private String curMessage;
	private final String destIP = "192.168.1.221";
	private final int destPort = 2500;
	private Scanner consoleInput;
	private Socket clientSocket;
	private ObjectOutputStream outStream;
	
	ClientSendingThread()
	{
		super();
		curMessage = null;
		consoleInput = new Scanner(System.in);
	}
	
	public void run()
	{
		try
		{
			clientSocket = new Socket(destIP, destPort);
		}
		catch (IOException e)
		{
			System.out.println("Unable to connect to destination. Terminating application.");
			stopRunning();
			return;
		}
		try
		{
			outStream = new ObjectOutputStream(clientSocket.getOutputStream());
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
		while(curMessage!=null)
		{
			curMessage = consoleInput.nextLine();
			if(curMessage.compareTo("exit") == 0)
			{
				curMessage = null;
				continue;
			}
			try
			{
				outStream.writeObject(curMessage);
				outStream.flush();
			}
			catch (IOException e)
			{
				System.out.println("Lost connection to the remote client. Terminating application.");
				stopRunning();
				try
				{
					clientSocket.close();
				}
				catch (IOException e1)
				{
					stopRunning();
					return;
				}
				return;
			}
		}
		try
		{
			clientSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Unable to close socket. Terminating application.");
			stopRunning();
			return;
		}
		stopRunning();
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
