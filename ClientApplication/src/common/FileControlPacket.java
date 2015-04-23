package common;

import java.io.Serializable;

public class FileControlPacket implements Serializable
{
	private static final long serialVersionUID = 1465289083771282932L;
	public boolean isServer;
	public String payload;
	public String fileName;
	public boolean isIP;
	
	public FileControlPacket(boolean isServer, String payload, boolean isIP)
	{
		this.isIP = isIP;
		this.isServer = isServer;
		this.payload = payload;
		this.fileName = null;
	}
}
