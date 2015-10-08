package Android.Zone.Http.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import Java.Zone.Setting.MyJava_Preferences;

/**
 * application/x-www-form-urlencoded
 * <br>��ͨ�ı��ύ
 * @author Zone
 *
 */
public abstract class MyHttpPostThread extends Thread {
	private DefaultHttpClient httpclient;
	private HttpPost request;
	private List<BasicNameValuePair> params;
	private String encoding;

	
	private boolean writeLog=true;
	private void log(String str){
		if(writeLog){
			System.out.println(str);
		}
	}
	/**
	 * application/x-www-form-urlencoded
	 * <br>��ͨ�ı��ύ
	 * @param urlString		url��ַ
	 * @param map			��ֵ��
	 * @param encoding		��ֵ�Եı��뷽ʽ
	 */
	public MyHttpPostThread(String urlString, Map<String, String> map, String encoding) {
		this.encoding = encoding;
		httpclient = new DefaultHttpClient();
		request = new HttpPost(urlString);
		params = new ArrayList<BasicNameValuePair>();
		for (Entry<String, String> entry : map.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

	}

	@Override
	public void run() {
		try {
			HttpEntity httpentity = new UrlEncodedFormEntity(params, encoding);
			request.setEntity(httpentity);
			HttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				log("---����---url---------------success---------------------------------------");
				//�õ���Ӧ���ص��ַ���
				String responseStr=EntityUtils.toString(response.getEntity(),encoding);
				// mHandler.obtainMessage(0, zifu).sendToTarget();
				//��ӡ������ֵ
				log("������ֵ:" + responseStr);
				success(response,responseStr);
			} else {
				log("---����---url---------------failed---------------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.run();
	}
	/**
	 * ���ʳɹ���Ĵ���������д
	 * @param response  ����response
	 * @param responseStr  ����response��������String
	 */
	public abstract void success(HttpResponse response,String responseStr);
}
