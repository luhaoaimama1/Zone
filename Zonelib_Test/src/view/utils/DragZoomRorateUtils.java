package view.utils;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.FloatMath;

public class DragZoomRorateUtils {

	/**
	 * 
	 * @param rectf
	 * @param target
	 * @return ���� ���� ���target �ڸ��ؼ��е� rectf  ���Ͻ�
	 */
	public static RectF getRightTopRectF(RectF rectf, RectF target) {
		RectF rect = new RectF(rectf);
		rect.offset(target.right - rect.centerX(), target.top - rect.centerY());
		return rect;
	}
	/**
	 * 
	 * @param rectf
	 * @param target
	 * @return ���� ���� ���target �ڸ��ؼ��е� rectf  ���½�
	 */
	public static RectF getLeftBottomRectF(RectF rectf, RectF target) {
		RectF rect = new RectF(rectf);
		rect.offset(target.left - rect.centerX(),
				target.bottom - rect.centerY());
		return rect;
	}
	 // ��������������֮��ľ���   
	public  static float distance(PointF start, PointF end) {
		float x = start.x - end.x;
		float y = start.y - end.y;
		return FloatMath.sqrt(x * x + y * y);
	}
}
