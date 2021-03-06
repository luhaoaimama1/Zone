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
		 * 绘画完毕  即布局已经画好  什么值都可以得到了
		 * @param v
		 * @param view_width
		 * @param view_height
		 */
		public void measureResult(View v,int view_width,int view_height);
	}
	/**
	 * 
	 * @param v
	 * @param gs  枚举表示 测量后是否移除此监听 目的是：为了防止被多次调用
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
