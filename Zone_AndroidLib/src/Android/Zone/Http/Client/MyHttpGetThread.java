package Android.Zone.Http.Client;


import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
/**
 * get�ύ
 * @author Zone
 *
 */
public abstract class MyHttpGetThread extends Thread {
	private DefaultHttpClient httpClinet;
	private HttpGet httpGet ;
	private String encoding;
	
	private boolean writeLog=true;
	private void log(String str){
		if(writeLog){
			System.out.println(str);
		}
	}	
	/**
	 * 
	 * @param urlString �Ѿ�ƴ�Ӻõ�
	 */
	public MyHttpGetThread(String urlString,String encoding) {
		// TODO Auto-generated constructor stub
		this.encoding=encoding;
		httpClinet = new DefaultHttpClient();
		httpGet = new HttpGet(urlString);
		
	}
	/**
	 * 
	 * @param urlString	 δƴ�ӵĵ�ַ
	 * @param map		map����
	 */
	public MyHttpGetThread(String urlString,Map<String,String> map,String encoding) {
		this.encoding=encoding;
		httpClinet = new DefaultHttpClient();
		String get="";
		for (Entry<String, String> entry : map.entrySet()) {
//			System.out.println("key:"+entry.getKey()+"\t value:"+entry.getValue());
			get+=entry.getKey()+"="+entry.getValue()+"&";
		}
		urlString+="?"+get;
//		System.out.println("url="+urlString.substring(0, urlString.length()-1));
		httpGet = new HttpGet(urlString.substring(0, urlString.length()-1));
	}

	@Override
	public void run() {
		try {
			HttpResponse response = httpClinet.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				log("---����---url---------------success---------------------------------------");
				//�õ���Ӧ���ص��ַ���
				String responseStr = EntityUtils.toString(response.getEntity(),encoding);
//				mHandler.obtainMessage(0, zifu).sendToTarget();
				//��ӡ������ֵ
//				System.out.println(sysoOp+"������ֵ:" + responseStr);
				onSuccess(response,responseStr);
			}else {
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
	public abstract void onSuccess(HttpResponse response,String responseStr);
}
