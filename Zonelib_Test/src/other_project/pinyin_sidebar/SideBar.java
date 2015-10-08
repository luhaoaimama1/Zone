package other_project.pinyin_sidebar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {

	public static String[] chars = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "#" };
	private int chooseIndex; // ��ѡ�е���ĸ�±�
	private Paint paint = new Paint();// ����
	private TextView mTextView;
	
	private OnLetterSelectedListener letterSelectedListener;

	public OnLetterSelectedListener getLetterSelectedListener() {
		return letterSelectedListener;
	}

	public void setLetterSelectedListener(
			OnLetterSelectedListener letterSelectedListener) {
		this.letterSelectedListener = letterSelectedListener;
	}

	public TextView getmTextView() {
		return mTextView;
	}

	public void setmTextView(TextView mTextView) {
		this.mTextView = mTextView;
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * ������ͼ
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ��ȡ�ؼ��Ŀ��
		int width = getWidth();
		int height = getHeight();
		// ÿ����ĸ�ĸ߶�
		int singleHeight = height / chars.length;
		for (int i = 0; i < chars.length; i++) {
			paint.setColor(Color.rgb(33, 66, 99));
			paint.setTypeface(Typeface.DEFAULT_BOLD);// ���ô���
			paint.setAntiAlias(true); // �����
			paint.setTextSize(20);
			if (chooseIndex == i) {
				paint.setColor(Color.parseColor("#3399ff"));
			}
			// �����ı�
			float xPos = (width - paint.measureText(chars[i])) / 2;
			float yPos= singleHeight*i+singleHeight;	// ÿ����ĸ��Y����
			canvas.drawText(chars[i], xPos, yPos, paint);
			paint.reset();	// ���û���
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action=event.getAction(); // ��ָ����
		float y=event.getY();	// ��ָY����
		int oldChoode=chooseIndex;
		int c=(int) (y/this.getHeight()*chars.length);
		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));	//���ñ���͸��
			chooseIndex=-1;
			invalidate(); // �ػ�
			if (null != mTextView) {
				mTextView.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(com.example.mylib_test.R.drawable.side_bar_bg); // ���ú�ɫ����
			// ����y�����ȡ�㵽����ĸ
			if (oldChoode != c) {
				if (c>=0 && c<=chars.length) {
					if (letterSelectedListener != null) {
						letterSelectedListener.onLetterSelected(chars[c]);
					}
					if (null != mTextView) {
						mTextView.setVisibility(View.VISIBLE);
						mTextView.setText(chars[c]);// ���ñ�ѡ�е���ĸ
					}
				}
				chooseIndex=c;
				invalidate();
			}
			break;
		}
		return true;
	}
	
	public interface OnLetterSelectedListener{
		public void onLetterSelected(String s);
	}
}
