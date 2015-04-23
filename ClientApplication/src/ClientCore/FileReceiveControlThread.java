package ClientCore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import common.FileControlPacket;

public class FileReceiveControlThread extends Thread {
	private Socket messageReceiverSocket;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private FileControlPacket fileControlPack;
	private final static int fileControlReceivePort = 4500;
	private String receiveFileName;

	public FileReceiveControlThread() {
		super();
	}
	
	public void receiveFileControlPacket() throws IOException, ClassNotFoundException{
		System.out.println("FRCT: Accepting connection");
		messageReceiverSocket = new Socket();
		messageReceiverSocket = (new ServerSocket(fileControlReceivePort)).accept();
		System.out.println("FRCT: Initializing input Stream");
		inStream = new ObjectInputStream(messageReceiverSocket.getInputStream());
		System.out.println("FRCT: Attempting to read FileControlPacket Object");
		fileControlPack = (FileControlPacket) inStream.readObject();
		
	}
	
	public void sendFileControlPacket() throws IOException{
		System.out.println("FRCT: Initializing output Stream");
		outStream = new ObjectOutputStream(messageReceiverSocket.getOutputStream());
		fileControlPack.isServer = false;
		System.out.println("FRCT: Writing FileControlPacket output Stream");
		outStream.writeObject(fileControlPack);
		outStream.flush();
		System.out.println("FRCT: Closing Socket");
		messageReceiverSocket.close();
		
	}
	
	public void run() {
		while (true) {
			fileControlPack = null;
			int retryCount = 3;	//should be very high
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
							.println("Retrying fileControlPacket receive | FileReceiveControlThread.");
					retryCount--;
					continue;
				}
				catch (ClassNotFoundException e)
				{
					System.out
							.println("Invalid packet received | FileReceiveControlThread.");
					return;
				}
			}
			
			if(fileControlPack == null || fileControlPack.isServer == false){
				System.out.println("FRCT: Did not receive valid FileControlPacket.");
				try {
					messageReceiverSocket.close();
				} catch (IOException e) {
					System.out.println("FRCT: Unable to close socket.");
				}
				continue;
			}
			receiveFileName = fileControlPack.fileName;
			retryCount = 3;
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
			break;
			//Break and Start FileReceiverThread
		}
		(new FileReceiverThread(receiveFileName)).start();
	}
}
