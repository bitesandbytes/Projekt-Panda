import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendString extends Thread {
	private static String destIP = "192.168.1.221";
	private Socket sendStringSocket;
	private ObjectOutputStream oos;
	private static int destPort = 3000;

	SendString() {
		super();
	}

	public void run() {
		int counter = 0;
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sendStringSocket = new Socket(destIP, destPort);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				oos = new ObjectOutputStream(sendStringSocket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				counter++;
				oos.writeObject("This is a string." + counter);
				System.out.println("Sent a string.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
}
