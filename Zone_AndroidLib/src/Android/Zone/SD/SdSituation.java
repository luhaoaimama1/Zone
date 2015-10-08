package Android.Zone.SD;

import java.io.File;

import Android.Zone.Setting.MyAndroid_Preferences;
import Java.Zone.Setting.MyJava_Preferences;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

public class SdSituation {
	
	
	/**
	 * �ж�SDCard�Ƿ����
	 * 
	 * @return
	 */
	public static boolean isSDCardEnable()
	{
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

	}

	/**
	 * ��ȡSD��·��
	 * 
	 * @return
	 */
	public static String getSDCardPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	/**
	 * ��ȡSD����ʣ������ ��λbyte
	 * 
	 * @return
	 */
	public static long getSDCardAllSize()
	{
		if (isSDCardEnable())
		{
			StatFs stat = new StatFs(getSDCardPath());
			// ��ȡ���е����ݿ������
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
			// ��ȡ�������ݿ�Ĵ�С��byte��
			long freeBlocks = stat.getAvailableBlocks();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}

	/**
	 * ��ȡָ��·�����ڿռ��ʣ����������ֽ�������λbyte
	 * 
	 * @param filePath
	 * @return �����ֽ� SDCard���ÿռ䣬�ڲ��洢���ÿռ�
	 */
	public static long getFreeBytes(String filePath)
	{
		// �����sd�����µ�·�������ȡsd����������
		if (filePath.startsWith(getSDCardPath()))
		{
			filePath = getSDCardPath();
		} else
		{// ������ڲ��洢��·�������ȡ�ڴ�洢�Ŀ�������
			filePath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs stat = new StatFs(filePath);
		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
		return stat.getBlockSize() * availableBlocks;
	}

	/**
	 * ��ȡϵͳ�洢·��
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath()
	{
		return Environment.getRootDirectory().getAbsolutePath();
	}
	

	/**
	 * SdSituation.IsSDspaceEnough(MainActivity.this, null);���ӡ ʣ���ڴ� <br>
	 * SdSituation.IsSDspaceEnough(MainActivity.this, "1000mb"); ���ӡ
	 * ʣ���ڴ桢����˿ռ�֮���ʣ���ڴ�
	 * 
	 * @param context
	 *            ������
	 * @param need
	 *            ����Ϊnull����ӡ ʣ���ڴ棩 <br>
	 *            ��Ϊnull��ʱ�� �����ִ�Сд kb KB���� ��������KB,MB,GB֮һ�� ���ӡ ʣ���ڴ桢����˿ռ�֮���ʣ���ڴ�
	 *            ��
	 * @return �Ƿ���
	 */
	public static boolean IsSDspaceEnough(Context context, String need) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {// ��sd��
			File sd = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
			StatFs size = new StatFs(sd.getPath());// �õ�����SD������Ϣ
			Long kuai = (long) size.getBlockSize();// ����Ϣ�еõ�ÿһ�� �Ĵ�С
			long sum = (long) size.getBlockCount();// һ�����ٿ�
			long left = (long) size.getAvailableBlocks();// ��ʣ�¶��ٿ�
			// Formatter.formatFileSize(context, left*kuai) ��byte ת���� GB,KBʲô��
			String leftStr = Formatter.formatFileSize(context, left * kuai);
			MyAndroid_Preferences.MyLog( "ʣ��ռ䣺" + leftStr);
			if (need == null) {
				return true;
			}
			// float b=(left*kuai)/(float)(1024*1024*1024);
			// Myshare.MyLog(SdSituation.class,"ʣ��ռ䣺"+String.valueOf(b));
			// �ɱ�ʾΪ1KB 0 ,1MB 1,��1GB 2
			int sign = -1;
			String[] danwei = new String[] { "KB", "MB", "GB" };
			String str = need.substring(need.length() - 2, need.length());
			for (int i = 0; i < danwei.length; i++) {
				if (danwei[i].equals(str.toUpperCase())) {
					sign = i;
				}
			}
			long needlong = Long.parseLong(need.substring(0, need.length() - 2)
					.trim());
			long needBig = 0;
			switch (sign) {
			case 0:
				needBig = needlong * 1024;
				break;
			case 1:
				needBig = needlong * 1024 * 1024;
				break;
			case 2:
				needBig = needlong * 1024 * 1024 * 1024;
				break;
			default:
				MyAndroid_Preferences.MyLog( "���� KB,MB,GB ֮һ");
				return false;
			}
			if (needBig < (left * kuai)) {
				long lin = left * kuai - needBig;
				MyAndroid_Preferences.MyLog("����������Ҫ���ڴ滹ʣ��" + Formatter.formatFileSize(context, lin));
				return true;
			} else {
				MyAndroid_Preferences.MyLog( "�ڴ治��");
				return false;
			}

		} else {
			MyAndroid_Preferences.MyLog( "SD�������ڵ�״������");
			return false;
		}
	}

}
