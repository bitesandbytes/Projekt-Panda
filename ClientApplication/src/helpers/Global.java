package helpers;

public class Global {

	public static int destPort = 2004;
	
	
	public static void Log (Object o)
	{
		synchronized (System.out)
		{
			System.out.println (o);
		}
	}
	
}
