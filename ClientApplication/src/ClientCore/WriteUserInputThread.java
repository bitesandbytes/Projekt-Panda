package ClientCore;

import helpers.MessageQueue;

import java.util.Scanner;

import common.Message;

public class WriteUserInputThread extends Thread {
	private Scanner sc;
	private String currentAction;
	private String currentText;
	private String currentFriend;
	private Message currentMessage;
	public MessageQueue messageQueue;
	public WriteUserInputThread(MessageQueue mq) {
		super();
		messageQueue = mq;

	}
	public void run() {

		currentMessage = new Message(Client.user.nick, "", "");
		while (true) {
			currentMessage.destNick = "";
			currentMessage.content = "";
			currentAction = "";
			currentFriend = "";
			currentText = "";
			sc = new Scanner(System.in);
			System.out
					.println("WMT: Established Sending connection. Type 'message' or 'file' or 'exit'");
			currentAction = sc.next();
			if (currentAction.equals("exit"))
				break;
			
			
			sc = new Scanner(System.in);
			System.out.println("Enter Friend's Nick:");
			currentFriend = sc.next();
			
			
			sc = new Scanner(System.in);
			if (currentAction.equals("message"))
				System.out.println("Enter message:");
			else
				System.out.println("Enter Valid File Path:");
			currentText = sc.nextLine();
			if (currentAction.equals("message")) {
				currentMessage.destNick = currentFriend;
				currentMessage.content = currentText;
				System.out.println("currentMessage.destNick :" + currentMessage.destNick);
				System.out.println("currentMessage.content :" + currentMessage.content);
				synchronized (messageQueue) {
					messageQueue.addMessage(currentMessage);
				}
			}
			else{
				(new FileSendControlThread(currentFriend, currentText)).start();
			}
		}
		System.out
				.println("WMT: Messages sent. Thank You. Enjoy your worthless life");

	}
}
