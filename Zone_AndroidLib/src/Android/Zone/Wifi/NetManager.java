package Android.Zone.Wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class NetManager {

	/**
	 * �ж�wifi�Ƿ�������״̬
	 * 
	 * @return boolean :����wifi�Ƿ�����
	 */
	public static boolean isWIFI(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean result = false;
		if (networkInfo != null) {
			result = networkInfo.isConnected();
		}
		return result;
	}

	/**
	 * �ж��Ƿ�APN�б���ĳ��������������״̬
	 * 
	 * @return �����Ƿ�����
	 */
	public static boolean isMoble(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean result = false;
		if (networkInfo != null) {
			result = networkInfo.isConnected();
		}
		return result;
	}
}
