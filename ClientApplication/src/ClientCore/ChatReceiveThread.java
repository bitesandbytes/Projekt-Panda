package ClientCore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import common.Message;

public class ChatReceiveThread extends Thread {
	private Socket messageReceiverSocket;
	private ObjectInputStream inStream;
	private Message currentMessage;

	private final static int messageReceivePort = 2500;

	public ChatReceiveThread() {
		super();
	}

	public void run() {
		System.out.println("Started Chat Receiver Thread");
		while (currentMessage == null) {
			//System.out.println("CRT: Waiting for server to accept my Connection.");
			try {
				messageReceiverSocket = new Socket();
				messageReceiverSocket = (new ServerSocket(messageReceivePort)).accept();
			} catch (IOException e1) {
				//System.out.println("CRT: Could not connect to server.");
				try {
					messageReceiverSocket.close();
				} catch (IOException e) {
					//System.out.println("CRT: Unable to close socket");
					continue;
				}
			}
			//System.out.println("CRT: Connection accepted from Server to receive incoming message");
			try {
				inStream = new ObjectInputStream(messageReceiverSocket.getInputStream());
			} catch (IOException e) {
				//System.out.println("CRT: Failed to obtain object input stream from client socket.");
			}
			//System.out.println("CRT: Attempting to read Message Object");
			try {
				currentMessage = (Message) inStream.readObject();
			} catch (ClassNotFoundException | IOException e) {
				//System.out.println("CRT: Unable to read input as Message object");
			}
			if(currentMessage != null)
				System.out.println(currentMessage.sourceNick + ": " + currentMessage.content);
			try {
				messageReceiverSocket.close();
			} catch (IOException e) {
				//System.out.println("CRT: Unable to close socket");
			}
			currentMessage = null;
		}

	}

}
