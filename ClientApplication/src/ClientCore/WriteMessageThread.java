package ClientCore;

import java.util.Scanner;

import Object.Message;
import Object.MessageQueue;

public class WriteMessageThread extends Thread{
	private Scanner sc;
	private String currentText;
	private String currentFriend;
	private Message currentMessage;
	public MessageQueue messageQueue; 
	public WriteMessageThread(MessageQueue mq){
		super();
		messageQueue = mq;
		
	}
	public void run(){
		
		currentMessage = new Message(Client.user.nick, "", "");
		System.out
				.println("Established Sending connection. Type friend and message. enter message exit to exit");
		while (true) {
			currentFriend = sc.next();
			currentText = sc.next();
			if (currentText == "exit")
				break;
			currentMessage.destNick = currentFriend;
			currentMessage.content = currentText;
			synchronized (messageQueue) {
				messageQueue.addMessage(currentMessage);
			}
		}
		System.out.println("Messages sent. Thank You. Enjoy your worthless life");

		
	}
}
