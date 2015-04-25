package GUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import common.LoginRequest;
import clientCoreThreads.LoginRequestThread;
import clientCoreThreads.RegisterRequestThread;
import GUIHelperThreads.ServerStatusThread;

import java.awt.Rectangle;

public class TabbedLogin
{

	private JFrame frmChatServerV;
	private JTextField nickLogin;
	private JPasswordField passwordLogin;
	private JTextField nickRegister;
	private JPasswordField passwordRegister;
	private ServerStatusThread serverStatusThread;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager
					.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					TabbedLogin window = new TabbedLogin();
					window.frmChatServerV.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TabbedLogin()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmChatServerV = new JFrame();
		frmChatServerV.setBounds(new Rectangle(600, 350, 320, 220));
		frmChatServerV.setTitle("Chat Server v0.1");
		frmChatServerV.setBounds(100, 100, 324, 258);
		frmChatServerV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatServerV.getContentPane().setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 123, 23);
		frmChatServerV.getContentPane().add(menuBar);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				AboutWindow aboutWindow = new AboutWindow();
				aboutWindow.setVisible(true);
				aboutWindow.setLocationRelativeTo(null);
				aboutWindow.setResizable(false);
			}
		});
		menuBar.add(mntmAbout);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frmChatServerV.setVisible(false);
				frmChatServerV.dispose();
			}
		});
		menuBar.add(mntmExit);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 23, 318, 170);
		frmChatServerV.getContentPane().add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Login", null, panel, null);
		panel.setLayout(null);

		JLabel label = new JLabel("Nick");
		label.setFont(new Font("LM Roman Unslanted 10", Font.PLAIN, 18));
		label.setBounds(39, 12, 98, 33);
		panel.add(label);

		nickLogin = new JTextField();
		nickLogin.setColumns(10);
		nickLogin.setBounds(155, 21, 134, 21);
		panel.add(nickLogin);

		JLabel label_3 = new JLabel("Password");
		label_3.setFont(new Font("LM Roman Unslanted 10", Font.PLAIN, 18));
		label_3.setBounds(39, 45, 98, 33);
		panel.add(label_3);

		passwordLogin = new JPasswordField();
		passwordLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				login();
			}
		});
		passwordLogin.setBounds(155, 54, 134, 21);
		panel.add(passwordLogin);

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				login();
			}
		});
		loginButton.setBounds(92, 89, 134, 26);
		panel.add(loginButton);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Register", null, panel_1, null);
		panel_1.setLayout(null);

		JLabel label_1 = new JLabel("Nick");
		label_1.setFont(new Font("LM Roman Unslanted 10", Font.PLAIN, 18));
		label_1.setBounds(37, 12, 98, 33);
		panel_1.add(label_1);

		nickRegister = new JTextField();
		nickRegister.setColumns(10);
		nickRegister.setBounds(153, 21, 134, 21);
		panel_1.add(nickRegister);

		JLabel label_2 = new JLabel("Password");
		label_2.setFont(new Font("LM Roman Unslanted 10", Font.PLAIN, 18));
		label_2.setBounds(37, 45, 98, 33);
		panel_1.add(label_2);

		passwordRegister = new JPasswordField();
		passwordRegister.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				register();
			}
		});
		passwordRegister.setBounds(153, 54, 134, 21);
		panel_1.add(passwordRegister);

		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				register();
			}
		});
		registerButton.setBounds(90, 89, 134, 26);
		panel_1.add(registerButton);

		JLabel lblServerStatus = new JLabel("Server Status : ");
		lblServerStatus.setBounds(10, 197, 105, 23);
		frmChatServerV.getContentPane().add(lblServerStatus);

		JLabel serverStatus = new JLabel("");
		serverStatus.setBounds(127, 197, 153, 23);
		frmChatServerV.getContentPane().add(serverStatus);
		serverStatusThread = new ServerStatusThread(serverStatus, loginButton,
				registerButton);
		serverStatusThread.start();

		frmChatServerV.setLocationRelativeTo(null);
	}

	protected void login()
	{
		String nick = nickLogin.getText();
		String password = new String(passwordLogin.getPassword());
		(new LoginRequestThread(new LoginRequest(nick, password),
				frmChatServerV)).start();
	}

	protected void register()
	{
		String nick = nickRegister.getText();
		String password = new String(passwordRegister.getPassword());
		(new RegisterRequestThread(new LoginRequest(nick, password),
				frmChatServerV)).start();
	}
}
