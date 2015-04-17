package ClientCore;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Object.Message;
import Object.MessageQueue;

public class ChatSendThread extends Thread {
	private Socket clientSocket;
	private ObjectOutputStream outStream;
	private Message currentMessage;
	public MessageQueue messageQueue;

	public ChatSendThread(Socket c) {
		super();
		clientSocket = c;
		messageQueue = new MessageQueue();
	}

	public void run() {

		System.out.println("Sending a socket connection");

		System.out.println("Intializing output Stream");
		try {
			outStream = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Unable to initialize output Stream");
			// notLoggedIn();
			e.printStackTrace();
		}
		/*
		 * For Feedback. One can have a server input coming in here saying true
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
				currentMessage = messageQueue.getMessage();
				try {
					outStream.writeObject(currentMessage);
					outStream.flush();
				} catch (IOException e) {
					System.out.println("Unable to write into output stream.");
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
