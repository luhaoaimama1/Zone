package json;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @version 2015.7.15
 * @author Zone
 *
 */
public class IOUtils {
	public static final String NEW_LINE_STRING ="\r\n";
//	public static void main(String[] args) {
//		String gaga="我喜欢你  \r\n咋办abkbkakak ";
//		File f = new File("D:/json8.txt");
//		write(f, gaga,"gbk");
//	}
	// -----------------------------------------------------------InputStream---------------------------------------------------------------
	/**
	 * 
	 * @param in
	 *            原始输入流
	 * @param encoded
	 *            编码你懂得 一般 GBK UTF-8 ISO8859-1
	 * @return 字符串
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
				throw new IllegalStateException("流关闭发生异常！");
			}
		}
		return str.toString();
	}

	/**
	 * 
	 * @param file
	 *            file你懂的
	 * @param encoded
	 *            编码你懂得 一般 GBK UTF-8 ISO8859-1
	 * @return 字符串
	 */
	public static String read(File file, String encoded) {
		String str = null;
		try {
			//调用已写
			str = read(new FileInputStream(file), encoded);
		} catch (FileNotFoundException e) {
			throw new NullPointerException("FileNotFound！");
		}
		return str;
	}

	/**
	 * 
	 * @param in
	 *            输入流
	 * 
	 * @return byte解码成16进制字符串
	 */
	public static String readToHexString(InputStream in,boolean inIsClose) {
		BufferedInputStream br = new BufferedInputStream(in);
		byte[] b = new byte[2];
		StringBuffer sb = new StringBuffer();
		try {
			while (br.read(b, 0, 1) != -1) {
				// 一次读一个字节
				int lin = b[0] & 0xff;
				sb.append(Integer.toHexString(lin));
			}
		} catch (IOException e) {
			throw new IllegalStateException("流读取发生IOException！！！");
		}
		if (inIsClose) {
			try {
				in.close();
			} catch (IOException e) {
				throw new IllegalStateException("流关闭发生异常！");
			}
		}
		return sb.toString();
	}

	// -----------------------------------------------------------OutStream---------------------------------------------------------------
	public static boolean write(File outFile, String str,String encoded) {
		ByteArrayInputStream in=null;
		try {
			in=new ByteArrayInputStream(str.getBytes(encoded));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		if(in!=null){
			return write(outFile, in);
		}else{
			return false;
		}
	}
	/**
	 * 
	 * @param file
	 *            存放的文件
	 * @param in
	 *            输入流
	 * @return 成功失败
	 */
	public static boolean write(File outFile, InputStream in) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);
		} catch (FileNotFoundException e1) {
			throw new NullPointerException("FileNotFound！");
		}
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = in.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			throw new IllegalStateException("流读取发生IOException！！！");
		} finally {
			try {
				in.close();
				fos.close();
			} catch (IOException e) {
				throw new IllegalStateException("流关闭发生异常！");
			}
		}
		return true;
	}
	/**
	 * 
	 * @param outFile   新的文件
	 * @param inFile  旧的文件
	 * @param oldIsNotDele  是否删除就文件
	 * @return  移动是否成功
	 */
	@SuppressWarnings("resource")
	public static boolean write(File outFile, File inFile,boolean oldIsNotDele) {
		FileInputStream in = null;
		try {
			in=new FileInputStream(inFile);
		} catch (FileNotFoundException e1) {
			throw new NullPointerException("FileNotFound！");
		}
		//调用已写
		write(outFile, in);
		if (oldIsNotDele) {
			if (!inFile.delete()) {
				throw new IllegalStateException("旧文件删除失败");
			}
		} 
		return true;
	}
	/**
	 * 
	 * @param os
	 * @param str 怎么拼接的我不管 总之我最后用这个16进制的字符串
	 * @param osIsClose  流是否关闭
	 */
	public static void writeHexStringToBytes(OutputStream os,String str,boolean osIsClose) {
		BufferedOutputStream br_os = new BufferedOutputStream(os);
		int total=str.length();
		if (total % 2 != 0) {
			throw new IllegalStateException("字符串长度能否整除2:False");
		} 
		for (int i = 0, j = 0; i < total; i = i + 2, j++) {
			String str_lin = str.substring(i, i + 2);
			System.out.println("第" + j + "个十进制字符为：" + str_lin);
			int hex = Integer.parseInt(str_lin, 16);
			byte bt = (byte) (hex);
			try {
				br_os.write(bt);
			} catch (IOException e) {
				throw new IllegalStateException("流写入失败 ！");
			}
		}
		try {
			br_os.flush();
		} catch (IOException e1) {
			throw new IllegalStateException("输出流推送失败");
		}
		if(osIsClose)
		{
			try {
				br_os.close();
			} catch (IOException e) {
				throw new IllegalStateException("流关闭发生异常！");
			}
		}
	}
	
}
