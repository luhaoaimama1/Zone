package Android.Zone.Wifi;

import java.lang.reflect.Method;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

/**
 * �����й�wifi��3G�����Ѿ��������  
 * <br>Ϊ���  wifi SSID�ظ�����  �������������޸�
 * <br> private û�� get �õ���˵��  mWifiConfiguration mNetWorkInfo OK��
 * @author Zone
 *
 */
public class MyWifiAnd3G {
	
	// ��������
	private ConnectivityManager mConnectivityManager;
	private WifiManager mWifiManager;
	
	// ����NetworkInfo��WifiInfo����
	private NetworkInfo mNetWorkInfo;
	private WifiInfo mWifiInfo;

	// ɨ��������������б����������б�
	private List<ScanResult> mWifiList;
	private List<WifiConfiguration> mWifiConfiguration;
	
	// ����һ��WifiLock
	private WifiLock mWifiLock;

	
	//	<!-- WIFIȨ�� -->
	//    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
	//    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
	//    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	//    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
	
	/**
	 * ���wifiȨ��  ����lib�� �ɿ���<br> 
	 * ��ʼ�� ���� <br>
	 * @param context
	 */
	public MyWifiAnd3G(Context context) {
		//��ʼ�� Manager
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//��ʼ�� ��ǰ��wifiInfo��netWorkInfo
		mWifiInfo = mWifiManager.getConnectionInfo();
		mNetWorkInfo=mConnectivityManager.getActiveNetworkInfo();
//		Log.e("�鿴�Ķ���", "WifiManager��Ϣ��:"+mWifiManager.toString()+"\n"+"ConnectivityManager��Ϣ��:"+mConnectivityManager.toString()+"\n"+"WifiInfo��Ϣ��:"+mWifiInfo.toString()+"\n");
		
		
	}
	
