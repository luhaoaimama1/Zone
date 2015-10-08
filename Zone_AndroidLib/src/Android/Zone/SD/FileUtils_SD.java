package Android.Zone.SD;

import java.io.File;

import Java.Zone.CustomException.OperationFailException;
import android.os.Environment;
/**
 * �����Ǵ����ļ��е�����
 * @version 2015.7.15
 * @author Zone
 *
 */
public class FileUtils_SD {
	/**
	 * ����SD����  ����ļ��Ľ���
	 * @param arg  �����ļ���·�� 
	 * <br><strong> ������FolderCreateOrGet("test001","test002","test003"); �ļ���Ŀ¼
	 * <br> ��������Ϊ��FolderCreateOrGet("") ��ʾSD����Ŀ¼  
	 * <br> ��������Ϊ��FolderCreateOrGet("","a.txt")   �ļ�
	 * </strong>
	 * @return	
	 */
	public static File FolderCreateOrGet(String... arg) {
		return FolderCreateOrGet(true,arg);
	}
	/**
	 * ����SD����  ����ļ��Ľ���
	 * @param isNotCreate  ���ļ������ڵ�ʱ���Ƿ񴴽�
	 * @param arg  �����ļ���·�� 
	 * <br><strong> ������FolderCreateOrGet(true,"test001","test002","test003");
	 * <br> ��������Ϊ��FolderCreateOrGet("") ��ʾSD����Ŀ¼
	 * <br> ��������Ϊ��FolderCreateOrGet("","a.txt") Ҳ����  �������Ĭ�ϵ��Ǵ��� a.txt�ĸ��ļ���  ��ʹ isNotCreate false��Ҳ���Զ��޸ĳ�true
	 * </strong>
	 * @return	
	 */
	private static File FolderCreateOrGet(boolean isNotCreate,String... arg) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
			throw new NullPointerException("sd�����ڷ�false!");
		}
		String pathJoin = "";
		String fileEnd=null;
		for (String str : arg) {
			if(str.contains(".")){
				fileEnd= str;
			}else{
				pathJoin += "/" + str;
			}
			
		}
		String f = Environment.getExternalStorageDirectory().getPath();
		File file = new File(f + pathJoin);
		if (fileEnd!=null) 
			isNotCreate=true;
		if (isNotCreate&&!file.exists()) {
			boolean isOk = file.mkdirs();
			if (!isOk) {
				throw new OperationFailException("�ļ�����ʧ�ܣ�");
			}
		}
		if (fileEnd!=null) {
			file = new File(file, fileEnd);
		}
		return file;
	}
	
}
