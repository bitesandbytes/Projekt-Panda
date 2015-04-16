package ClientCore;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import Object.User;


public class LoginRequestThread extends Thread {
	private Socket clientSocket;
	private final static String destIP = "10.42.0.27";
	private final static int destPort = 2400;
	private final static String hostIP = "10.42.0.27";
	private final static int hostPort = 2500;
	User user;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Boolean serverInput;

	LoginRequestThread(User u) {
		super();
		this.user = u; 
	}

	public void run() {
			System.out.println("Sending a socket connection");
			try {
				clientSocket.bind(new InetSocketAddress(hostIP, hostPort));
			} catch (IOException e1) {
				System.out.println("Unable to bind the socket.");
			}
			try {
				clientSocket.connect(new InetSocketAddress(destIP, destPort));
			} catch (IOException e) {
				System.out.println("Unable to establish connection with server");
			}
			System.out.println("Intializing output Stream");
			try {
				outStream = new ObjectOutputStream(
						clientSocket.getOutputStream());
			} catch (IOException e) {
				System.out.println("Unable to initialize output Stream");
				// notLoggedIn();
				e.printStackTrace();
			}
			
			System.out.println("Writing Object to Server");
			try {
				outStream.writeObject(user);
				outStream.flush();
			} catch (IOException e) {
				System.out.println("Unable to write to  Output Stream");
				e.printStackTrace();
			}
			System.out.println("Object sent. Intializing input Stream");
			try {
				inStream = new ObjectInputStream(clientSocket.getInputStream());
			} catch (IOException e) {
				System.out.println("Unable to initialize input Stream");
				e.printStackTrace();
			}
			System.out.println("Begin to listen.");
			try {
				serverInput = (Boolean) inStream.readObject();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Unable to read from Server");
				// notLoggedIn();
				e.printStackTrace();
			}
			System.out.println("Received something from server");
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (serverInput.booleanValue() == true && Client.user.isNewUser == true) {
				System.out.println("Sign Up Successful");
				(new ChatInstantiateThread()).start();
			}
			else if(serverInput.booleanValue() == true && Client.user.isNewUser == false)
				(new ChatInstantiateThread()).start();
			else if(serverInput.booleanValue() == false && Client.user.isNewUser == true)
				System.out.println("Sign Up Unsuccessful. Nick Already Taken");
			else
				System.out.println("Login Unsuccessful. Please Try Again");
		
	}
}
