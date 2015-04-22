package ClientCore;

import helpers.MessageQueue;

import java.util.Scanner;

import common.Message;

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
		while (true) {
			sc = new Scanner(System.in);
			System.out.println("WMT: Established Sending connection. Type friend and message. enter message exit to exit");
			currentFriend = sc.next();
			sc = new Scanner(System.in);
			System.out.println("Enter message");
			currentText = sc.nextLine();
			if (currentText == "exit")
				break;
			currentMessage.destNick = currentFriend;
			currentMessage.content = currentText;
			synchronized (messageQueue) {
				messageQueue.addMessage(currentMessage);
			}
		}
		System.out.println("WMT: Messages sent. Thank You. Enjoy your worthless life");

		
	}
}
