package coreServer;

import java.util.HashMap;

import common.LoginRequest;

import coreServerThreads.PendingMessageDeliveryThread;

public class UserMap
{
	HashMap<String, User> userMap;
	HashMap<String, String> onlineUsers;

	public UserMap()
	{
		userMap = new HashMap<String, User>();
		onlineUsers = new HashMap<String, String>();
	}

	public boolean isNickUsed(String checkNick)
	{
		synchronized (userMap)
		{
			return userMap.containsKey(checkNick);
		}
	}

	public boolean addUser(LoginRequest req, String ip)
	{
		System.out.println("Adding a new user.");
		if (isNickUsed(req.nick))
			return false;
		else
		{
			synchronized (userMap)
			{
				userMap.put(req.nick, new User(req.nick, req.pass));
			}
			synchronized (onlineUsers)
			{
				onlineUsers.put(req.nick, ip);
			}
			return true;
		}
	}

	public boolean logIn(LoginRequest req, String ip)
	{
		System.out.println("Logging in.");
		synchronized (userMap)
		{
			if (!userMap.containsKey(req.nick))
				return false;
			else
			{
				User user = userMap.get(req.nick);
				if (user.password.hashCode() != req.pass.hashCode())
					return false;

				else
				{
					synchronized (onlineUsers)
					{
						onlineUsers.put(req.nick, ip);
						(new PendingMessageDeliveryThread(user,
								ip)).start();
					}
					return true;
				}
			}
		}
	}

	public boolean logOut(String nick)
	{
		synchronized (onlineUsers)
		{
			if (onlineUsers.containsKey(nick))
			{
				onlineUsers.put(nick, null);
				return true;
			}
		}
		return false;
	}

	public String getCurrentIP(String nick)
	{
		synchronized (onlineUsers)
		{
			return onlineUsers.get(nick);
		}
	}

	public PendingQueue getPendingMessageQueueFor(String nick)
	{
		synchronized (userMap)
		{
			if (userMap.containsKey(nick))
				return userMap.get(nick).pendingMessageQueue;
		}
		return null;
	}
}
