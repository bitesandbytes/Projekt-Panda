import java.io.*;
import java.net.UnknownHostException;
public class Sender {
	public static String hostName = "192.168.1.221";
	public static int stringTargetPort = 3000;
	public static int messageTargetPort = 2500;
	public static void main(String [] args) throws UnknownHostException, IOException{
		/*Socket messageSenderSocket = new Socket(hostName, messageTargetPort);
		Socket stringSenderSocket = new Socket(hostName, stringTargetPort);
		ObjectOutputStream messageOutputStream = new ObjectOutputStream(messageSenderSocket.getOutputStream());
		ObjectOutputStream stringOutputStream = new ObjectOutputStream(stringSenderSocket.getOutputStream());
		
		Message sendMessage = new Message("This is a Message");
		String sendString = "This is a String";
		messageOutputStream.writeObject(sendMessage);
		System.out.println("Successfully sent Message");
		stringOutputStream.writeObject(sendString);
		System.out.println("Successfully sent String");*/
		
		SendMessage messageThread = new SendMessage();
		messageThread.start();
		SendString stringThread = new SendString();
		stringThread.start();
		
		
	}

}
