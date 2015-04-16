package coreServer;

import java.net.InetAddress;
import java.util.HashMap;

import common.User;

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

	public boolean addNewUser(String nick, String pass, InetAddress ip)
	{
		if (isNickUsed(nick))
			return false;
		else
		{
			synchronized (userMap)
			{
				userMap.put(nick, new User(nick, pass));
			}
			synchronized (onlineUsers)
			{
				onlineUsers.put(nick, ip.getHostAddress());
			}
			return true;
		}
	}

	public boolean logIn(String nick, String pass, InetAddress ip)
	{
		synchronized (userMap)
		{
			if (!userMap.containsKey(nick))
				return false;
			else
			{
				User newUser = new User(nick, pass);
				if (newUser.compareTo(userMap.get(nick)) != 0)
					return false;

				else
				{
					synchronized (onlineUsers)
					{
						onlineUsers.put(nick, ip.getHostAddress());
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
}
