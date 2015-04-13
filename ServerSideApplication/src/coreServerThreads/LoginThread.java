package coreServerThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.User;
import coreServer.UserMap;

public class LoginThread extends Thread
{
	User user;
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
		try
		{
			user = (User) ois.readObject();
		}
		catch (ClassNotFoundException | IOException e1)
		{
			System.out.println("Class not found || Time out.");
			return;
		}

		Boolean sendObj = false;
		if (user.isNewUser)
		{
			sendObj = userMap.addNewUser(user.nick, user.pass,
					remoteSocket.getInetAddress());
		}
		else if (userMap.logIn(user.nick, user.pass,
				remoteSocket.getInetAddress()))
			sendObj = true;

		try
		{
			oos = new ObjectOutputStream(remoteSocket.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println("Lost connection to user.");
			return;
		}
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
		try
		{
			remoteSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Unable to close socket. Terminating thread.");
			return;
		}
	}
}