	/**
	 * �õ����úõ�����
	 * 
	 * @return
	 */
	public List<WifiConfiguration> getConfiguration() {
		return mWifiManager.getConfiguredNetworks();
	}
	/**
	 * @return ��ǰ��WifiInfo
	 */
	public WifiInfo getWifiInfo() {
		return mWifiManager.getConnectionInfo();
	}

	
	/**
	 * �򿪻�رյ�ǰ3G����
	 * 
	 * @param value
	 *            true �� false �ر�
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object openOrClose3GNet(boolean value) {
		Class ownerClass = mConnectivityManager.getClass();
		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;
		Method method;
		Object obj = null;
		try {
			method = ownerClass.getMethod("setMobileDataEnabled", argsClass);
			obj = method.invoke(mConnectivityManager, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	/**
	 * 
	 * @return  ����3gNet��״̬
	 * @throws Exception
	 */
	public boolean state3GNet()  {
		Class<? extends ConnectivityManager> ownerClass = mConnectivityManager
				.getClass();
		boolean state = false;
		try {
			Method method = ownerClass.getMethod("getMobileDataEnabled");
			state = (Boolean) method.invoke(mConnectivityManager);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return state;
	}

	/**
	 * �жϵ�ǰWIFI�Ƿ��ڴ�״̬
	 * 
	 * @return
	 */
	public boolean isWiFiActive() {
		return mWifiManager.isWifiEnabled();
	}
	/**
	 * ���ص�ǰWifi״̬
	 * 
	 * @return
	 */
	public int checkState() {
		// WIFI_STATE_DISABLED WIFI�ر�
		// WIFI_STATE_DISABLING WIFI���ڹر�
		// WIFI_STATE_ENABLED WIFI�Ѵ�
		// WIFI_STATE_ENABLING WIFI���ڴ�
		// WIFI_STATE_UNKNOWN WIFI״̬δ֪
		return mWifiManager.getWifiState();
	}
	/**
	 * ��Wifi
	 */
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);//false�ǹر�wifi true�Ǵ�wifi
			mWifiManager.isWifiEnabled();//wifi�ǿ��ŵ���
		}
	}

	/**
	 * �ر�Wifi
	 */
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}


	/**
	 * �ж��ֻ���ǰ�Ƿ�����
	 * 
	 * @return
	 */
	public boolean isNetwork() {
		if (mNetWorkInfo == null) {
			return false;
		}
			return mNetWorkInfo.isAvailable();
	}
	/**
	 * �ж��ֻ���ǰ��������  WifiOr3G
	 * 
	 * @return  0:������ 1:3g 2:WIFI
	 */
	public int getNowConnectType() {
		if (mNetWorkInfo == null) {
			return 0;
		}else
		{
			if("WIFI".equals(mNetWorkInfo.getTypeName()))
			{
				return 2;
			}
		}
		return 1;
		
	}

	
	/**
	 *  ����ָ�������úõ�������� 
	 * 
	 * @param wcfg     WifiConfiguration(���ڴ�����get��)
	 * @param mostCount     ��������ٴ�
	 * @param sleepMs     ÿ��˯���ٺ���
	 */
	public boolean connectConfiguration(WifiConfiguration wcfg,int mostCount,int sleepMs) {
		// �����������úõ�������������
		if (wcfg ==null) {
			System.err.println("WifiConfiguration wcfgΪnull������");
			return  false;
		}
		mWifiInfo=mWifiManager.getConnectionInfo();
		if (mWifiInfo != null && mWifiInfo.getNetworkId() == wcfg.networkId&&mWifiInfo.getLinkSpeed()>0) {
			// ������  ��wifi���ҵ�ǰwifi  ���������Ҫ���ӵ�wifi
			System.out.println("��ǰ���ӵ�wifi �Ѿ������� �����ظ�����~~~");
			return  true;

		} else {
			// �������úõ�ָ��ID������
			System.out.println("Ҫ���ӵ�netid:\t" + wcfg.networkId);
			boolean connStatue= mWifiManager.enableNetwork(wcfg.networkId, true);
			boolean  resultStatue=false;
			if (connStatue) {
				// ������ wifi �ɹ���ʱ��
				int i = 1;
				while (i <= mostCount) {
					try {
						if (getWifiInfo().getNetworkId() == wcfg.networkId
								&& WifiManager.WIFI_STATE_ENABLED == mWifiManager.getWifiState()) {
							resultStatue = true;
							System.out.println("wifi�����ɹ�success!!!");
							break;
						}
						Thread.sleep(sleepMs);
						if (i == 1) {
							System.out.println("ÿ�εȴ�ʱ��Ϊ:\t" + sleepMs + "\tms");
						}
						System.out.println("�ȴ�wifi��ȫ����  ��" + i + "��");
						i++;
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.err.println("Thread sleep�����쳣");
					}
				}
				if (!resultStatue) {
					System.err.println("����fail");
				}
			}
		
			return resultStatue;
		}
		
	}
	/**
	 * �Ͽ���ǰ����
	 */
	public boolean disconnectWifi() {

		// �Ͽ�ָ���� wifi����
		// mWifiManager.disableNetwork(wcfg.networkId);
		// �Ͽ���ǰwifi������ һ����˼
		mWifiInfo=mWifiManager.getConnectionInfo();
		if (mWifiInfo == null) {
			System.out.println("��ǰû���������� �ʲ��öϿ�~~~");
			return true;
		}
		return mWifiManager.disconnect();
	}
	/**
	 * ��������
	 */
	public void startScan() {
		mWifiManager.startScan();
		// �õ�ɨ����
		mWifiList = mWifiManager.getScanResults();
		// �õ����úõ���������
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	/**
	 * �鿴ɨ����
	 * 
	 * @return
	 */
	@SuppressLint("UseValueOf")
	public StringBuilder lookUpScan() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder
					.append("Index_" + new Integer(i + 1).toString() + ":");
			// ��ScanResult��Ϣת����һ���ַ�����
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}


	/**
	 * 
	 * @return  Ҳ�����磺ip�������ַ��192.168.60.104
	 */
	public String getIPAddress() {
		if (mWifiInfo == null) {
			return null;
		} else {
			//���磺�õ�IP��ַ ip :1748805824
			int ip = mWifiInfo.getIpAddress();
			return ((ip & 0xff) + "." + (ip >> 8 & 0xff) + "."+ (ip >> 16 & 0xff) + "." + (ip >> 24 & 0xff));
		}
	}


	
	/**
	 * ����WifiLock
	 */
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	/**
	 * ����WifiLock
	 */
	public void releaseWifiLock() {
		// �ж�ʱ������
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	/**
	 * ����һ��WifiLock
	 */
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	
	

}
