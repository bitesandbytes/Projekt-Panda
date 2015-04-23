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
	private final static int fileControlPort = Global.fileControlPort;
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
		String[] temp;
		temp = filePath.split("/");
		fileName = temp[temp.length - 1];
		fileControlPack = new FileControlPacket(false, destNick, false);
		fileControlPack.fileName = fileName;
		int retryCount = 3;
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
		if (fileControlPack.isIP == false)
		{
			System.out.println("FSCT: User is offline. Try Again Later");
		}
		else
		{
			(new FileSenderThread(filePath, fileControlPack.payload)).start();

		}
	}

	private void receiveFileControlPacket() throws IOException,
			ClassNotFoundException
	{
		inStream = new ObjectInputStream(fileSendControlSocket.getInputStream());
		fileControlPack = (FileControlPacket) inStream.readObject();
		fileSendControlSocket.close();
	}

	private void sendFileControlPacket() throws IOException
	{
		fileSendControlSocket = new Socket(serverIP, fileControlPort);
		outStream = new ObjectOutputStream(
				fileSendControlSocket.getOutputStream());
		outStream.writeObject(fileControlPack);
		outStream.flush();
	}
}
