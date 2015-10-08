package Android.Zone.Http.Client;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import Java.Zone.Setting.MyJava_Preferences;
import android.util.Log;

	/**
	 *  multipart/form-data; <br>
	 * ���ļ��ı��ύ��ʽ
	 * <br>���������:File��String   
	 * 
	 * @author Zone
	 *
	 */
public abstract class MyHttpFilePostThread extends Thread {
		private String url;
		Map<String, String> map_str=null;
		Map<String, File> map_file=null;
		private String encoding;
		
		private boolean writeLog=true;
		private void log(String str){
			if(writeLog){
				System.out.println(str);
			}
		}
		/**
		 *  multipart/form-data; <br>
		 * ���ļ��ı��ύ��ʽ
		 * <br>���������:File��String  
		 * <br>�Դ��ļ���֤ �Ƿ���ڣ� �����ڲ�����ӵ��ǻ�log���err��Ϣ 
		 * @param url ��������ַ
		 * @param map ��� key ������ ��1.String_���������� <br>2.File_����������
		 * <br>3.String �� File ���Բ��ִ�Сд
		 * <br>��ʽΪ��Map<String, Object> �� Object!
		 */
		public MyHttpFilePostThread(String url,Map<String, Object> map,String encoding) {
			super();
			this.url = url;
			this.encoding = encoding;
			//��������map��ʼ��
			map_str=new HashMap<String, String>();
			map_file=new HashMap<String, File>();
			if(map!=null)
			{//���map��Ϊ��
				//��map �����ݷֱ�װ����������map��
				for ( Entry<String, Object> item : map.entrySet()) {
					//��keyֵȡ����  ����file����String
					String[] splits=item.getKey().split("[_]");
					if("string".equals(splits[0].toLowerCase())){
						map_str.put(splits[1], (String)item.getValue());
					}
					if("file".equals(splits[0].toLowerCase())){
						map_file.put(splits[1], (File)item.getValue());
					}
					
				}
			}
			
		}

		@Override
		public void run() {
			/* �ϴ��ļ���Server��uploadUrl�������ļ��Ĵ���ҳ�� */
			try {
				HttpParams httpParams = new BasicHttpParams();
				// ���ó�ʱ
				HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
				HttpClient httpclient = new DefaultHttpClient(httpParams);
				HttpPost httppost = new HttpPost(url);
				//��ֹ����  �ļ�����  ���Ƿ������� �жϵ��ǲ��Ǹ��ϱ� 
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,  null, Charset.forName(encoding));
				if(map_str!=null&&map_file!=null)
				{//������map����Ϊnull 
					
					//����װString��map
					for ( Entry<String, String> item : map_str.entrySet()) {
						entity.addPart(item.getKey(), new StringBody(item.getValue()));
						log("String װ��ģ�"+"key:"+item.getKey()+"\t value:"+item.getValue());	
					}
					//����װFile��map
					for ( Entry<String, File> item : map_file.entrySet()) {
						if(item.getValue().exists()){
							//��֤��û������ļ� ��������
							entity.addPart(item.getKey(), new FileBody(item.getValue()));
							log("File װ��ģ�"+"key:"+item.getKey()+"\t  File����:OK! \tvalue��·��:"+item.getValue().getPath());
						}else{
							throw new IllegalStateException(item.getValue().getPath()+"·���µ��ļ�  �����ڰ������� ���ļ�δ װ�����   ");
						}
					}
				}
				
				Log.e("params",httppost.getURI().toString()+ entity.getContentLength());
				//��entity װ� HttpPost
				httppost.setEntity(entity);
				Log.e("params222", httppost.getEntity().getContentLength() + "");
				//�ύ��
				HttpResponse response = httpclient.execute(httppost);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					log("---����---url---------------success---------------------------------------");
					//response.getEntity() ΪnullҲ����
					 String responseStr=EntityUtils.toString(response.getEntity(),encoding);
					// mHandler.obtainMessage(0, zifu).sendToTarget();
					//��ӡ������ֵ
//					System.out.println(sysoOp+"������ֵ:" + responseStr);
					success(response,responseStr);
				} else {
					log("---����---url---------------failed---------------------------------------");
				}
				  // �ͷ���Դ  
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			} 

		}
		/**
		 * ���ʳɹ���Ĵ���������д
		 * @param response  ����response
		 * @param responseStr  ����response��������String
		 */
		public abstract void success(HttpResponse response,String responseStr);
	}
