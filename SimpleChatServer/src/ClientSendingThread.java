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
			if(curMessage.compareTo("exit"))
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
				System.exit(0);
				e.printStackTrace();
			}
		}
	}
}
