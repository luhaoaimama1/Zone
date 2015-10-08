package Android.Zone.Http.DownFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Android.Zone.Http.DownFile.Entity.ProgressEntity;
import Android.Zone.Http.DownFile.Entity.ProgressEntity.RangeStaue;
import Android.Zone.Http.DownFile.Entity.ThreadTable;
import Android.Zone.Http.DownFile.Entity.UrlPath;
import Android.Zone.Sqlite.Sqlite_Utils;
import Android.Zone.Sqlite.Sqlite_Utils.OnCreate;
import Android.Zone.Sqlite.Sqlite_Utils.OnUpgrade;
import android.content.Context;
import android.os.Handler;

/**
 * 	String urlPath="http://down.360safe.com/360/inst.exe";
 *  String urlPath="http://img4.freemerce.com/ci49h5p.jpg";
 *	��������ַ�����
 * @author Administrator
 *
 */
//TODO �ϵ�����       ��ʱ�Ȳ�����setget
public enum DownLoader  {
	INSTANCE;
	private static final int CONTAIN_THREAD_COUNT=5;
	private ExecutorService executorService=Executors.newFixedThreadPool(CONTAIN_THREAD_COUNT);
	
	public interface ProgressListener{
		 public void onProgressUpdate( int current,int total,float progress);
	}
	private  Handler handler=new Handler();
	
	
	public void  downLoader(final String urlPath,File targetFolder,int threadCount){
		downLoader(urlPath, targetFolder, threadCount, null);
	}
	public void  downLoader(final String urlPath,final File targetFolder,final int tc,final ProgressListener pl){
			for (ProgressEntity item : ProgressEntity.peList) {
				if((urlPath).equals(item.url)){
					System.out.println("�˵�ַ�Ѿ�����  ");
					return ;
				}
			}
			executorService.execute(new Runnable() {
				
				@Override
				public void run() {
					ProgressEntity pe =  new ProgressEntity(urlPath);
					try {
						// ���ӷ���������ȡһ���ļ�����ȡ�ļ��ĳ��ȣ��ڱ��ش���һ����С���������ļ���Сһ������ʱ�ļ�
						URL url = new URL(urlPath);
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						// ������������ʱʱ��
						conn.setConnectTimeout(5000);
						// ��������ʽ
						conn.setRequestMethod("GET");//get����Сд �����д
						int code = conn.getResponseCode();
						int length = 0;
						String fileName = "";
						String rangeStr = null;
						if (code == 200) {
							// ���������ص����ݵĳ��ȣ�ʵ�ʾ����ļ��ĳ���
							length = conn.getContentLength();
							rangeStr = conn.getHeaderField("Accept-Ranges");
							if (rangeStr == null) {
								rangeStr = "0";
							} else {
								rangeStr = "1";
							}

							System.out.println("rangeStr   " + rangeStr);
							//						for (Entry<String, List<String>> item : conn.getHeaderFields().entrySet()) {
							//							System.out.print("key: "+item.getKey());
							//							System.out.print("  value:");
							//							for (String str : item.getValue()) {
							//								System.out.print(str+"  ");
							//							}
							//							System.out.println();
							//						}
							System.out.println("��������" + conn.toString());
							System.out.println("----�ļ��ܳ���----" + length);
							String name = url.getFile();
							String[] lin = name.split("[/]");
							for (int i = lin.length - 1; i >= 0; i--) {
								if (lin[i].contains(".")) {
									fileName = lin[i];
									break;
								}

							}
						}
						int supportStatue = Integer.parseInt(rangeStr);
						File saveOutFile = new File(targetFolder, fileName);
						try {
							RandomAccessFile raf = new RandomAccessFile(
									saveOutFile, "rwd");
							// ָ������������ļ��ĳ���
							raf.setLength(length);
							System.out.println("byte�ļ����ȣ�" + length);
							// �ر�raf
							raf.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					
						int threadCount=tc;
						switch (supportStatue) {
						case 0:
							pe.init(length,threadCount, RangeStaue.UNSUPPORTED);
							threadCount = 1;
							break;
						case 1:
							pe.init(length,threadCount, RangeStaue.SUPPORTED);
							break;

						default:
							break;
						}
						int blockSize = (int) Math.ceil(length / threadCount);
						for (int threadId = 1; threadId <= threadCount; threadId++) {
							// ����ÿ���߳����صĿ�ʼλ�úͽ���λ��
							int startIndex = (threadId - 1) * blockSize;
							int endIndex = threadId * blockSize - 1;
							if (threadId == threadCount) {
								endIndex = length;
							}
							System.out.println("----threadId---" + threadId
									+ "--startIndex--" + startIndex
									+ "--endIndex--" + endIndex);
							executorService.execute(new DownLoader_Core(
									threadId, startIndex, endIndex, urlPath,
									saveOutFile, pe, pl,handler));
						}
						//��ֹ�������ύ
//						executorService.shutdown();
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				
					
				}
			});
			
			}
}
