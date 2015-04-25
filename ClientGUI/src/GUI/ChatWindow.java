package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class ChatWindow {

	private JFrame frmChatServerV;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow();
					window.frmChatServerV.setVisible(true);
					window.frmChatServerV.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChatServerV = new JFrame();
		frmChatServerV.setTitle("Chat Server v0.1");
		frmChatServerV.setBounds(100, 100, 556, 454);
		frmChatServerV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatServerV.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(12, 41, 140, 21);
		frmChatServerV.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblAddNewUser = new JLabel("Add New User:");
		lblAddNewUser.setBounds(12, 12, 140, 17);
		frmChatServerV.getContentPane().add(lblAddNewUser);
		
		JButton btnSendMessage = new JButton("Send");
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSendMessage.setBounds(471, 323, 63, 23);
		frmChatServerV.getContentPane().add(btnSendMessage);
		
		JButton btnTransferFile = new JButton("File");
		btnTransferFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnTransferFile.setBounds(471, 365, 63, 23);
		frmChatServerV.getContentPane().add(btnTransferFile);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(164, 294, 290, 115);
		frmChatServerV.getContentPane().add(scrollPane);
		
		JTextArea textArea_1 = new JTextArea();
		scrollPane.setViewportView(textArea_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(164, 12, 370, 265);
		frmChatServerV.getContentPane().add(scrollPane_1);
		
		JTextArea textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 71, 140, 338);
		frmChatServerV.getContentPane().add(scrollPane_2);
		
		JTextArea textArea_2 = new JTextArea();
		scrollPane_2.setViewportView(textArea_2);
	}
}
