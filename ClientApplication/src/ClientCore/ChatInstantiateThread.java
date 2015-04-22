package ClientCore;

public class ChatInstantiateThread extends Thread {
	public ChatSendThread messageSender;
	public ChatReceiveThread messageReceiver;
	
	
	public ChatInstantiateThread() {
		super();
	}

	public void run() {
		System.out.println("Welcome to the Chatting Browser :");
		System.out.println("List of Friends: ");
		for (int i = 0; i < Client.friends.size(); i++)
			System.out.println(i + ". " + Client.friends.get(i));
		System.out.println("Control: Starting Sender Thread");
		messageSender = new ChatSendThread();
		messageSender.start();
		System.out.println("Control: Starting Receiver Thread");
		messageReceiver = new ChatReceiveThread();
		messageReceiver.start();

	}
}
