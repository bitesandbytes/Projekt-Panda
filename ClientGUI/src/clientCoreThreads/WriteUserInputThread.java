package clientCoreThreads;

import javax.swing.JTextArea;

import common.Message;
import coreClient.Global;
import coreClient.MessageQueue;

public class WriteUserInputThread extends Thread
{
	private String curText;
	private String curFriend;
	private Message curMessage;
	public MessageQueue messageQueue = Global.msgQueue;
	public JTextArea msgBox;

	public WriteUserInputThread(JTextArea msgBox)
	{
		super();
		this.msgBox = msgBox;
	}

	public void setFriend(String destNick)
	{
		this.curFriend = destNick;
	}

	public void run()
	{
		while (true)
		{
			synchronized (msgBox)
			{
				try
				{
					msgBox.wait();
				}
				catch (InterruptedException e)
				{
					continue;
				}
				curText = msgBox.getText();
				msgBox.setText("");
			}

			if (curText == "")
				continue;
			curMessage = new Message(Global.myNick, curFriend, curText);
			Global.Log("sentMessage" + curMessage);
			synchronized (messageQueue)
			{
				messageQueue.addMessage(curMessage);
				messageQueue.notify();
			}
		}
	}
}
