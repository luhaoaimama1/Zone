package Android.Zone.Log;

import android.util.Log;

public class LogUtils {
	private static final String TAG="LogUtils";
	private static boolean printLog=true; 
	/**
	 * this is debug
	 *  <br>color:blue <b>��ɫ<b>
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag,String msg){
		if (printLog) 
			Log.d(tag, msg);
	}
	/**
	 * this is error
	 * <br>color:red <b>��ɫ<b>
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg){
		if (printLog) 
			Log.e(tag, msg);
	}
	/**
	 * this is information
	 * <br>color:green <b>��ɫ<b>
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if (printLog) 
			Log.i(tag, msg);
	}
	/**
	 * this is verbose(<b>��ϸ��Ϣ<b>)
	 *  <br>color:blue <b>��ɫ<b>
	 * @param tag
	 * @param msg
	 */
	public static void v(String tag,String msg){
		if (printLog) 
			Log.v(tag, msg);
	}
	/**
	 * this is warning   
	 <br>color:orange <b>��ɫ<b>
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag,String msg){
		if (printLog) 
			Log.w(tag, msg);
	}
	public static void openWriteLog(){
		printLog=true;
	}
	public static void closeWriteLog(){
		printLog=false;
	}
}
