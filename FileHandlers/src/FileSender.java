import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

//import java.util.Scanner;

public class FileSender
{
	private static Socket fileSenderSocket;
	private static OutputStream outStream;
	private static InputStream in;

	public static String filePath = "/home/akshay/Documents/file.txt";

	private final static String serverIP = "10.42.0.27";
	private final static int fileSendPort = 4500;

	public static void main(String[] args)
	{
		/*
		 * sc = new Scanner(System.in);
		 * System.out.println("File Sender Started. Enter full path of file:");
		 * filepath = sc.next();
		 */
		File myFile = new File(filePath);
		try
		{
			fileSenderSocket = new Socket();
			fileSenderSocket.connect(new InetSocketAddress(serverIP,
					fileSendPort));
		} catch (IOException e1)
		{
			System.out.println("FH: Could not connect to server.");
		}
		try
		{
			outStream = new ObjectOutputStream(
					fileSenderSocket.getOutputStream());
		} catch (IOException e1)
		{
			System.out.println("FH: Unable to intialize output stream");
		}

		try
		{
			in = new FileInputStream(filePath);
		} catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		System.out.println("Started sending file");
		try
		{
			IOUtils.copy(in, outStream);
			outStream.flush();
		} catch (IOException e)
		{
			System.out.println("Error while sending packets");
		}
		try
		{
			fileSenderSocket.close();
		} catch (IOException e)
		{
			System.out.println("Unable to close socket");
		}
		System.out.println("File successfully sent");

	}
}
