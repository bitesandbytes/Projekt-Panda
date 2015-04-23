import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Cursor;

public class ClientGUI
{

	private JFrame loginScreen;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ClientGUI window = new ClientGUI();
					window.loginScreen.setVisible(true);
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
	public ClientGUI()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		loginScreen = new JFrame();
		loginScreen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginScreen.setFont(new Font("Oxygen-Sans", Font.BOLD, 13));
		loginScreen.setTitle("Chat Server v0.1");
		loginScreen.setBounds(100, 100, 410, 243);
		loginScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginScreen.getContentPane().setLayout(null);
		loginScreen.setResizable(false);
		
		JLabel lblNick = new JLabel("Nick");
		lblNick.setBounds(64, 63, 70, 15);
		loginScreen.getContentPane().add(lblNick);

		JLabel lblWelcomeToChat = new JLabel("Welcome to Chat Server v0.1");
		lblWelcomeToChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToChat.setBounds(38, 12, 302, 25);
		loginScreen.getContentPane().add(lblWelcomeToChat);

		TextField nickField = new TextField();
		nickField.setBounds(155, 57, 163, 25);
		loginScreen.getContentPane().add(nickField);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(64, 107, 70, 15);
		loginScreen.getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setToolTipText("Enter your password here");
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		passwordField.setBounds(155, 100, 163, 30);
		loginScreen.getContentPane().add(passwordField);

		JButton btnNewUser = new JButton("New User");
		btnNewUser.setBounds(225, 154, 129, 25);
		loginScreen.getContentPane().add(btnNewUser);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(64, 154, 129, 25);
		loginScreen.getContentPane().add(btnLogin);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(UIManager.getColor("CheckBox.background"));
		menuBar.setBounds(0, 0, 129, 21);
		loginScreen.getContentPane().add(menuBar);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setOpaque(true);
		mntmAbout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				AboutWindow aboutWindow = new AboutWindow();
				aboutWindow.setVisible(true);
			}
		});
		menuBar.add(mntmAbout);
	}
}
