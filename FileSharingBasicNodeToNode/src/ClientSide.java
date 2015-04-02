import java.net.*;
import java.io.*;
public class ClientSide
{
	public static void main(String[] args) throws IOException
	{
		int filesize = 952;
		int bytesRead;
		int currentTot = 0;
		Socket socket = new Socket("127.0.0.1", 13500);
		System.out.println("Connected to server.");
		byte[] bytearray = new byte[filesize];
		InputStream is = socket.getInputStream();
		FileOutputStream fos = new FileOutputStream("B.P.Lathi.pdf");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bytesRead = is.read(bytearray, 0, bytearray.length);
		currentTot = bytesRead;
		System.out.println("Receving from server.");
		do
		{
			bytesRead = is.read(bytearray, currentTot,
					(bytearray.length - currentTot));
			if (bytesRead >= 0)
				currentTot += bytesRead;
		} while (bytesRead > -1);
		System.out.println("Completed transfer at Client side.");
		bos.write(bytearray, 0, currentTot);
		bos.flush();
		bos.close();
		socket.close();
		
	}
}