package Android.Zone.Setting;

import android.util.Log;

/**
 * �ڲ����Ĳ������� �뷽������
 * 
 * @author a
 * 
 */
public class MyAndroid_Preferences {
	public static String projectName = "Zone_AndroidLib";

	/**
	 * ����Log �����������Ҫдtag <br>
	 * ���������NotPerfectlib.xx.xx <br>
	 * xx.xx��˭���������������˭
	 * 
	 * @param msg
	 */
	public static void MyLog(String msg) {
		StackTraceElement ste = new Throwable().getStackTrace()[1];
		String[] s = ste.toString().split("[.]");
		String sec_path = s[0] + "." + s[1];
		Log.e(projectName + "." + sec_path, msg);
	}
}