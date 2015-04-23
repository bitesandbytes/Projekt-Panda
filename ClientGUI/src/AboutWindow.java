import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class AboutWindow extends JFrame
{

	private static final long serialVersionUID = -7067924545758370214L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					AboutWindow frame = new AboutWindow();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AboutWindow()
	{
		setAlwaysOnTop(true);
		setTitle("About");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 389, 240);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		AboutWindow.getFrames()[0].setResizable(false);
		
		JLabel label = new JLabel("Chat Server v0.1");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(12, 12, 366, 32);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Features:");
		label_1.setBounds(12, 42, 99, 22);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("1. Supports anonymous chat server capabilities.");
		label_2.setBounds(12, 64, 366, 22);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("2. Supports File Sharing in same subnet.");
		label_3.setBounds(12, 86, 366, 22);
		contentPane.add(label_3);
		
		JLabel label_4 = new JLabel("Maintained by:");
		label_4.setBounds(12, 120, 195, 22);
		contentPane.add(label_4);
		
		JLabel label_5 = new JLabel("Akshay Balaji (github.com/akshay-balaji)");
		label_5.setBounds(12, 158, 346, 15);
		contentPane.add(label_5);
		
		JLabel label_6 = new JLabel("J S Suhas (github.com/bitesandbytes)");
		label_6.setBounds(12, 142, 281, 15);
		contentPane.add(label_6);
	}
}
