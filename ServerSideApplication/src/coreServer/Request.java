package coreServer;

import common.Message;
import common.User;

public class Request
{
	public boolean isMessage;
	public Message msgContent;
	public User controlObj; // Modify this for final deployment.
	public int retryCount;

	public Request(Message msg)
	{
		this.isMessage = true;
		this.msgContent = msg;
		this.controlObj = null;
		this.retryCount = 3;
	}

	public Request(User controlObj)
	{
		this.isMessage = false;
		this.controlObj = controlObj;
		this.msgContent = null;
		this.retryCount = 3;
	}
}
