package Android.Zone.Log;

import android.content.Context;
import android.widget.Toast;

/**
 * Toastͳһ������
 * 
 */
public class ToastUtils {
	private static boolean showToast = true;
	/**
	 * ��ʱ����ʾToast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message)
	{
		if (showToast)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��ʱ����ʾToast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message)
	{
		if (showToast)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��ʱ����ʾToast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message)
	{
		if (showToast)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * ��ʱ����ʾToast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message)
	{
		if (showToast)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * �Զ�����ʾToastʱ��
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration)
	{
		if (showToast)
			Toast.makeText(context, message, duration).show();
	}

	/**
	 * �Զ�����ʾToastʱ��
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration)
	{
		if (showToast)
			Toast.makeText(context, message, duration).show();
	}
	public static void openToast(){
		showToast=true;
	}
	public static void closeToast(){
		showToast=false;
	}
}
