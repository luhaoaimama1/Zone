package Json;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;

import Json.GuiJson.Statue;

public class Dialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Label label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Dialog dialog = new Dialog(Statue.OK);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @param temp 
	 */
	public Dialog(Statue temp) {
		setBounds(100, 100, 414, 157);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			temp=Statue.JSON_ERROR;
			if(temp==Statue.JSON_ERROR){
				label = new Label("http://www.bejson.com/");
				label.setForeground(Color.blue);
				label.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						//鼠标点击
						super.mouseClicked(e);
						try {
							URI uri=new URI("http://www.bejson.com/");
							Desktop desktop=Desktop.getDesktop(); 
							//是否支持此类和对象浏览 
							if(desktop.isDesktopSupported()&&desktop.isSupported(Desktop.Action.BROWSE)){
									desktop.browse(uri);
							}
						} catch (URISyntaxException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						//鼠标移入
						super.mouseEntered(e);
						label.setForeground(Color.red);
						
					}
					@Override
					public void mouseExited(MouseEvent e) {
						//鼠标移出
						super.mouseExited(e);
						label.setForeground(Color.blue);
					}
				});
				contentPanel.add(label);
			}
			{
				JLabel lblJson = new JLabel();
				switch (temp) {
				case FIRSTCLASSNAME:
					lblJson.setText("最顶层类名不能为空！");
					break;
				case PACKAGENAME:
					lblJson.setText("包名不能为空！ ");
					break;
				case JSON_ERROR:
					lblJson.setText("json格式异常,请点击此处 到该网站确保其格式正确");
					break;
				case OUTSAVEPATH:
					lblJson.setText("输出路径不能为空！");
					break;
				case JSON_EMPTY:
					lblJson.setText("json不能为空！");
					break;

				default:
					break;
				}
			
				contentPanel.add(lblJson);
			}
			
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
//				addActionListener
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Dialog.this.setVisible(false);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Dialog.this.setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

}
