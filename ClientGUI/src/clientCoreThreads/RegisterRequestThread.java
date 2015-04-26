package clientCoreThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GUI.ChatWindow;
import common.LoginRequest;
import coreClient.Global;

public class RegisterRequestThread extends Thread
{
	private Socket clientSocket;
	private final static String destIP = Global.serverIP;
	private final static int destPort = Global.serverLoginRequestPort;
	private LoginRequest loginObj;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Boolean serverInput;
	private JFrame loginWindow;

	public RegisterRequestThread(LoginRequest u, JFrame frame)
	{
		super();
		this.loginObj = u;
		this.loginObj.isSignup = true;
		this.loginWindow = frame;
	}

	public void run()
	{
		System.out.println("Spawned RegisterRequestThread");
		try
		{
			sendLoginObj();
		}
		catch (IOException ex)
		{
			System.out
					.println("Unable to contact server | RegisterRequestThread.");
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
					.println("Unable to contact server | RegisterRequestThread.");
			JOptionPane.showMessageDialog(loginWindow,
					"Server Busy. Please Try Again.");
			return;
		}
		catch (ClassNotFoundException e)
		{
			System.out
					.println("Invalid object received from server. Terminating.");
			JOptionPane.showMessageDialog(loginWindow,
					"Server Busy. Please Try Again.");
			return;
		}
		if (serverInput.booleanValue() == true)
		{
			System.out.println("Sign Up Successful");
			JOptionPane.showMessageDialog(loginWindow, "Register successful.");
			loginWindow.setVisible(false);
			loginWindow.dispose();
			Global.myNick = loginObj.nick;
			Global.window = new ChatWindow();
			Global.window.frmChatServerV.setVisible(true);
			Global.window.frmChatServerV.setResizable(false);
			Global.window.frmChatServerV.setLocationRelativeTo(null);
			return;
		}
		else
		{
			JOptionPane.showMessageDialog(loginWindow,
					"Nick already taken. Please Try Again.");
			System.out.println("Nick already taken. Please Try Again");
		}

	}

	private void sendLoginObj() throws IOException
	{
		clientSocket = new Socket(destIP, destPort);
		System.out.println("Obtained socket | RegisterRequestThread");
		outStream = new ObjectOutputStream(clientSocket.getOutputStream());
		System.out.println("Obtained OOS | RegisterRequestThread");
		outStream.writeObject(loginObj);
		System.out.println("Wrote loginObj | RegisterRequestThread");
		outStream.flush();
		return;
	}

	private void receiveServerResponse() throws IOException,
			ClassNotFoundException
	{
		inStream = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("Obtained OIS | RegisterRequestThread");
		serverInput = (Boolean) inStream.readObject();
		System.out.println("Received serverInput | RegisterRequestThread");
		return;
	}
}
