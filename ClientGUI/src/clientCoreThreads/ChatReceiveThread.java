package clientCoreThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;

import common.Message;
import coreClient.Global;

public class ChatReceiveThread extends Thread
{
	private ServerSocket msgServer;
	private ObjectInputStream inStream;
	private Message currentMessage;
	private final static int msgRcvPort = Global.localMsgReceivePort;
	public JTextArea msgBox;
	
	public ChatReceiveThread(JTextArea msgBox){
		super();
		this.msgBox = msgBox;
		
	}

	public void run()
	{
		System.out.println("Spawned ChatReceiveThread");
		try
		{
			msgServer = new ServerSocket(msgRcvPort);
		}
		catch (IOException e)
		{
			System.out.println("FATAL ERROR : Unable to bind to local port @ "
					+ msgRcvPort);
			System.out.println("Terminating application.");
			System.exit(0);
		}
		while (true)
		{
			try
			{
				currentMessage = getMessage(msgServer.accept());
				if (currentMessage != null){
					System.out.println(currentMessage.sourceNick + ": "
							+ currentMessage.content);
				}
			}
			catch (Exception ex)
			{
				System.out.println("Lost a message packet.");
				continue;
			}
		}
	}

	private Message getMessage(Socket serverSocket) throws Exception
	{
		inStream = new ObjectInputStream(serverSocket.getInputStream());
		System.out.println("Got OIS : ChatReceiveThread.");
		Message rcvMessage = (Message) inStream.readObject();
		System.out.println("Got Message Obj | ChatReceiveThread.");
		serverSocket.close();
		System.out.println("Closed server connection | ChatReceiveThread.");
		return rcvMessage;

	}

}
