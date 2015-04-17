package ClientCore;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import common.Message;
import common.MessageQueue;

public class ChatSendThread extends Thread {
	private Socket messageSenderSocket;
	private ObjectOutputStream outStream;
	private Message currentMessage;
	public MessageQueue messageQueue;
	
	private final static String serverIP = "192.168.1.221";
	private final static int messageSendPort = 2400;


	public ChatSendThread() {
		super();
		messageQueue = new MessageQueue();
	}

	public void run() {
		System.out.println("Started Chat Sender Thread");
		

		/* For Feedback. One can have a server input coming in here saying true
		 * of false.Gives indication whether server was able to request a
		 * connection and currentFriend accepted it.
		 */
		(new WriteMessageThread(messageQueue)).start();
		while(true){
			currentMessage = null;
			synchronized (messageQueue) {
				if(messageQueue.size() == 0)
					try {
						messageQueue.wait();
					} catch (InterruptedException e) {
						break;
					}
				System.out.println("CST: Sending a socket connection");
				try {
					messageSenderSocket = new Socket();
					messageSenderSocket.connect(new InetSocketAddress(serverIP, messageSendPort));
				} catch (IOException e1) {
					System.out.println("CST: Could not connect to server.");
				}
				System.out.println("CST: Intializing output Stream");
				try {
					outStream = new ObjectOutputStream(messageSenderSocket.getOutputStream());
				} catch (IOException e) {
					System.out.println("CST: Unable to initialize output Stream");
				}
				currentMessage = messageQueue.getMessage();
				try {
					outStream.writeObject(currentMessage);
					outStream.flush();
				} catch (IOException e) {
					System.out.println("CST: Unable to write into output stream.");
				}
			}
		}
		
		/*
		 * System.out.println("Press 0 to establish connection with a friend");
		 * System.out.println("Press 1 to establish a new connection");
		 * newConnection = sc.nextInt();
		 */

	}

}
