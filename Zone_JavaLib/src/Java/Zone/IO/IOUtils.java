package Java.Zone.IO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import Java.Zone.CustomException.OperationFailException;
import Java.Zone.Setting.MyJava_Preferences;
/**
 * @version 2015.7.15
 * @author Zone
 *
 */
public class IOUtils {
	// -----------------------------------------------------------InputStream---------------------------------------------------------------
	/**
	 * 
	 * @param in
	 *            ԭʼ������
	 * @param encoded
	 *            �����㶮�� һ�� GBK UTF-8 ISO8859-1
	 * @return �ַ���
	 */
	public static String read(InputStream in, String encoded) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in, encoded));
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("encoded_Unsupported");
		}
		String line = null;
		StringBuffer str = new StringBuffer("");
		try {
			while ((line = br.readLine()) != null) {
				str.append(line+"\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				throw new OperationFailException("���رշ����쳣��");
			}
		}
		return str.toString();
	}

	/**
	 * 
	 * @param file
	 *            file�㶮��
	 * @param encoded
	 *            �����㶮�� һ�� GBK UTF-8 ISO8859-1
	 * @return �ַ���
	 */
	public static String read(File file, String encoded) {
		String str = null;
		try {
			//������д
			str = read(new FileInputStream(file), encoded);
		} catch (FileNotFoundException e) {
			throw new NullPointerException("FileNotFound��");
		}
		return str;
	}

	/**
	 * 
	 * @param in
	 *            ������
	 * 
	 * @return byte�����16�����ַ���
	 */
	public static String readToHexString(InputStream in,boolean inIsClose) {
		BufferedInputStream br = new BufferedInputStream(in);
		byte[] b = new byte[2];
		StringBuffer sb = new StringBuffer();
		try {
			while (br.read(b, 0, 1) != -1) {
				// һ�ζ�һ���ֽ�
				int lin = b[0] & 0xff;
				sb.append(Integer.toHexString(lin));
			}
		} catch (IOException e) {
			throw new OperationFailException("����ȡ����IOException������");
		}
		if (inIsClose) {
			try {
				in.close();
			} catch (IOException e) {
				throw new OperationFailException("���رշ����쳣��");
			}
		}
		return sb.toString();
	}

	// -----------------------------------------------------------OutStream---------------------------------------------------------------
	/**
	 * 
	 * @param file
	 *            ��ŵ��ļ�
	 * @param in
	 *            ������
	 * @return �ɹ�ʧ��
	 */
	public static boolean write(File outFile, InputStream in) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);
		} catch (FileNotFoundException e1) {
			throw new NullPointerException("FileNotFound��");
		}
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = in.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			throw new OperationFailException("����ȡ����IOException������");
		} finally {
			try {
				in.close();
				fos.close();
			} catch (IOException e) {
				throw new OperationFailException("���رշ����쳣��");
			}
		}
		return true;
	}
	/**
	 * 
	 * @param outFile   �µ��ļ�
	 * @param inFile  �ɵ��ļ�
	 * @param oldIsNotDele  �Ƿ�ɾ�����ļ�
	 * @return  �ƶ��Ƿ�ɹ�
	 */
	@SuppressWarnings("resource")
	public static boolean write(File outFile, File inFile,boolean oldIsNotDele) {
		FileInputStream in = null;
		try {
			in=new FileInputStream(inFile);
		} catch (FileNotFoundException e1) {
			throw new NullPointerException("FileNotFound��");
		}
		//������д
		write(outFile, in);
		if (oldIsNotDele) {
			if (!inFile.delete()) {
				throw new OperationFailException("���ļ�ɾ��ʧ��");
			}
		} 
		return true;
	}
	/**
	 * 
	 * @param os
	 * @param str ��ôƴ�ӵ��Ҳ��� ��֮����������16���Ƶ��ַ���
	 * @param osIsClose  ���Ƿ�ر�
	 */
	public static void writeHexStringToBytes(OutputStream os,String str,boolean osIsClose) {
		BufferedOutputStream br_os = new BufferedOutputStream(os);
		int total=str.length();
		if (total % 2 != 0) {
			throw new IllegalStateException("�ַ��������ܷ�����2:False");
		} 
		for (int i = 0, j = 0; i < total; i = i + 2, j++) {
			String str_lin = str.substring(i, i + 2);
			System.out.println("��" + j + "��ʮ�����ַ�Ϊ��" + str_lin);
			int hex = Integer.parseInt(str_lin, 16);
			byte bt = (byte) (hex);
			try {
				br_os.write(bt);
			} catch (IOException e) {
				throw new OperationFailException("��д��ʧ�� ��");
			}
		}
		try {
			br_os.flush();
		} catch (IOException e1) {
			throw new OperationFailException("���������ʧ��");
		}
		if(osIsClose)
		{
			try {
				br_os.close();
			} catch (IOException e) {
				throw new OperationFailException("���رշ����쳣��");
			}
		}
	}
	
}
