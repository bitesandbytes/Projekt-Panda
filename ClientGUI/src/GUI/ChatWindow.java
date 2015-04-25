package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

public class ChatWindow
{

	private JFrame frmChatServerV;

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
					ChatWindow window = new ChatWindow();
					window.frmChatServerV.setVisible(true);
					window.frmChatServerV.setResizable(false);
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
	public ChatWindow()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmChatServerV = new JFrame();
		frmChatServerV.setTitle("Chat Server v0.1");
		frmChatServerV.setBounds(100, 100, 556, 454);
		frmChatServerV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatServerV.getContentPane().setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 121, 23);
		frmChatServerV.getContentPane().add(menuBar);

		JMenuItem mntmNew = new JMenuItem("New");
		menuBar.add(mntmNew);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				frmChatServerV.setVisible(false);
				frmChatServerV.dispose();
				System.exit(0);
			}
		});
		menuBar.add(mntmExit);
	}
}
