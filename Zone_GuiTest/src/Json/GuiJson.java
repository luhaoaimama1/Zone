package Json;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Label;
import java.io.File;


public class GuiJson extends JFrame {
	public enum Statue{
		PACKAGENAME,FIRSTCLASSNAME,OUTSAVEPATH,JSON_EMPTY,JSON_ERROR,OK
	}

	private JPanel contentPane;
	private TextField packageName;
	private TextField firstClassName;
	private TextField outSavePath;
	private TextArea jsonStr;
	private Statue temp;
	private static final File CONFIG_FILE=new File("Config.txt");
	private  Gson gson=new Gson();
	private String savePath_Real="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiJson frame = new GuiJson();
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
	public GuiJson() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 648, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		jsonStr = new TextArea();
		jsonStr.setBounds(10, 64, 612, 322);
		contentPane.add(jsonStr);
		
		Label label = new Label("包名:");
		label.setBounds(10, 22, 33, 23);
		contentPane.add(label);
		
		Label label_1 = new Label("最顶层类名:");
		label_1.setBounds(166, 22, 69, 23);
		contentPane.add(label_1);
		
		
		Label label_2 = new Label("输出路径:");
		label_2.setBounds(362, 22, 56, 23);
		contentPane.add(label_2);
		
		Label label_3 = new Label("作者: Zone ");
		label_3.setBounds(218, 426, 69, 23);
		contentPane.add(label_3);
		
		Label label_4 = new Label("邮箱:114932777@qq.com");
		label_4.setBounds(314, 426, 170, 23);
		contentPane.add(label_4);

		packageName = new TextField();
		packageName.setBounds(54, 22, 106, 23);
		contentPane.add(packageName);
		
		firstClassName = new TextField();
		firstClassName.setBounds(241, 22, 116, 23);
		contentPane.add(firstClassName);
		
		outSavePath = new TextField();
		outSavePath.setBounds(424, 22, 160, 23);
		contentPane.add(outSavePath);
	
		readConfig();
		Button button = new Button("生成");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("包名:"+packageName.getText().toString());
				System.out.println("最顶层类名:"+firstClassName.getText().toString());
				System.out.println("输出路径:"+outSavePath.getText().toString());
				if("".equals(packageName.getText().toString().trim())){
					temp=Statue.PACKAGENAME;
					new Dialog(temp).setVisible(true);
					return ;
				}
				if("".equals(firstClassName.getText().toString().trim())){
					temp=Statue.FIRSTCLASSNAME;
					new Dialog(temp).setVisible(true);
					return ;
				}
				savePath_Real=outSavePath.getText().toString().trim().replace("\\", "/");
				if("".equals(savePath_Real)){
					temp=Statue.OUTSAVEPATH;
					new Dialog(temp).setVisible(true);
					return ;
				}
				
				if("".equals(jsonStr.getText().toString().trim())){
					temp=Statue.JSON_EMPTY;
					new Dialog(temp).setVisible(true);
					return ;
				}
				
				
				JsonTest.generateFile(packageName.getText().toString(), firstClassName.getText().toString(), savePath_Real, jsonStr.getText().toString(),new JsonExceptionListener() {
					
					@Override
					public void errMsg(String msg) {
						temp=Statue.JSON_ERROR;
						new Dialog(temp).setVisible(true);
					}

					@Override
					public void successMsg() {
						ConfigEntity configEntity = new ConfigEntity(packageName.getText().toString().trim(), firstClassName.getText().toString().trim(), outSavePath.getText().toString().trim());
						String jsonConfig = gson.toJson(configEntity);
						IOUtils.write(CONFIG_FILE, jsonConfig, "gbk");	
						IOUtils.openFolder(savePath_Real);
					}
				});
				
			}
		});
		button.setBounds(275, 392, 76, 23);
		contentPane.add(button);
	}

	private void readConfig() {
		if (CONFIG_FILE.exists()) {
			String tempFile = IOUtils.read(CONFIG_FILE, "gbk");
			ConfigEntity tempConfigEntity = gson.fromJson(tempFile, ConfigEntity.class);
			 packageName.setText(tempConfigEntity.getPackageName());;
			 firstClassName.setText(tempConfigEntity.getFirstClassName());
			 outSavePath.setText(tempConfigEntity.getOutSavePath());
		}
	}
}
