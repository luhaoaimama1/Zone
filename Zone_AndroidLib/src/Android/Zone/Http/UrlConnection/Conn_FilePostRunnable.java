package Android.Zone.Http.UrlConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import Android.Zone.Http.UrlConnection.Conn_Core.HttpType;
import Android.Zone.Http.UrlConnection.utils.Conn_FileUtils;

public abstract class Conn_FilePostRunnable implements Conn_Interface{
	private static final String PREFIX = "--", LINEND = "\r\n";
	private final static String BOUNDARY = "---------------------------123821742118716"; //boundary����requestͷ���ϴ��ļ����ݵķָ���    
	private String urlString;
	private String encoding;
	private Map<String, String> map_str=null;
	private Map<String, File> map_file=null;
	
	public Conn_FilePostRunnable(String urlString,Map<String,Object> map) {
		if(urlString==null) throw new IllegalArgumentException("urlString may be null");
		this.urlString=urlString;
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
	private void writeFile(OutputStream out) {
		 try {
			if (map_file != null) {  
				long total =0;
				for (Entry<String, File> item : map_file.entrySet()) {
					 total += item.getValue().length();//��ô���ֽ�
				}
				 long current=0;
			 	for (Entry<String, File> item : map_file.entrySet()) {
			 		StringBuilder sb = new StringBuilder();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""+item.getKey()+"\"; filename=\""
							+item.getValue().getName()+ "\"" + LINEND);
					sb.append("Content-Type: "+Conn_FileUtils.getContentType(item.getValue())+"; charset="
							+ encoding + LINEND);
					sb.append(LINEND);
				    out.write(sb.toString().getBytes(encoding));  
			        DataInputStream in = new DataInputStream(new FileInputStream(item.getValue()));  
			         int bytes = 0;  
			         byte[] bufferOut = new byte[1024];  
			         onStart();
			         onLoading(total, current);
			         while ((bytes = in.read(bufferOut)) != -1) {  
			        	 current+=bytes;
			        	 onLoading(total, current);
			             out.write(bufferOut, 0, bytes);  
			         }  
			         in.close();  
			         out.write(LINEND.getBytes(encoding));
					}
			 }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

	private void writeString(OutputStream out) {
		try {
			if (map_str != null) {
				StringBuffer sb = new StringBuffer();
				for (Entry<String, String> item : map_str.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""
							+ item.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + encoding
							+ LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(URLEncoder.encode(item.getValue(), encoding));
					sb.append(LINEND);
				}
				out.write(sb.toString().getBytes(encoding));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}


	@Override
	public HttpType getHttpType() {
		return HttpType.POST_FILE;
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
			conn.setRequestProperty("Connection", "Keep-Alive");  
			//TODO ���ò��Ҳ���Բ�д ��Ϊ���ò���������������
//    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}		
	}


	@Override
	public void writeConn(HttpURLConnection conn) {
		OutputStream out=null ;
        try {
			/**  =====================д ����==========================*/
			 out = new DataOutputStream(conn.getOutputStream());  
			// text    
			writeString(out);
			// file    
			writeFile(out);
			out.write((PREFIX + BOUNDARY + PREFIX + LINEND).getBytes());//// ���������־   
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
				try {
					out.flush();  
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	@Override
	public void setEncoding(String encoding) {
		this.encoding=encoding;
	}

	 /**
     * �ϴ��Ĵ���������д
     */
    public abstract void onLoading(Long total,Long current);  
    /**
     * Ҫ��ʼ�ϴ���
     */
    public abstract void onStart(); 
  
}
