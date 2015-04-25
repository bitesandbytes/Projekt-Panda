package coreClient;

public class Global
{
	public static String serverIP = "localhost";
	public static String defaultFileSavePath = "/home/sauce/Documents/";
	public static int serverFileRequestPort = 4400;
	public static int serverMsgPort = 2400;
	public static int serverLoginRequestPort = 3400;
	public static int localMsgReceivePort = 2500;
	public static int clientFilePort = 4500;
	public static int pingTimeout = 1000;

	public static void Log(Object o)
	{
		synchronized (System.out)
		{
			System.out.println(o);
		}
	}
}
