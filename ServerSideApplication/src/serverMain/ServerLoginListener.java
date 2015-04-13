package serverMain;

import coreServer.UserMap;
import coreServerThreads.ControlThread;

public class ServerLoginListener
{
	public static UserMap userMap = new UserMap();
	public static void main(String[] args)
	{
		ControlThread controlThread = new ControlThread(userMap);
		controlThread.start();
	}
}
