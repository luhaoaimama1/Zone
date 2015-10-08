package Android.Zone.Utils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
/**
 * @author Zone
 *
 */
public class MeasureUtils {
	public enum GlobalState{
		MEASURE_REMOVE_LISNTER,MEASURE_CONTINUE_LISNTER
	}
	public interface OnMeasureListener{
		/**
		 * �滭���  �������Ѿ�����  ʲôֵ�����Եõ���
		 * @param v
		 * @param view_width
		 * @param view_height
		 */
		public void measureResult(View v,int view_width,int view_height);
	}
	/**
	 * 
	 * @param v
	 * @param gs  ö�ٱ�ʾ �������Ƿ��Ƴ��˼��� Ŀ���ǣ�Ϊ�˷�ֹ����ε���
	 */
	public static void measureView_addGlobal(final View v,final GlobalState gs,final OnMeasureListener oml){
		ViewTreeObserver  vto = v.getViewTreeObserver(); 
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				int height = v.getMeasuredHeight();
				int width = v.getMeasuredWidth();
				if(oml!=null){
					oml.measureResult(v,width, height);
				}
				switch (gs) {
				case MEASURE_REMOVE_LISNTER:
					v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					break;
				case MEASURE_CONTINUE_LISNTER:
					
					break;

				default:
					break;
				}
			}
		});
	}
}
