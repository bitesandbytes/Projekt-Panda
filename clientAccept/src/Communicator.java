import java.io.IOException;
import java.net.ServerSocket;

public class Communicator
{
	public static int portNumberMessage = 2500;
	public static int portNumberString = 3000;

	public static void main(String[] args) throws IOException,
			ClassNotFoundException
	{
		ServerSocket serverMessageSocket = new ServerSocket(portNumberMessage);
		ServerSocket serverStringSocket = new ServerSocket(portNumberString);
		ListenMessage messageListener = new ListenMessage(serverMessageSocket);
		ListenString stringListener = new ListenString(serverStringSocket);
		messageListener.start();
		stringListener.start();
	}
}
