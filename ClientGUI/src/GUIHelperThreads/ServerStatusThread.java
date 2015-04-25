package GUIHelperThreads;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JLabel;

import coreClient.Global;

public class ServerStatusThread extends Thread
{
	private JLabel serverStatus;
	private JButton login;
	private JButton register;
	private boolean pingResult;
	private String serverIP = Global.serverIP;

	public ServerStatusThread(JLabel serverStatus, JButton login,
			JButton register)
	{
		this.serverStatus = serverStatus;
		this.login = login;
		this.register = register;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				pingResult = InetAddress.getByName(serverIP).isReachable(Global.pingTimeout);
				if (pingResult)
				{
					serverStatus.setForeground(Color.green);
					serverStatus.setText("Server online.");
					login.setEnabled(true);
					register.setEnabled(true);
				}
				else
				{
					serverStatus.setForeground(Color.red);
					serverStatus.setText("Server offline.");
					login.setEnabled(false);
					register.setEnabled(false);
				}
				Thread.sleep(1000);
			}
			catch (IOException e)
			{
				serverStatus.setForeground(Color.red);
				serverStatus.setText("Server offline.");
				login.setEnabled(false);
				register.setEnabled(false);
				try
				{
					Thread.sleep(5000);
				}
				catch (InterruptedException e1)
				{
					continue;
				}
			}
			catch (InterruptedException e)
			{
				continue;
			}
		}
	}
}
