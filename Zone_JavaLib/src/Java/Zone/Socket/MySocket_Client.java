package Java.Zone.Socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Java.Zone.Setting.MyJava_Preferences;

/**
 * �Ѿ����� OK  ����˵���� class�Ա�Client˵��
 * @author zone
 * 
 */
public abstract class MySocket_Client {
	private static Socket server;
	/**
	 * 
	 * @param host  Ҫ���ӵķ����IP��ַ ���磺String host = "192.168.60.111";
	 * @param port  �˿ں�
	 */
	public MySocket_Client(String host, int port) {
		// TODO Auto-generated constructor stub
		MyJava_Preferences.op();
		try {
			server = new Socket(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("UnknownHostException������");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException!!!");
		}
		MyJava_Preferences.InnerOp();
		init();
		MyJava_Preferences.InnerEd();
		MyJava_Preferences.ed();
	}

	private void init() {
		MyJava_Preferences.op();
		BufferedInputStream br_in = null;
		BufferedOutputStream br_out = null;
		try {
			br_in = new BufferedInputStream(server.getInputStream());
			br_out =new BufferedOutputStream(server.getOutputStream()) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("server��ȡ������IOException������");
		}
		MyJava_Preferences.InnerOp();
		sendOrRead(br_in, br_out);
		MyJava_Preferences.InnerEd();
		try {
			br_in.close();
			br_out.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("����ͻ��˹رշ���IOException������");
		}
		MyJava_Preferences.ed();

	}
	/**
	 * ������д ��д�߼�������
	 * @param br_in  service�����������ṩ����������� �Ѿ���װ���� BufferedInputStream
	 * @param br_out  service�����������ṩ���������� �Ѿ���װ���� BufferedOutputStream 
	 */
	public abstract void sendOrRead(BufferedInputStream br_in, BufferedOutputStream br_out);

}
