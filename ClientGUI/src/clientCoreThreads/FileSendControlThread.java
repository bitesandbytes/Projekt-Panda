package clientCoreThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JTextArea;

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
	private JTextArea msgBox;
	private JButton fileButton;

	public FileSendControlThread(String dn, String fp, JTextArea msgBox,
			JButton fileButton)
	{
		super();
		this.destNick = dn;
		this.filePath = fp;
		this.msgBox = msgBox;
		this.fileButton = fileButton;
	}

	public void run()
	{
		Global.Log("File Sent Request Found.");
		String[] temp;
		temp = filePath.split("/");
		fileName = temp[temp.length - 1];
		fileControlPack = new FileControlPacket(false, destNick, false);
		fileControlPack.fileName = fileName;
		int retryCount = 3;
		Global.Log("FSCT: Pinging server for Client IP.");
		while (retryCount > 0)
		{
			try
			{
				sendFileControlPacket();
				break;
			}
			catch (IOException ex)
			{
				Global.Log("Retrying fileControlPacket send | FileSendControlThread.");
				retryCount--;
				continue;
			}
		}
		Global.Log("FSCT: Successfully Pinged Server. Waiting For Server to Send Back Control Packet");
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
				Global.Log("Retrying fileControlPacket receive | FileSendControlThread.");
				retryCount--;
				continue;
			}
			catch (ClassNotFoundException e)
			{
				Global.Log("Invalid packet received | FileSendControlThread.");
				writeError();
				return;
			}
		}

		if (fileControlPack.isIP == true
				&& fileControlPack.payload.equals("0.0.0.0"))
		{
			Global.Log("FSCT: User is offline. Try Again Later");
			writeError();
			return;
		}
		else
		{
			Global.Log("FSCT: Setting up connection directly to client");
			(new FileSenderThread(filePath, fileName, fileControlPack.payload,
					msgBox, fileButton)).start();
			return;
		}
	}

	private void receiveFileControlPacket() throws IOException,
			ClassNotFoundException
	{
		Global.Log("FSCT: Initializing Input Stream");
		inStream = new ObjectInputStream(fileSendControlSocket.getInputStream());
		Global.Log("FSCT: Reading FileControlPacket Object");
		fileControlPack = (FileControlPacket) inStream.readObject();
		fileSendControlSocket.close();
	}

	private void sendFileControlPacket() throws IOException
	{
		Global.Log("FSCT: Setting up a socket Connection");
		fileSendControlSocket = new Socket(serverIP, fileControlPort);
		Global.Log("FSCT: Initializing Output Stream");
		outStream = new ObjectOutputStream(
				fileSendControlSocket.getOutputStream());
		Global.Log("FSCT: Writing FIleControlPacket into outStream");
		outStream.writeObject(fileControlPack);
		outStream.flush();
	}

	private void writeError()
	{
		synchronized (msgBox)
		{
			msgBox.append("\n" + fileName + " transfer failed. Try again.");
		}
		return;
	}
}
