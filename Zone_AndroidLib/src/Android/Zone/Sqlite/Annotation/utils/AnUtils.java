package Android.Zone.Sqlite.Annotation.utils;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Android.Zone.Sqlite.Annotation.Column;
import Android.Zone.Sqlite.Annotation.Table;

public class AnUtils {
	/**
	 * �õ�ע��ı���  ���û�� ��getSimpleName
	 * @param t
	 * @return
	 */
	public static String getTableAnnoName(Class<?> t){
		String temp=null;
		Table anno =t.getAnnotation(Table.class);
		if(anno!=null){
			temp=anno.name();
		}else{
			temp=t.getSimpleName();
		}
		return temp;
	}
	//TODO  
	public static void getColumnAnno(){
		
	}
	
	/**
	 * �����  ��ɾ�Ĳ� �õ�
	 * 
	 * �����ַ���''��ȡ�� _abc_  ���滻�� abc  
	 * û��ע���� ��ӴŪ��ԭ���� ��abc
	 */
	public static String replaceByAnnoColumn(String abc,Class<?> t) {
		Pattern pa = Pattern.compile("_.*?_");
		Matcher m = pa.matcher(abc);
		String tempStr=abc;
		while (m.find()) {
			String olderChar = m.group();
			String newChar = olderChar.substring(1, olderChar.length() - 1);
			//������ע��ĳ�newChar ����_?_ ֻ��һ�� �����÷ָ�
			Column annoStr=null;
			try {
				 Field field = t.getDeclaredField(newChar);
				 annoStr =field.getAnnotation(Column.class);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			if(annoStr!=null){
				newChar=annoStr.column();
			}
			tempStr=tempStr.replace(olderChar, newChar);
		}
		return tempStr;
	}
	public static String getAnnoColumn_Name_ByField(Field f,Class<?> t){
		String temp=null;
		Column anno = f.getAnnotation(Column.class);
		if(anno!=null){
			temp=anno.column();
		}else{
			temp=f.getName();
		}
		return temp;
	}
	public static String getAnnoColumn_Length_ByField(Field f,Class<?> t){
		String temp=null;
		Column anno = f.getAnnotation(Column.class);
		if(anno!=null){
			temp=anno.length();
		}else{
			temp="100";
		}
		return temp;
	}
	
}
