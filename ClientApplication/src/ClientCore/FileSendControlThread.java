package ClientCore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import common.FileControlPacket;

public class FileSendControlThread extends Thread {
	private Socket fileSendControlSocket;
	private final static String serverIP = "10.42.0.27";
	private final static int fileControlPort = 4400;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private String destNick;
	private FileControlPacket fileControlPack;
	private String filePath;
	private String fileName;

	public FileSendControlThread(String dn, String fp) {
		super();
		destNick = dn;
		filePath = fp;
	}

	public void run() {
		String [] temp;
		temp = filePath.split("/");
		fileName = temp[temp.length-1];
		try {
			fileSendControlSocket = new Socket();
			fileSendControlSocket.connect(new InetSocketAddress(serverIP,
					fileControlPort));
		} catch (IOException e1) {
			System.out.println("FSIT: Could not connect to server.");
		}
		System.out.println("FSIT: Intializing output Stream");
		try {
			outStream = new ObjectOutputStream(
					fileSendControlSocket.getOutputStream());
		} catch (IOException e) {
			System.out.println("FSIT: Unable to initialize output Stream");
		}
		fileControlPack = new FileControlPacket(false, destNick, false);
		fileControlPack.fileName = fileName;
		try {
			outStream.writeObject(fileControlPack);
			outStream.flush();
		} catch (IOException e) {
			System.out.println("FSIT: Unable to write into output stream.");
		}
		try {
			inStream = new ObjectInputStream(
					fileSendControlSocket.getInputStream());
		} catch (IOException e) {
			System.out.println("FSIT: Unable to initialize input Stream");
		}
		try {
			fileControlPack = (FileControlPacket) inStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.out
					.println("FSIT: Unable to read input as FileControlPacket object");
		}
		try {
			fileSendControlSocket.close();
		} catch (IOException e) {
		}
		if (fileControlPack.isIP == false) {
			System.out.println("FSIT: User is offline. Try Again Later");
		} else {
			(new FileSenderThread(filePath, fileControlPack.payload)).start();

		}
	}
}
