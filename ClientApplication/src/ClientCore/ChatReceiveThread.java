package ClientCore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Object.Message;

public class ChatReceiveThread extends Thread {
	private Socket clientSocket;
	private ObjectInputStream inStream;
	private Message currentMessage;
	
	public ChatReceiveThread(Socket c) {
		clientSocket = c;
	}

	public void run() {
		
		
		try {
			inStream = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			System.out.println("Failed to obtain object output stream from client socket.");
		}
		while(currentMessage == null){
			try {
				currentMessage = (Message) inStream.readObject();
			} catch (ClassNotFoundException | IOException e){
				System.out.println("Unable to read input as Message object");
			}
			System.out.println(currentMessage.sourceNick + ": " + currentMessage.content);
			currentMessage = null;
		}

	}

}
