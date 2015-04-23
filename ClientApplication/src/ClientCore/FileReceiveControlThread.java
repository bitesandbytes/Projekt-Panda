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
	public void run() {
		while (true) {
			fileControlPack = null;
			try {
				messageReceiverSocket = new Socket();
				messageReceiverSocket = (new ServerSocket(
						fileControlReceivePort)).accept();
			} catch (IOException e1) {
				System.out.println("FRCT: Could not connect to server.");
				try {
					messageReceiverSocket.close();
				} catch (IOException e) {
					// System.out.println("CRT: Unable to close socket");
					continue;
				}
			}
			try {
				inStream = new ObjectInputStream(messageReceiverSocket.getInputStream());
			} catch (IOException e) {
				System.out.println("FRCT: Failed to obtain object input stream from client socket.");
			}
			System.out.println("FRCT: Attempting to read FileControlPacket Object");
			try {
				fileControlPack = (FileControlPacket) inStream.readObject();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("FRCT: Unable to read input as FileControlPacket object");
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
			try {
				outStream = new ObjectOutputStream(messageReceiverSocket.getOutputStream());
			} catch (IOException e) {
				System.out.println("FRCT: Failed to obtain object output stream from client socket.");
			}
			fileControlPack.isServer = false;
			try {
				outStream.writeObject(fileControlPack);
				outStream.flush();
			} catch (IOException e) {
				System.out.println("FRCT: Unable to write into output stream.");
			}
			try {
				messageReceiverSocket.close();
			} catch (IOException e) {
				System.out.println("FRCT: Unable to Close Socket");
			}
			break;
			//Break and Start FileReceiverThread
		}
		(new FileReceiverThread(receiveFileName)).start();
	}
}
