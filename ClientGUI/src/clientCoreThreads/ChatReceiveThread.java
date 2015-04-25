package clientCoreThreads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
	PrintWriter writer;
	File curFriendFile;

	public ChatReceiveThread(JTextArea chatBox)
	{
		super();
		this.chatBox = chatBox;

	}

	public void run()
	{
		Global.Log("Spawned ChatReceiveThread");
		try
		{
			msgServer = new ServerSocket(msgRcvPort);
		} catch (IOException e)
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
				if (currentMessage != null)
				{
					// Global.Log(currentMessage.sourceNick + ": "+
					// currentMessage.content);
					try
					{
						curFriendFile = new File(Global.userContainerPath
								+ currentMessage.sourceNick);
						Global.Log("Opened File: " + Global.userContainerPath
								+ currentMessage.sourceNick);
						FileWriter fw = new FileWriter(curFriendFile, true);
						BufferedWriter bw = new BufferedWriter(fw);
						Global.Log("Begin To Write");
						bw.write("\n" + currentMessage.content);
						bw.close();
						Global.Log("Wrote: " + currentMessage.content);
					} catch (IOException e1)
					{
						Global.Log("Unable to write into File");
					}
					if (Global.window.curFriend.getText().equals(currentMessage.sourceNick))
					{
						/*
						 * writer = new PrintWriter(Global.userContainerPath +
						 * currentMessage.sourceNick, "UTF-8");
						 * writer.print(currentMessage.content); writer.close();
						 */
						Global.Log("Appending");
						chatBox.append("\n"+currentMessage.content);
					}
				}
			} catch (Exception ex)
			{
				ex.printStackTrace();
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
		Global.Log("Receiver : " + rcvMessage.sourceNick + "->"
				+ rcvMessage.destNick + ": " + rcvMessage.content);
		return rcvMessage;

	}

}
