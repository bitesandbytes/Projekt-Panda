import java.io.IOException;
import java.net.ServerSocket;

public class ServerLoginListener
{
	private static final int loginListenPort = 2400;
	private static UserMap userMap = null;
	private static ServerSocket serverSocket;

	public static void main(String[] args) throws IOException
	{
		serverSocket = new ServerSocket(loginListenPort);
		userMap = new UserMap();
		while (true)
		{
			System.out.println("Waiting for new connection.");
			synchronized (userMap)
			{
				new LoginProcessThread(userMap, serverSocket.accept()).start();
			}
		}
	}
}
