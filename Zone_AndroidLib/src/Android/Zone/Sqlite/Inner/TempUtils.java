package Android.Zone.Sqlite.Inner;

import java.lang.reflect.Type;

public class TempUtils {
	/**
	 * ���ӣ�typeIsNotEquals(String.class, field.getGenericType())
	 * @param cl  String.class
	 * @param type  �������Type
	 * @return  �Ƿ���ͬ
	 */
	public static boolean typeIsEquals(Class cl,Type type){
		if(cl.toString().equals(type.toString())){
			return true;
		}
		return false;
	}
}
