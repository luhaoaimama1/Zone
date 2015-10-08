package Android.Zone.Http.UrlConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Java.Zone.IO.IOUtils;


public abstract class Conn_Core implements Runnable{
	private Conn_Interface myConnection=null;
	private HttpURLConnection conn=null;
	/** =======================set get================================ */
	public static String encoding;
	public static  int timeoutMillis=6*1000;
	public static  boolean isNotPrintHeaders=false;
	
	public enum HttpType{
		GET,POST,POST_FILE
	}
	@SuppressWarnings("static-access")
	public Conn_Core(String encoding,Conn_Interface myConnection){
		this.encoding=encoding;
		this.myConnection=myConnection;
		myConnection.setEncoding(encoding);
		
	}
	@Override
	public void run() {
		conn=initConnection(myConnection.getHttpUrl(), conn, myConnection.getHttpType());
		myConnection.connInit(conn);
		myConnection.writeConn(conn);
		readConn();
	}
	
	/**
     * ���ʳɹ���Ĵ���������д
     * @param msg  ����response��������String
     */
    public abstract void onSuccess(String msg);
    /**
     * ����ʧ�ܵĴ���������д
     * @param msg  ����response��������String
     */
    public abstract void onFailure(String msg); 
   
	
	@SuppressWarnings("finally")
	private  HttpURLConnection initConnection(String urlString,HttpURLConnection conn,HttpType httpType){
			try {
				//����һ��URL����
				URL url = new URL(urlString);
				//����HttpURLConnection����������л�ȡ��ҳ����
				conn = (HttpURLConnection) url.openConnection();
				//�������ӳ�ʱ
				conn.setConnectTimeout(timeoutMillis);
				//����������������������
				conn.setDoInput(true);
				//������������������ϴ�   
				conn.setDoOutput(true);
				//��ʹ�û���
				conn.setUseCaches(false);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				return conn;
			}
	}
	private void readConn() {
		try {
			//			�ж��Ƿ����ӳɹ�
			if (conn.getResponseCode() == 200){
				if (isNotPrintHeaders) {
					printHeaders(conn);
				}
				/**  =====================��ȡ ����==========================*/
				InputStream input = conn.getInputStream();
				String s=IOUtils.read(input, encoding);
				onSuccess(s);
			}else{
				onFailure(" 200 is ok,but response code is"+conn.getResponseCode()+"!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
		}
	}
	private  void printHeaders(HttpURLConnection conn){
		Map<String, List<String>>  map=conn.getHeaderFields();
		StringBuilder sb=new StringBuilder();
		sb.append("==================����ͷ��Ϣ �������Ŀ�ʼ=======================\n");
		for (Entry<String, List<String>> iterable_element : map.entrySet()) {
			List<String> lsit = iterable_element.getValue();
			sb.append(iterable_element.getKey()+":{");
			for (String string : lsit) {
				sb.append(string);
			}
			sb.append("} \n");
		}
		sb.append("==================����ͷ��Ϣ �������Ľ���=======================\n");
		System.out.println(sb.toString());
	}
	
}
