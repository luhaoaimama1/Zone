package Java.Zone.Socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * �Ѿ����� OK �������� startAccept ����˵���� class�Ա�Service˵��
 * 
 * @author zone
 * 
 */
public abstract class  MySocket_Service {
	private ServerSocket server;
	private Socket client;

	public MySocket_Service(int port) throws IOException {
		// TODO Auto-generated constructor stub
		server = new ServerSocket(port);
	}

	/**
	 * ��ʼtcp�ķ���
	 * 
	 * @throws IOException
	 */
	public void startAccept() throws IOException {
		while (true) {
			client = server.accept();
			new Thread(new ClientThread(client)).start();
		}
	}

	/**
	 * �ͻ��˴���
	 * 
	 * @author Ozone
	 * 
	 */
	class ClientThread implements Runnable {
		private Socket client;
		private BufferedOutputStream br_out;
		private BufferedInputStream br_in;

		public ClientThread(Socket client) throws IOException {
			// TODO Auto-generated constructor stub
			this.client = client;
			br_out = new BufferedOutputStream(client.getOutputStream());
			br_in = new BufferedInputStream(client.getInputStream());
		}

		public void run() {
			// TODO Auto-generated method stub
				blockProcess(br_in,br_out);
				try {
					br_in.close();
					br_out.close();
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("����ͻ��˹ر�IOException������");
				}
		}

	}
	/**
	 * ������д ��д�߼�������
	 * @param br_in Client���ͻ��ˣ��ṩ����������� �Ѿ���װ���� BufferedInputStream
	 * @param br_out  service�����������ṩ���������� �Ѿ���װ���� BufferedOutputStream 
	 */ 
	public abstract void blockProcess(BufferedInputStream br_in, BufferedOutputStream br_ou) ;
}
