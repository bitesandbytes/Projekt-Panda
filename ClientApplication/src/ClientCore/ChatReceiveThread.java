package ClientCore;

import helpers.Global;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import common.Message;

public class ChatReceiveThread extends Thread
{
	private ServerSocket msgServer;
	private ObjectInputStream inStream;
	private Message currentMessage;
	private final static int msgRcvPort = Global.localMsgReceivePort;

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
				if (currentMessage != null)
					System.out.println(currentMessage.sourceNick + ": "
							+ currentMessage.content);
			}
			catch (Exception ex)
			{
				System.out.println("Lost a message packet.");
				continue;
			}
		}
		/*
		 * while (currentMessage == null) { //
		 * System.out.println("CRT: Waiting for server to accept my Connection."
		 * ); try { messageReceiverSocket = new Socket(); messageReceiverSocket
		 * = (new ServerSocket(msgRcvPort)).accept(); } catch (IOException e1) {
		 * // System.out.println("CRT: Could not connect to server."); try {
		 * messageReceiverSocket.close(); } catch (IOException e) { //
		 * System.out.println("CRT: Unable to close socket"); continue; } } //
		 * System.out.println(
		 * "CRT: Connection accepted from Server to receive incoming message");
		 * try { inStream = new ObjectInputStream(
		 * messageReceiverSocket.getInputStream()); } catch (IOException e) { //
		 * System.out.println(
		 * "CRT: Failed to obtain object input stream from client socket."); }
		 * // System.out.println("CRT: Attempting to read Message Object"); try
		 * { currentMessage = (Message) inStream.readObject(); } catch
		 * (ClassNotFoundException | IOException e) { //
		 * System.out.println("CRT: Unable to read input as Message object"); }
		 * if (currentMessage != null)
		 * System.out.println(currentMessage.sourceNick + ": " +
		 * currentMessage.content); try { messageReceiverSocket.close(); } catch
		 * (IOException e) { //
		 * System.out.println("CRT: Unable to close socket"); } currentMessage =
		 * null; }
		 */
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
