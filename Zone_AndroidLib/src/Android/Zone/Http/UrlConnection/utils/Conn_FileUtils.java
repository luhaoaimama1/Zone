package Android.Zone.Http.UrlConnection.utils;

import java.io.File;

public class Conn_FileUtils {
	/**
	 * ���ݺ�׺���ж��Ƿ���ͼƬ�ļ�
	 * 
	 * @param �ļ�
	 * @return �Ƿ���ͼƬ���true or false
	 */
	public static boolean isImage(File file) {
		String fileName = file.getName();
		int typeIndex = fileName.lastIndexOf(".");
		if (typeIndex != -1) {
			String fileType = fileName.substring(typeIndex + 1).toLowerCase();
			if (fileType != null
					&& (fileType.equals("jpg") || fileType.equals("gif")
							|| fileType.equals("png")
							|| fileType.equals("jpeg")
							|| fileType.equals("bmp")
							|| fileType.equals("wbmp")
							|| fileType.equals("ico") || fileType.equals("jpe"))) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * ��ȡ�ļ�����
	 * @param file  �ļ�
	 * @return   �ļ���׺��
	 */
	public static String getFileType(File file){
		String fileName = file.getName();
		int typeIndex = fileName.lastIndexOf(".");
		if(typeIndex != -1){
			return fileName.substring(typeIndex + 1).toLowerCase();
		}else{
			return "";
		}
	}
	/**
	 * ��ȡ�ļ����ϴ�����
	 * 
	 * @param file
	 * @return ͼƬ��ʽΪimage/png,image/jpg�ȡ���ͼƬΪapplication/octet-stream
	 */
	public static  String getContentType(File file) {
		if (Conn_FileUtils.isImage(file)) {
			return "image/" + Conn_FileUtils.getFileType(file).toLowerCase();// ��FormatName���ص�ֵת����Сд��Ĭ��Ϊ��дr
		} 
			return "application/octet-stream";
	}
}
