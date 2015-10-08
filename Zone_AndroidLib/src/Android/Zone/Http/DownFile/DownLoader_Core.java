package Android.Zone.Http.DownFile;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import Android.Zone.Http.DownFile.DownLoader.ProgressListener;
import Android.Zone.Http.DownFile.Entity.ProgressEntity;


public class DownLoader_Core implements Runnable{
	private int threadId;
	private int startIndex;
	private int endIndex;
	private String urlPath;
	private File saveOutFile;
	private ProgressEntity pe;
	private ProgressListener pl;
	private Handler handler;
	/**
	 * @param path
	 *            �����ļ��ڷ������ϵ�·��
	 * @param threadId
	 *            �߳�id
	 * @param startIndex
	 *            �߳����صĿ�ʼλ��
	 * @param endIndex
	 *            �߳����صĽ���λ��
	 * @param pl 
	 * @param pe 
	 * @param handler 
	 * @param targetFile 
	 */
	public DownLoader_Core(int threadId, int startIndex,
			int endIndex,String urlPath, File saveOutFile, ProgressEntity pe, ProgressListener pl, Handler handler) {
		this.threadId = threadId;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.urlPath = urlPath;
		this.saveOutFile = saveOutFile;
		this.pe=pe;
		this.pl=pl;
		this.handler=handler;
	}

	@Override
	public void run() {

		try {
			URL	url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setRequestMethod("GET");
			// ��Ҫ��������������ز��ֵ��ļ� ָ���ļ���λ��
			conn.setRequestProperty("Range", "bytes=" + startIndex + "-"
					+ endIndex);
			conn.setConnectTimeout(5000);
			// �ӷ���������ȫ����Դ��״̬��200 ok ����ӷ��������󲿷���Դ��״̬��206 ok
			int code = conn.getResponseCode();
			System.out.println("---code---" + code);
			InputStream is = conn.getInputStream();// �Ѿ������������λ�ã����ص��ǵ�ǰλ�ö�Ӧ���ļ���������
			RandomAccessFile raf = new RandomAccessFile(saveOutFile, "rwd");
			// ���д�ļ���ʱ����ĸ�λ�ÿ�ʼд
			raf.seek(startIndex);// ��λ�ļ�
			int len = 0;
			byte[] buffer = new byte[1024];
			int count=0;
			int overLength=0;
			while ((len = is.read(buffer)) != -1) {//!=-1����Ҳ�ô�
				count++;
				if(pl!=null){
					overLength+=len;
					System.out.println("threadId��"+threadId+"  overLength:"+overLength);
					pe.set_updateProgress(threadId, overLength, pl,handler);
				}
				raf.write(buffer, 0, len);
			}
			System.out.println("�߳�" + threadId + "   startIndex "+startIndex+"  endIndex   "+endIndex+"����������"+overLength);
			System.out.println("�߳�" + threadId + "���ô�����"+count);
			is.close();
			raf.close();
			pe.set_complete(threadId);
			System.out.println("�߳�" + threadId + ":��������ˣ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
