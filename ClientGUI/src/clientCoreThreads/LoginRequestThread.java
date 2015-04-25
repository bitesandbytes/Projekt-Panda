package clientCoreThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import common.LoginRequest;

import coreClient.Global;

public class LoginRequestThread extends Thread
{
	private Socket clientSocket;
	private final static String destIP = Global.serverIP;
	private final static int destPort = Global.serverLoginRequestPort;
	private LoginRequest loginObj;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Boolean serverInput;
	private JFrame loginWindow;

	public LoginRequestThread(LoginRequest u, JFrame frame)
	{
		super();
		this.loginObj = u;
		this.loginWindow = frame;
	}

	public void run()
	{
		System.out.println("Spawned LoginRequestThread");
		try
		{
			sendLoginObj();
		}
		catch (IOException ex)
		{
			System.out
					.println("Unable to contact server | LoginRequestThread.");
			JOptionPane.showMessageDialog(loginWindow,
					"Server Busy. Please Try Again.");
			return;
		}
		try
		{
			receiveServerResponse();
		}
		catch (IOException ex)
		{
			System.out
					.println("Unable to contact server | LoginRequestThread.");
			JOptionPane.showMessageDialog(loginWindow,
					"Server Busy. Please Try Again.");
			return;
		}
		catch (ClassNotFoundException e)
		{
			System.out
					.println("Invalid object received from server. Terminating login.");
			JOptionPane.showMessageDialog(loginWindow,
					"Server Busy. Please Try Again.");
			return;
		}
		if (serverInput.booleanValue() == true)
		{
			System.out.println("Login Successful");
			loginWindow.setVisible(false);
			loginWindow.dispose();
			//TODO:: start chat GUI thread.
			return;
		}
		else
		{
			JOptionPane.showMessageDialog(loginWindow,
					"Login failed. Please Try Again.");
			System.out.println("Login failed. Please Try Again");
		}
		return;
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
