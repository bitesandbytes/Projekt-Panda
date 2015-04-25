package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChatWindow {

	private JFrame frmChatServerV;
	private JTextField addNewUser;
	DefaultListModel listModel;
	JList UserList;

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
		
		final JLabel curFriend = new JLabel("New label");
		curFriend.setBounds(164, 12, 61, 17);
		frmChatServerV.getContentPane().add(curFriend);
		
		listModel = new DefaultListModel();
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 71, 140, 338);
		frmChatServerV.getContentPane().add(scrollPane_2);
		
		
		
		UserList = new JList(listModel);
		UserList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
		        if (mouseEvent.getClickCount() == 2) {
		          int index = theList.locationToIndex(mouseEvent.getPoint());
		          if (index >= 0) {
		            Object o = theList.getModel().getElementAt(index);
		            System.out.println("Double-clicked on: '" + o.toString() + "'");
		            curFriend.setText(o.toString());
		          }
		        }
			}
		});
		UserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_2.setViewportView(UserList);
		
		

		addNewUser = new JTextField();
		addNewUser.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String newUser = addNewUser.getText();
					// To DO: Append This New User to Users
					System.out.println("ENTER pressed. Value Entered: "
							+ newUser);
					addNewUser.setText(null);
					if (newUser.equals("")){
						System.out.println("Empty Value. Do nothing");
						return;
					}
					listModel.addElement(newUser);
					
				}
			}
		});
		addNewUser.setBounds(12, 41, 140, 21);
		frmChatServerV.getContentPane().add(addNewUser);
		addNewUser.setColumns(10);
		
		

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

		JTextArea currentMessageBox = new JTextArea();
		currentMessageBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String curText = addNewUser.getText();
					// To DO: Append This New User to Users
					String currFriend = curFriend.getText();
					System.out.println("ENTER pressed. Value Entered: "
							+ curText);
					if (curText.equals(""))
						System.out.println("Empty Value. Do nothing");
					addNewUser.setText(null);
				}

			}
		});
		scrollPane.setViewportView(currentMessageBox);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(164, 41, 370, 236);
		frmChatServerV.getContentPane().add(scrollPane_1);

		JTextArea currentChatBox = new JTextArea();
		currentChatBox.setEditable(false);
		scrollPane_1.setViewportView(currentChatBox);

		

		
		
	}
}
