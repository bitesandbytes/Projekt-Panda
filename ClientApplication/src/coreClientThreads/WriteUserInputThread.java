package coreClientThreads;

import java.util.Scanner;

import clientMain.Client;
import common.Message;
import coreClient.MessageQueue;

public class WriteUserInputThread extends Thread
{
	private Scanner sc;
	private String curAction;
	private String curText;
	private String curFriend;
	private Message curMessage;
	public MessageQueue messageQueue;

	public WriteUserInputThread(MessageQueue mq)
	{
		super();
		messageQueue = mq;

	}

	public void run()
	{

		curMessage = new Message(Client.user.nick, "", "");
		sc = new Scanner(System.in);
		while (true)
		{
			System.out
					.println("WMT: Established Sending connection. Type 'm' or 'f' or 'exit'");

			sc = new Scanner(System.in);
			curAction = sc.next();

			if (curAction.equals("exit"))
				System.exit(0);

			System.out.println("Enter Friend's Nick:");
			sc = new Scanner(System.in);
			curFriend = sc.next();

			if (curAction.equals("m"))
				System.out.println("Enter message:");
			else
				System.out.println("Enter Valid File Path:");
			
			sc = new Scanner(System.in);
			curText = sc.nextLine();

			if (curAction.equals("m"))
			{
				curMessage.destNick = curFriend;
				curMessage.content = curText;
				synchronized (messageQueue)
				{
					messageQueue.addMessage(curMessage);
					messageQueue.notify();
				}
			}
			else if (curAction.equals("f"))
				(new FileSendControlThread(curFriend, curText)).start();
			else
			{
				System.out.println("Invalid input. ");
				continue;
			}
		}
	}
}
