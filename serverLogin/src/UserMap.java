import java.net.InetAddress;
import java.util.HashMap;

public class UserMap
{
	HashMap<String, User> userMap;
	HashMap<String, InetAddress> onlineUsers;

	UserMap()
	{
		userMap = new HashMap<String, User>();
		onlineUsers = new HashMap<String, InetAddress>();
	}

	public boolean isNickUsed(String checkNick)
	{
		return userMap.containsKey(checkNick);
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
				onlineUsers.put(nick, ip);
			}
			return true;
		}
	}

	public boolean logIn(String nick, String pass, InetAddress ip)
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
					if (!onlineUsers.containsKey(nick))
						onlineUsers.put(nick, ip);
				}
				return true;
			}
		}
	}
}
