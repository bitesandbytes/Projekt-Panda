package clientCoreThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import common.Message;
import coreClient.Global;

public class ChatReceiveThread extends Thread
{
	private ServerSocket msgServer;
	private ObjectInputStream inStream;
	private Message currentMessage;
	private final static int msgRcvPort = Global.localMsgReceivePort;
	public JTextArea chatBox;
	public JLabel curFriendLabel = Global.currentFriendLabel;
	PrintWriter writer;
	
	public ChatReceiveThread(JTextArea chatBox){
		super();
		this.chatBox = chatBox;
		
	}

	public void run()
	{
		Global.Log("Spawned ChatReceiveThread");
		try
		{
			msgServer = new ServerSocket(msgRcvPort);
		}
		catch (IOException e)
		{
			Global.Log("FATAL ERROR : Unable to bind to local port @ "
					+ msgRcvPort);
			Global.Log("Terminating application.");
			System.exit(0);
		}
		while (true)
		{
			try
			{
				currentMessage = getMessage(msgServer.accept());
				if (currentMessage != null){
					//Global.Log(currentMessage.sourceNick + ": "+ currentMessage.content);
					if(curFriendLabel.getText().equals(currentMessage.sourceNick)){
						writer = new PrintWriter(Global.userContainerPath+currentMessage.sourceNick, "UTF-8");
						writer.print(currentMessage.content);
						writer.close();
						synchronized (chatBox)
						{
							chatBox.append(currentMessage.content);
						}
					}
				}
			}
			catch (Exception ex)
			{
				Global.Log("Lost a message packet.");
				continue;
			}
		}
	}

	private Message getMessage(Socket serverSocket) throws Exception
	{
		inStream = new ObjectInputStream(serverSocket.getInputStream());
		Global.Log("Got OIS : ChatReceiveThread.");
		Message rcvMessage = (Message) inStream.readObject();
		Global.Log("Got Message Obj | ChatReceiveThread.");
		serverSocket.close();
		Global.Log("Closed server connection | ChatReceiveThread.");
		return rcvMessage;

	}

}
