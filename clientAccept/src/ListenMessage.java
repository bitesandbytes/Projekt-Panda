

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenMessage extends Thread
{
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private Message msgObj;
	private ObjectInputStream objStream;

	ListenMessage(ServerSocket serverSocket)
	{
		super("Message-Listener");
		this.serverSocket = serverSocket;
		msgObj = null;
	}

	public void run()
	{
		System.out.println("Started message listener.");
		while (true)
		{
			try
			{
				clientSocket = serverSocket.accept();
			}
			catch (IOException e)
			{
				System.out.println("Unable to bind to client.");
				e.printStackTrace();
			}
			try
			{
				objStream = new ObjectInputStream(clientSocket.getInputStream());
			}
			catch(EOFException eof)
			{
				
			}
			catch (IOException e)
			{
				System.out
						.println("Failed to obtain object output stream from client socket.");
				e.printStackTrace();
			}
			try
			{
				while (msgObj == null)
				{
					msgObj = (Message) objStream.readObject();
				}
			}
			catch (EOFException io)
			{
				try
				{
					clientSocket.close();
					continue;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			catch (ClassNotFoundException | IOException e)
			{
				System.out.println("ClassNotFoundException.");
				e.printStackTrace();
			}
			System.out.println("Message received from "
					+ clientSocket.getInetAddress() + ", message = " + msgObj.message);
			msgObj = null;
		}
	}
}
