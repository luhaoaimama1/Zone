package Json;
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


@SuppressWarnings("serial")
public class GuiJson extends JFrame {
	public enum Statue{
		PACKAGENAME,FIRSTCLASSNAME,OUTSAVEPATH,JSON_EMPTY,JSON_ERROR,OK
	}

	private JPanel contentPane;
	private TextField packageName;
	private TextField firstClassName;
	private TextArea jsonStr;
	private Statue temp;
	private  File CONFIG_FILE=null;
	private  Gson gson=new Gson();
	private File fileFolder_Save;

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
		label.setBounds(28, 22, 33, 23);
		contentPane.add(label);
		
		Label label_1 = new Label("最顶层类名:");
		label_1.setBounds(293, 22, 69, 23);
		contentPane.add(label_1);
		
		Label label_3 = new Label("作者: Zone ");
		label_3.setBounds(218, 426, 69, 23);
		contentPane.add(label_3);
		
		Label label_4 = new Label("邮箱:114932777@qq.com");
		label_4.setBounds(314, 426, 170, 23);
		contentPane.add(label_4);

		packageName = new TextField();
		packageName.setBounds(67, 22, 195, 23);
		contentPane.add(packageName);
		
		firstClassName = new TextField();
		firstClassName.setBounds(368, 22, 222, 23);
		contentPane.add(firstClassName);
	
		fileFolder_Save = new File("savePath");
		if(!fileFolder_Save.exists()){
			fileFolder_Save.mkdir();
		}
		CONFIG_FILE=new File("Config.txt");
		System.out.println("包名:"+packageName.getText().toString());
		System.out.println("最顶层类名:"+firstClassName.getText().toString());
		
		readConfig();
		Button button = new Button("生成");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//每次生成的时候把  保存文件夹内部的 文件清空了
				File[] files = fileFolder_Save.listFiles();
				for (File file : files) {
					if(file.exists()){
						file.delete();
					}
				}
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
				if("".equals(jsonStr.getText().toString().trim())){
					temp=Statue.JSON_EMPTY;
					new Dialog(temp).setVisible(true);
					return ;
				}
				final String temp_savePath = fileFolder_Save.getAbsolutePath();
				JsonTest.generateFile(packageName.getText().toString(), firstClassName.getText().toString(), fileFolder_Save.getAbsolutePath(), jsonStr.getText().toString(),new JsonExceptionListener() {
					@Override
					public void errMsg(String msg) {
						temp=Statue.JSON_ERROR;
						new Dialog(temp).setVisible(true);
					}

					@Override
					public void successMsg() {
						ConfigEntity configEntity = new ConfigEntity(packageName.getText().toString().trim(), firstClassName.getText().toString().trim());
						String jsonConfig = gson.toJson(configEntity);
						IOUtils.write(CONFIG_FILE, jsonConfig, "gbk");	
						IOUtils.openFolder(temp_savePath);
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
		}
	}
}
