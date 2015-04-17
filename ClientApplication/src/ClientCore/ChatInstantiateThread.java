package ClientCore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatInstantiateThread extends Thread {
	public Socket messageReceiverSocket;
	public Socket messageSenderSocket;
	public ChatSendThread messageSender;
	public ChatReceiveThread messageReceiver;
	
	private final static int messageReceivePort = 2500;
	private final static String serverIP = "10.42.0.27";
	private final static int messageSendPort = 2400;

	public ChatInstantiateThread() {
		super();
	}

	public void run() {
		try {
			messageReceiverSocket = (new ServerSocket(messageReceivePort)).accept();
		} catch (IOException e1) {
			System.out.println("Could not connect to server.");
		}
		try {
			messageSenderSocket.connect(new InetSocketAddress(serverIP, messageSendPort));
		} catch (IOException e1) {
			System.out.println("Could not connect to server.");
		}
		System.out.println("Welcome to the Chatting Browser :");
		System.out.println("List of Friends: ");
		for (int i = 0; i < Client.friends.size(); i++)
			System.out.println(i + ". " + Client.friends.get(i));
		messageSender = new ChatSendThread(messageSenderSocket);
		messageSender.start();
		messageReceiver = new ChatReceiveThread(messageReceiverSocket);
		messageReceiver.start();

	}
}
