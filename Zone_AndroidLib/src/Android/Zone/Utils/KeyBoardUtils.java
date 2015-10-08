package Android.Zone.Utils;


import Android.Zone.Utils.MeasureUtils.GlobalState;
import Android.Zone.Utils.MeasureUtils.OnMeasureListener;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * �򿪻�ر������
 * 
 * @author zhy
 * 
 */
public abstract class KeyBoardUtils {
	public static int heightDiff=0;
	public OpenStatue openStatue=OpenStatue.NO_MEASURE;
	public enum OpenStatue{
		OPEN,CLOSE,NO_MEASURE
	}
	/**
	 * �������
	 * 
	 * @param mEditText
	 *            �����
	 * @param mContext
	 *            ������
	 */
	public static void openKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * �ر������
	 * 
	 * @param mEditText
	 *            �����
	 * @param mContext
	 *            ������
	 */
	public static void closeKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	/**
	 * monitor ���
	 * ��Ŀ�嵥������
	 *  android:windowSoftInputMode="stateHidden|adjustResize"
	 *  �Ͼ仰�Ľ��ͣ���Activity����<strong>������Ļ</strong>�Ĵ�С�Ա���������̵Ŀռ� 
	 *  
	 * @param mainLayoutView  �������ֵĶ���
	 */
	public void monitorKeybordState(View mainLayoutView) {
		MeasureUtils.measureView_addGlobal(mainLayoutView,
				GlobalState.MEASURE_CONTINUE_LISNTER, new OnMeasureListener() {

					@Override
					public void measureResult(View v, int view_width,
							int view_height) {
						int heightDiffTemp = v.getRootView().getHeight()- v.getHeight();
						if (heightDiffTemp > 100) {
							// ˵�������ǵ���״̬
							if (heightDiff<100) {
								heightDiff = heightDiffTemp;
							}
							switch (openStatue) {
							case CLOSE:
								openStatue=OpenStatue.OPEN;
								openState(heightDiff);
								break;
							case NO_MEASURE:
								openStatue=OpenStatue.OPEN;
								openState(heightDiff);
								break;
							default:
								break;
							}
						} else {
							switch (openStatue) {
							case OPEN:
								openStatue=OpenStatue.CLOSE;
								closeState(heightDiff);
								break;
							case NO_MEASURE:
								openStatue=OpenStatue.CLOSE;
								closeState(heightDiff);
								break;
							default:
								break;
							}
							
						}
					}
				});
	}
	/**
	 * ���̸߶�
	 * @param keyboardHeight
	 */
	public abstract void openState(int keyboardHeight);
	/**
	 * ���̸߶�
	 * @param keyboardHeight
	 */
	public abstract void closeState(int keyboardHeight);
}
