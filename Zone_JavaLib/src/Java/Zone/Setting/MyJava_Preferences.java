package Java.Zone.Setting;

import java.net.URL;

/**
 * �ڲ����Ĳ������� �뷽������
 * 
 * @author Zone
 * 
 */
public class MyJava_Preferences {
	public static String a="b";

	/**
	 * ÿ�������Ŀ�ʼ ����� log���׹۲�
	 */
	public static void op() {
//		StackTraceElement ste = new Throwable().getStackTrace()[1];
//		String printStr = ste.toString();
//		System.err.println("---------------" + printStr+ "��ʼ-------------------------");
	}
	/**
	 * ÿ�������Ľ��� ����� log���׹۲�
	 */
	public static void ed() {
//		StackTraceElement ste = new Throwable().getStackTrace()[1];
//		String printStr = ste.toString();
//		System.err.println("---------------" + printStr+ "����-------------------------");
	}
	/**
	 * �����ڲ���������ʼ
	 */
	public static void InnerOp() {
//		System.err.println("---------------�����ڲ�����:����ʼ-------------------------");
	}
	/**
	 * �����ڲ�����������
	 */
	public static void InnerEd() {
//		System.err.println("---------------�����ڲ�����:������-------------------------");
	}
	
	
	/**
	 * 
	 * @return �õ���ǰ������
	 */
	public static String getClassName(){
		StackTraceElement ste = new Throwable().getStackTrace()[1];
		String[] s=ste.getClassName().split("[.]");
		return s[s.length-1]+"��:";
	}
	/**
	 * ����1
	 * Java��OK 
	 * <br>Android�� ����ʹ
	 * @return  ���ô˷��������Ŀ����
	 */
	public static String getProjectName() {
//		StackTraceElement[] a=new Throwable().getStackTrace();
//		int j=0;
//		for (StackTraceElement stackTraceElement : a) {
//			System.out.println("j:"+j+"_====="+stackTraceElement.toString());
//			j++;
//		}
		StackTraceElement st = new Throwable().getStackTrace()[1];
		String className = st.getClassName();
		System.out.println("className��>:" + st.getClassName());
		String classNamePath = className.replace(".", "/") + ".class";
		System.out.println("classNamePath��>:" + classNamePath);
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL is = cl.getResource(classNamePath);
		String path = is.getPath();
		String[] k=path.split("[/]");
		for (int i = 0; i < k.length; i++) {
			if ("bin".equals(k[i])) {
				return k[i - 1];
			}
		}
		return null;
	}
	/**
	 * ����2
	 * Java��OK 
	 * <br>Android�� ����ʹ
	 * @return  ���ô˷��������Ŀ����
	 */
	public static String getProjectName2() {
		StackTraceElement st = new Throwable().getStackTrace()[1];
		String className = st.getClassName();
		URL is=null;
		try {
			System.out.println("className��>:" + st.getClassName());
			System.out.println("������֣�"+Class.forName(className).getClassLoader().getResource(""));
			is = Class.forName(className).getClassLoader().getResource("");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("��Ϊ�ҵ��쳣");
		}
		String path = is.getPath();
		String[] k=path.split("[/]");
		for (int i = 0; i < k.length; i++) {
			if ("bin".equals(k[i])) {
				return k[i - 1];
			}
		}
		return null;
	}
}
