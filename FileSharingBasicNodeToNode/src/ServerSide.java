import java.io.*;
import java.net.*;
/*
 * Simple file sharing java application
 * User receives file from the server.
 * Server sends the file to the user.
 */

//Runs on the server.
public class ServerSide
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = new ServerSocket(13500);
		Socket socket = serverSocket.accept();
		System.out.println("Accepting connection from : "+socket);
		File transferFile = new File("testFile.pdf");
		byte[] bytearray = new byte[(int)transferFile.length()];
		FileInputStream fin = new FileInputStream(transferFile);
		BufferedInputStream bin = new BufferedInputStream(fin);
		bin.read(bytearray, 0, bytearray.length);
		OutputStream os = socket.getOutputStream();
		System.out.println("Sending file... ");
		os.flush();
		socket.close();
		System.out.println("Completed transfer from server side.");
	}
}
