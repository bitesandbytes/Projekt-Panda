package coreClientThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.FileControlPacket;
import coreClient.Global;

public class FileSendControlThread extends Thread
{
	private Socket fileSendControlSocket;
	private final static String serverIP = Global.serverIP;
	private final static int fileControlPort = Global.serverFileRequestPort;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private String destNick;
	private FileControlPacket fileControlPack;
	private String filePath;
	private String fileName;

	public FileSendControlThread(String dn, String fp)
	{
		super();
		destNick = dn;
		filePath = fp;
	}

	public void run()
	{
		System.out.println("File Sent Request Found.");
		String[] temp;
		temp = filePath.split("/");
		fileName = temp[temp.length - 1];
		fileControlPack = new FileControlPacket(false, destNick, false);
		fileControlPack.fileName = fileName;
		int retryCount = 3;
		System.out.println("FSCT: Pinging server for Client IP.");
		while (retryCount > 0)
		{
			try
			{
				sendFileControlPacket();
				break;
			}
			catch (IOException ex)
			{
				System.out
						.println("Retrying fileControlPacket send | FileSendControlThread.");
				retryCount--;
				continue;
			}
		}
		System.out.println("FSCT: Successfully Pinged Server. Waiting For Server to Send Back Control Packet");
		retryCount = 3;
		while (retryCount > 0)
		{
			try
			{
				receiveFileControlPacket();
				break;
			}
			catch (IOException ex)
			{
				System.out
						.println("Retrying fileControlPacket receive | FileSendControlThread.");
				retryCount--;
				continue;
			}
			catch (ClassNotFoundException e)
			{
				System.out
						.println("Invalid packet received | FileSendControlThread.");
				return;
			}
		}
		
		if (fileControlPack.isIP == true && fileControlPack.payload.equals("0.0.0.0"))
		{
			System.out.println("FSCT: User is offline. Try Again Later");
		}
		else
		{	
			System.out.println("FSCT: Setting up connection directly to client");
			(new FileSenderThread(filePath,fileName, fileControlPack.payload)).start();

		}
	}

	private void receiveFileControlPacket() throws IOException,
			ClassNotFoundException
	{
		System.out.println("FSCT: Initializing Input Stream");
		inStream = new ObjectInputStream(fileSendControlSocket.getInputStream());
		System.out.println("FSCT: Reading FileControlPacket Object");
		fileControlPack = (FileControlPacket) inStream.readObject();
		fileSendControlSocket.close();
	}

	private void sendFileControlPacket() throws IOException
	{
		System.out.println("FSCT: Setting up a socket Connection");
		fileSendControlSocket = new Socket(serverIP, fileControlPort);
		System.out.println("FSCT: Initializing Output Stream");
		outStream = new ObjectOutputStream(
				fileSendControlSocket.getOutputStream());
		System.out.println("FSCT: Writing FIleControlPacket into outStream");
		outStream.writeObject(fileControlPack);
		outStream.flush();
	}
}
