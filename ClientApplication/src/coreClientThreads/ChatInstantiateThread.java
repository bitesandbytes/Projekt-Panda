package coreClientThreads;

import clientMain.Client;

public class ChatInstantiateThread extends Thread
{
	public void run()
	{
		System.out.println("Welcome to the Chat Server :");
		System.out.println("List of Friends: ");
		for (int i = 0; i < Client.friends.size(); i++)
			System.out.println((i + 1) + ". " + Client.friends.get(i));
		(new ChatSendThread()).start();
		(new ChatReceiveThread()).start();
		//(new FileReceiveControlThread()).start();
		(new NewFileReceiveThread()).start();
	}
}
