package Paint;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import java.awt.TextArea;

import javax.swing.JLabel;

public class Gui_Json extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui_Json frame = new Gui_Json();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui_Json() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 683, 500);
		setTitle("Œ“œ≤ª∂ƒ„");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Generate");
		btnNewButton.setBounds(236, 417, 175, 34);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(btnNewButton);
		
		TextArea textArea = new TextArea();
		textArea.setBounds(10, 53, 647, 358);
		contentPane.add(textArea);
		
		JLabel lblTopclassname = new JLabel("TopClassName:");
		lblTopclassname.setBounds(140, 15, 99, 15);
		contentPane.add(lblTopclassname);
		
		textField = new JTextField();
		textField.setBounds(236, 10, 189, 25);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("click show errMsg");
		lblNewLabel.setBounds(480, 422, 145, 25);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setVisible(false);
		contentPane.add(lblNewLabel);
	}
}
