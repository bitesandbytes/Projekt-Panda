package coreServerThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.LoginRequest;
import coreServer.UserMap;

public class LoginThread extends Thread
{
	LoginRequest loginReq;
	UserMap userMap;
	Socket remoteSocket;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	LoginThread(UserMap map, Socket remoteSocket)
	{
		System.out.println("Login request from "
				+ remoteSocket.getInetAddress());
		this.userMap = map;
		this.remoteSocket = remoteSocket;
	}

	public void run()
	{
		try
		{
			ois = new ObjectInputStream(remoteSocket.getInputStream());
		}
		catch (IOException e1)
		{
			System.out.println("Unable to get input stream.");
			return;
		}
		System.out.println("Got OIS. Login Thread.");
		try
		{
			loginReq = (LoginRequest) ois.readObject();
		}
		catch (ClassNotFoundException | IOException e1)
		{
			System.out.println("Class not found || Time out.");
			return;
		}
		System.out.println("Got obj.");
		Boolean sendObj = false;
		System.out.println("Credentials: "+loginReq.nick+", "+loginReq.pass+" : "+loginReq.isSignup);
		if (loginReq.isSignup)
		{
			sendObj = userMap.addUser(loginReq, remoteSocket.getInetAddress()
					.getHostAddress());
		}
		else if (userMap.logIn(loginReq, remoteSocket.getInetAddress()
				.getHostAddress()))
			sendObj = true;
		else
			sendObj = false;
		
		System.out.println("Got Output obj.");
		try
		{
			oos = new ObjectOutputStream(remoteSocket.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println("Lost connection to user.");
			return;
		}
		System.out.println("Got OOS.");
		try
		{
			System.out.println("Writing obj to remote socket.");
			oos.writeObject(sendObj);
		}
		catch (IOException e)
		{
			System.out
					.println("Unable to write to user. Possibly lost connection");
			return;
		}
		System.out.println("Sent outputObj.");
		try
		{
			remoteSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Unable to close socket. Terminating thread.");
			return;
		}
		System.out.println("Closed socket.");
		return;
	}
}
