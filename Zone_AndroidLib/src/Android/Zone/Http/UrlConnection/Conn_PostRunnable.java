package Android.Zone.Http.UrlConnection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Map;
import java.util.Map.Entry;

import Android.Zone.Http.UrlConnection.Conn_Core.HttpType;


public  class Conn_PostRunnable implements Conn_Interface{
	private String urlString;
	private String postParams;
	private String encoding;
	/**
	 * 
	 * @param urlString
	 * @param map
	 */
	public Conn_PostRunnable(String urlString,Map<String,String> map) {
		if(urlString==null) throw new IllegalArgumentException("urlString may be null");
		this.urlString=urlString;
		String get="";
		if(map!=null){
			for (Entry<String, String> entry : map.entrySet()) {
				System.out.println("key:"+entry.getKey()+"\t value:"+entry.getValue());
				get+=entry.getKey()+"="+entry.getValue()+"&";
			}
			postParams=get.substring(0, get.length()-1);
		}
		
	}

	@Override
	public HttpType getHttpType() {
		return HttpType.POST;
	}

	@Override
	public String getHttpUrl() {
		return urlString;
	}

	@Override
	public void connInit(HttpURLConnection conn) {
		try {
			//��������ģʽ��
			conn.setRequestMethod("POST");
			//��˼��������urlencoded�������form����
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset="+encoding);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeConn(HttpURLConnection conn) {
		try {
			if (postParams!=null) {
				//�õ�post������byte
				byte[] data_bytes = postParams.getBytes(encoding);
				//��ȡ���������ʱ��������������s
				conn.getOutputStream().write(data_bytes);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding=encoding;
	}


}
