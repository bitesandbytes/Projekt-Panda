package ClientCore;

import helpers.Global;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.LoginRequest;

public class LoginRequestThread extends Thread
{
	private Socket clientSocket;
	private final static String destIP = Global.serverIP;
	private final static int destPort = Global.serverLoginRequestPort;
	private LoginRequest loginObj;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Boolean serverInput;

	LoginRequestThread(LoginRequest u)
	{
		super();
		this.loginObj = u;
	}

	public void run()
	{
		System.out.println("Spawned LoginRequestThread");
		/*
		 * System.out.println("Sending a socket connection"); clientSocket = new
		 * Socket(); try { clientSocket.connect(new InetSocketAddress(destIP,
		 * destPort)); } catch (IOException e) {
		 * System.out.println("Unable to establish connection with server"); }
		 * System.out.println("Intializing output Stream"); try { outStream =
		 * new ObjectOutputStream(clientSocket.getOutputStream()); } catch
		 * (IOException e) {
		 * System.out.println("Unable to initialize output Stream"); }
		 * 
		 * System.out.println("Writing Object to Server"); try {
		 * outStream.writeObject(loginObj); outStream.flush(); } catch
		 * (IOException e) {
		 * System.out.println("Unable to write to  Output Stream"); }
		 */

		int retryCount = 3;
		while (retryCount > 0)
		{
			try
			{
				sendLoginObj();
				break;
			}
			catch (IOException ex)
			{
				retryCount--;
				System.out.println("Retrying login ...");
				if (retryCount == 0)
				{
					System.out
							.println("Unable to contact server | LoginRequestThread.");
					return;
				}
			}
		}
		retryCount = 3;
		while (retryCount > 0)
		{
			try
			{
				receiveServerResponse();
				break;
			}
			catch (IOException ex)
			{
				retryCount--;
				System.out.println("Retrying login ...");
				if (retryCount == 0)
				{
					System.out
							.println("Unable to contact server | LoginRequestThread.");
					return;
				}
			}
			catch (ClassNotFoundException e)
			{
				System.out
						.println("Invalid object received from server. Terminating login.");
				return;
			}
		}
		/*
		 * 
		 * System.out.println("Object sent. Intializing input Stream"); try {
		 * inStream = new ObjectInputStream(clientSocket.getInputStream()); }
		 * catch (IOException e) {
		 * System.out.println("Unable to initialize input Stream"); }
		 * System.out.println("Begin to listen."); try { serverInput = (boolean)
		 * inStream.readObject(); } catch (ClassNotFoundException | IOException
		 * e) { System.out.println("Unable to read from Server"); }
		 * System.out.println("Received something from server");
		 */

		if (serverInput.booleanValue() == true && Client.user.isSignup == true)
		{
			System.out.println("Sign Up Successful");
			(new ChatInstantiateThread()).start();
		}
		else if (serverInput.booleanValue() == true
				&& Client.user.isSignup == false)
			(new ChatInstantiateThread()).start();
		else if (serverInput.booleanValue() == false
				&& Client.user.isSignup == true)
			System.out.println("Sign Up failed. Nick Already Taken");
		else
			System.out.println("Login failed. Please Try Again");

	}

	private void sendLoginObj() throws IOException
	{
		clientSocket = new Socket(destIP, destPort);
		System.out.println("Obtained socket | LoginRequestThread");
		outStream = new ObjectOutputStream(clientSocket.getOutputStream());
		System.out.println("Obtained OOS | LoginRequestThread");
		outStream.writeObject(loginObj);
		System.out.println("Wrote loginObj | LoginRequestThread");
		outStream.flush();
		return;
	}

	private void receiveServerResponse() throws IOException,
			ClassNotFoundException
	{
		inStream = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("Obtained OIS | LoginRequestThread");
		serverInput = (Boolean) inStream.readObject();
		System.out.println("Received serverInput | LoginRequestThread");
		return;
	}
}
