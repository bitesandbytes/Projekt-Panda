package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewChat
{

	private JFrame frmStartANew;
	private JTextField textField;
	private JLabel lblStartingANew;

	public NewChat()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmStartANew = new JFrame();
		frmStartANew.setTitle("New Chat");
		frmStartANew.setBounds(100, 100, 217, 172);
		frmStartANew.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmStartANew.getContentPane().setLayout(null);

		JLabel lblNick = new JLabel("Nick : ");
		lblNick.setHorizontalAlignment(SwingConstants.LEFT);
		lblNick.setFont(new Font("LM Roman Unslanted 10", Font.PLAIN, 16));
		lblNick.setBounds(12, 56, 51, 27);
		frmStartANew.getContentPane().add(lblNick);

		textField = new JTextField();
		textField.setBounds(61, 61, 134, 21);
		frmStartANew.getContentPane().add(textField);
		textField.setColumns(10);

		lblStartingANew = new JLabel("Starting a new chat");
		lblStartingANew.setHorizontalAlignment(SwingConstants.CENTER);
		lblStartingANew.setFont(new Font("LM Roman Unslanted 10", Font.PLAIN,
				16));
		lblStartingANew.setBounds(12, 12, 183, 27);
		frmStartANew.getContentPane().add(lblStartingANew);

		JButton btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(12, 96, 183, 34);
		frmStartANew.getContentPane().add(btnNewButton);
	}
}
