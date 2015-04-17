package coreServer;

import common.Message;
import common.LoginRequest;

public class Request
{
	public boolean isMessage;
	public Message msgContent;
	public LoginRequest loginRequest; // Modify this for final deployment.
	public int retryCount;

	public Request(Message msg)
	{
		this.isMessage = true;
		this.msgContent = msg;
		this.loginRequest = null;
		this.retryCount = 3;
	}

	public Request(LoginRequest login)
	{
		this.isMessage = false;
		this.loginRequest = login;
		this.msgContent = null;
		this.retryCount = 3;
	}
}
