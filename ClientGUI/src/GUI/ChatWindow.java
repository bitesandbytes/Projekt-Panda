package GUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import common.Message;
import coreClient.Global;
import coreClient.MessageQueue;
import clientCoreThreads.ChatReceiveThread;
import clientCoreThreads.WriteUserInputThread;

public class ChatWindow
{

	private JFrame frmChatServerV;
	private JTextField addNewUser;
	private JFileChooser fileChooser;
	private WriteUserInputThread msgWriter;
	DefaultListModel listModel;
	JList UserList;
	private JTextArea currentSendMessageBox;
	private JTextArea currentChatBox;
	private JLabel filename;
	private JLabel fileSize;
	File curFriendFile;
	PrintWriter writer;
	private String userContainerPath = Global.userContainerPath;
	JLabel curFriend =  Global.currentFriendLabel;
	private Message curMessage;
	public MessageQueue messageQueue = Global.msgQueue;
	private JButton fileTransfer;

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
					ChatWindow window = new ChatWindow();
					window.frmChatServerV.setVisible(true);
					window.frmChatServerV.setResizable(false);
					window.frmChatServerV.setLocationRelativeTo(null);
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

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		fileChooser = new JFileChooser();
		frmChatServerV = new JFrame();
		frmChatServerV.setTitle("Chat Server v0.1");
		frmChatServerV.setBounds(100, 100, 552, 451);
		frmChatServerV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatServerV.getContentPane().setLayout(null);

		curFriend = new JLabel("New label");
		curFriend.setBounds(164, 12, 61, 17);
		frmChatServerV.getContentPane().add(curFriend);

