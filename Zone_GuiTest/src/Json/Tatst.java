package Json;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class Tatst extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			private Tatst frame;

			public void run() {
				try {
					 frame = new Tatst();
					 
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
	public Tatst() {
		this.addWindowListener(new hello());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Wo  like you ");
		lblNewLabel.setBounds(27, 33, 72, 15);
		contentPane.add(lblNewLabel);
	}
    static class hello extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
        	System.out.println("¹þ¹þ");
           System.exit(0);
        }
    }
}
