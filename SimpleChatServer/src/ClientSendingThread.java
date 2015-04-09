import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientSendingThread extends Thread
{
	private String curMessage;
	private final String destIP = "192.168.1.208";
	private final int destPort = 2500;
	private Scanner consoleInput;
	private Socket clientSocket;
	private ObjectOutputStream outStream;
	public boolean isRunning;
	
	ClientSendingThread()
	{
		super();
		curMessage = null;
		consoleInput = new Scanner(System.in);
		isRunning = false;
	}
	
	public void run()
	{
		isRunning = true;
		try
		{
			clientSocket = new Socket(destIP, destPort);
		}
		catch (IOException e)
		{
			System.out.println("Unable to connect to destination. Terminating application.");
			e.printStackTrace();
		}
		try
		{
			outStream = new ObjectOutputStream(clientSocket.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println("Unable to get OOS.");
			e.printStackTrace();
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
			}
			catch (IOException e)
			{
				System.out.println("Lost connection to the remote client. Terminating application.");
				isRunning = false;
				try
				{
					clientSocket.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
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
			isRunning = false;
			return;
		}
	}
}