		listModel = new DefaultListModel();
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 71, 140, 338);
		frmChatServerV.getContentPane().add(scrollPane_2);

		UserList = new JList(listModel);
		UserList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2)
				{
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0)
					{
						Object o = theList.getModel().getElementAt(index);
						System.out.println("Double-clicked on: '"
								+ o.toString() + "'");
						
						curFriend.setText(o.toString());
						System.out.println(userContainerPath + curFriend.getText());
						String content = null;
						try {
							content = new Scanner(new File(userContainerPath + curFriend.getText())).useDelimiter("\\Z").next();
						} catch (FileNotFoundException e) {
							Global.Log("Can't Open File of: "+curFriend.getText());
						}
						currentSendMessageBox.setEditable(true);
						currentChatBox.setText(null);
						currentChatBox.setText(content);
					}
				}
			}
		});
		UserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_2.setViewportView(UserList);

		addNewUser = new JTextField();
		addNewUser.setBounds(12, 41, 140, 21);
		addNewUser.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String newUser = addNewUser.getText();
					// TODO : Append This New User to Users
					Global.Log("ENTER pressed. Value Entered: " + newUser);
					addNewUser.setText(null);
					if (newUser.equals(""))
					{
						Global.Log("Empty Value. Do nothing");
						return;
					}
					if (listModel.contains(newUser) == false)
					{
						listModel.addElement(newUser);

						curFriendFile = new File(userContainerPath + newUser);
						try {
							if (curFriendFile.createNewFile()) {
								Global.Log("File is created!");
							}
							else
							{
								Global.Log("File already exists.");
							}
						}
						catch (IOException e1)
						{
							Global.Log("File Cannot be created");
						}
					}
				}
			}
		});
		frmChatServerV.getContentPane().add(addNewUser);
		addNewUser.setColumns(10);

		JLabel lblAddNewUser = new JLabel("Add New User:");
		lblAddNewUser.setBounds(12, 12, 140, 17);
		frmChatServerV.getContentPane().add(lblAddNewUser);

		JButton btnSendMessage = new JButton("Send");
		btnSendMessage.setBounds(467, 307, 67, 45);
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!fileSize.getText().equals("0 Bytes")) {
					currentSendMessageBox.append("\n" + "File : "

							+ filename.getText() + ", Size : "
							+ fileSize.getText() + ".");
				}
				String msg;
				synchronized (currentSendMessageBox) {
					msg = currentSendMessageBox.getText();
					currentSendMessageBox.notify();

				}
				if (msg.length() > 0)
				{
					currentChatBox.append("\n" + "You: ");
					currentChatBox.append(msg);
				}
				msgWriter.setFriend(curFriend.getText());
				// TODO :: Invoke file sender thread.
				filename.setText("No file chosen.");
				fileSize.setText("0 Bytes");
			}
		});
		frmChatServerV.getContentPane().add(btnSendMessage);

		fileTransfer = new JButton("File");
		fileTransfer.setBounds(164, 386, 69, 23);
		fileTransfer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int hasSelected = fileChooser.showOpenDialog(frmChatServerV);
				if (hasSelected == JFileChooser.APPROVE_OPTION)
				{
					File curFile = fileChooser.getSelectedFile();
					filename.setText(curFile.getName());
					long size = curFile.length();
					if (size > 1000000)
					{
						float val = (size / 1000000);
						fileSize.setText(val + "MB");
					}
					else if (size > 1000)
					{
						float val = (size / 1000);
						fileSize.setText(val + "KB");
					}
					else if (size > 0)
					{
						fileSize.setText(size + "B");
					}
					else
					{
						fileSize.setText("Too large.");
					}
				}
				else
				{
					filename.setText("No file chosen.");
					fileSize.setText("0 bytes");
				}
			}
		});
		frmChatServerV.getContentPane().add(fileTransfer);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(164, 289, 290, 85);
		frmChatServerV.getContentPane().add(scrollPane);


		currentSendMessageBox = new JTextArea();
		currentSendMessageBox.setEditable(false);
		currentSendMessageBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String curText = currentSendMessageBox.getText();
					System.out.println("ENTER pressed. Value Entered: "
							+ curText);
					addNewUser.setText(null);
					if (curText.equals("") == false){
						currentChatBox.append(curText);
						curMessage = new Message("1", curFriend.getText(), curText);
						//curMessage.content = ;
						//curMessage.destNick = ;
						synchronized (messageQueue)
						{
							messageQueue.addMessage(curMessage);
							messageQueue.notify();
						}
						try {
							writer = new PrintWriter(userContainerPath+curFriend.getText(), "UTF-8");
						} catch (FileNotFoundException e1) {
							System.out.println("Unable To Find File");
						} catch (UnsupportedEncodingException e1) {
							System.out.println("Stop Using UTF-8");
						}
						writer.print(curText);
						writer.close();
					}
					if(!fileSize.getText().equals("0 Bytes")){
						currentSendMessageBox.append("\n" + "File : "
								+ filename.getText() + ", Size : "
								+ fileSize.getText() + ".");

					}

				}

			}
		});
		scrollPane.setViewportView(currentSendMessageBox);
		msgWriter = new WriteUserInputThread(currentSendMessageBox);
		msgWriter.start();

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(164, 41, 370, 236);
		frmChatServerV.getContentPane().add(scrollPane_1);

		currentChatBox = new JTextArea();
		currentChatBox.setEditable(false);
		scrollPane_1.setViewportView(currentChatBox);
		(new ChatReceiveThread(currentChatBox)).start();

		filename = new JLabel("No file chosen.");
		filename.setFont(new Font("Liberation Sans", Font.PLAIN, 13));
		filename.setBounds(251, 389, 176, 20);
		frmChatServerV.getContentPane().add(filename);

		fileSize = new JLabel("0 Bytes");
		fileSize.setHorizontalAlignment(SwingConstants.CENTER);
		fileSize.setFont(new Font("Liberation Sans", Font.PLAIN, 13));
		fileSize.setBounds(439, 390, 80, 17);
		frmChatServerV.getContentPane().add(fileSize);
	}

	public void enableFileTransfer()
	{
		fileTransfer.setEnabled(true);
		filename.setText("No file chosen.");
		fileSize.setText("0 Bytes");
	}
}
