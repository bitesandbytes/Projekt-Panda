package coreServer;

public class User
{
	public String nick;
	public String password;
	public PendingQueue pendingMessageQueue;
	
	User(String nick, String pass)
	{
		this.nick = nick;
		this.password = pass;
		this.pendingMessageQueue = new PendingQueue();
	}
}
