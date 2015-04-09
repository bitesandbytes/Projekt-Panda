import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendMessage extends Thread {
	private static String destIP = "192.168.1.221";
	private Socket sendMessageSocket;
	private ObjectOutputStream senderOutputStream;
	private static int destPort = 2500;
	private static Message msg = new Message("This is a message");

	public SendMessage() {
		super();
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sendMessageSocket = new Socket(destIP, destPort);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				senderOutputStream = new ObjectOutputStream(
						sendMessageSocket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				senderOutputStream.writeObject(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Unable to send object");
				e.printStackTrace();
			}
			System.out.println("Successfully sent Message");
		}
	}

}
