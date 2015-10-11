package Android.Zone.Sqlite.Annotation.utils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import Android.Zone.Sqlite.Annotation.Column;
import Android.Zone.Sqlite.Annotation.Table;
import Android.Zone.Sqlite.GsonEntity.GsonColumn;
import Android.Zone.Sqlite.GsonEntity.GsonTop;

public class AnUtils {

	public static String getGsonStr(Class<?> t) {
		GsonTop gsonTop = new GsonTop();
		Field[] fields = t.getDeclaredFields();
		List<GsonColumn> gsonColumnList=new ArrayList<GsonColumn>();
		for (Field item : fields) {
			GsonColumn gsonColumn = new GsonColumn();
			gsonColumn.setName(getAnnoColumn_Name_ByField(item, t));
			gsonColumn.setLength(getAnnoColumn_Length_ByField(item, t));
			gsonColumnList.add(gsonColumn);
		}
		gsonTop.setData(gsonColumnList);
		String tempStr =new Gson().toJson(gsonTop);
		return tempStr;
	}
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
			if("".equals(temp)){
				throw new IllegalStateException("ע��column ����Ϊ ���ַ���");
			}
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
			//TODO  ��ע����""��ʱ����  ����ô���ĵ�  �Ҿ��׳��쳣 ����������
			if("".equals(temp)){
				throw new IllegalStateException("ע��column ����Ϊ ���ַ���");
			}
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
