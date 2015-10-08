package Android.Zone.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import Java.Zone.Setting.MyJava_Preferences;
import android.content.Context;
import android.widget.Toast;

public class RunProcess {
	/**
	 * (Android��)
	 * <br>
	 * ��Ҫ�����߳�������������
	 * @param PingBoolean  ����ʲôʱ��ֹͣ ѭ���Ŀ��� �ı伴�ر�Ping
	 * @param IPAddress ping��IP��ַ��www.baidu.com Ҳ����192.168.60.112��
	 * @param context	Toast��Ҫ��Context
	 */
	public static void Ping(boolean PingBoolean, String IPAddress,Context context) {
		MyJava_Preferences.op();
		BufferedReader br = null;
		try {
			System.out.println("Ping ��ֵΪ : ping -c 100" + IPAddress);
			Process p = Runtime.getRuntime().exec("ping -c 100   " + IPAddress);

			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while (PingBoolean) {
				String s = br.readLine();
				if (s == null) {
					System.out.println("s == null  ��wifiδ��");
					toToast(context, "wifiδ����");
					break;
				}
				if (s != null && s.contains("Host Unreachable")) {
					toToast(context, "δ��ͨ��");
				}
				if (s != null && s.contains("64 bytes")) {
					toToast(context, "�Ѿ���ͨ!");
					break;
				}
				System.out.println("Ping�Ľ����" + s);
			}
		} catch (Exception e) {
			System.out.println("�����쳣��������" + e.getMessage());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.out.println("br���ر��쳣");
			}
			MyJava_Preferences.ed();
		}

	}

	/**
	 * Java�� <br> �����пո� �κγ��򶼿��Դ�<br>
	 * ���ӣ� 	File f=new File("C:/Users/a/Desktop/a b.txt"); <br>
	 * openExe(f.getCanonicalPath());
	 * 
	 * @param string
	 *            �ļ��ľ���·��
	 */
	public static void openExe(String string) {
		// TODO Auto-generated method stub
		try {
			Runtime.getRuntime().exec("cmd /c \""+string+"\"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�򿪳�����IOException������");
		}
	}
	/**
	 * Java�� <br>�����пո� �κγ��򶼿��Դ�<br>
	 * ���ӣ�	File f=new File("C:/Users/a/Desktop/a b.txt"); <br>
	 * openExe(f);
	 * @param file  �ļ�����
	 */
	public static void openExe(File file) {
		// TODO Auto-generated method stub
		try {
			Runtime.getRuntime().exec("cmd /c \""+file.getCanonicalPath()+"\"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�򿪳�����IOException������");
		}
	}

	private static void toToast(Context context, String string) {
		Toast.makeText(context, string, 1).show();
	}
}
