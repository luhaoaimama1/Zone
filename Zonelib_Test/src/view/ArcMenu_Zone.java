package view;


import java.util.ArrayList;
import java.util.List;

import view.utils.Attr_Styleable_Utils;

import com.example.mylib_test.R;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 *
 * <strong>�˿ؼ�����view��������</strong> ���������������� <strong>������    </strong>
 * <br>���Ŀؼ����� �ϼ�����Ū   ���ҿ�������Բ���ȥ����  
 * 
 * @author Zone
 *
 */
public class ArcMenu_Zone extends RelativeLayout implements OnClickListener{
	
	//�뾶
	private int mRadius = 100;
	//����û����
	private int position = 0;
	// ��һ����ת�ؼ������ĵ��0
	private Float start_angle = 0F;
	//չ���Ƕ�
	private Float spreadAngel = 90F;
	//����   ˳ʱ��Or��ʱ��
	private Rorate_Mode rorate_mode=Rorate_Mode.DIRECTION_CCW;
	//��ʼ״̬��  �ر�
	private ToggleState toggleState=ToggleState.CLOSE;
	//��¼���ĵ�view
	private View centerView_Click;
	private Point centerPoint;
	
	//����ΧView ���ĵ� ��ʾ��Χ����¼����
	private List<Rect> outter_viewRectList;
	private List<View> outter_viewList;
	private List<Point> outter_viewCenterPointList;
	//��������ʱ��  ����
	private static final int duration=300;
	private static final int maxDelay_Duration=100;
	private   onItemlistener listener;
	
	{
		outter_viewRectList=new ArrayList<Rect>();
		outter_viewList=new ArrayList<View>();
		outter_viewCenterPointList=new ArrayList<Point>();
	}
	private enum ToggleState{
		OPEN,CLOSE
	}
	private enum Rorate_Mode{
		/**
		 * ˳ʱ��
		 */
		DIRECTION_CW,
		/**
		 * ��ʱ��
		 */
		DIRECTION_CCW
	}

	public ArcMenu_Zone(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		Attr_Styleable_Utils attr_utils = new Attr_Styleable_Utils(context,
				attrs, defStyle, R.styleable.ArcMenu_Zone, this);
		mRadius = attr_utils.get_attr_demen_toPix(
				R.styleable.ArcMenu_Zone_radius, mRadius);
		position = attr_utils.get_attr_enum_to_int(
				R.styleable.ArcMenu_position, position);
		start_angle = attr_utils.get_attr_Float(
				R.styleable.ArcMenu_Zone_startAngel, start_angle);
		spreadAngel = attr_utils.get_attr_Float(
				R.styleable.ArcMenu_Zone_startAngel, spreadAngel);
		attr_utils.recycle();

	}

	public ArcMenu_Zone(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ArcMenu_Zone(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);
	}

	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		outter_viewRectList.clear();
		outter_viewList.clear();
		outter_viewCenterPointList.clear();
		
		if(getChildCount()>0){
			//�������ĵ�ؼ��Ĳ���
			centerView_Click = getChildAt(getChildCount()-1);
			//�����İ�ť ���õ������
			centerView_Click.setOnClickListener(this);
			RelativeLayout.LayoutParams st = (RelativeLayout.LayoutParams) centerView_Click.getLayoutParams();
			centerView_Click.layout(st.leftMargin, st.topMargin, st.leftMargin+ centerView_Click.getMeasuredWidth(),
					st.topMargin + centerView_Click.getMeasuredHeight());
//			Log.d("centerPoint", "Pingmu:left_"+st.leftMargin+" \t right_"+
//			st.leftMargin+ centerView.getMeasuredWidth()+" \t top_"+st.topMargin
//					+"\t bottom"+st.topMargin + centerView.getMeasuredHeight());
			centerPoint=new Point(st.leftMargin+centerView_Click.getMeasuredWidth()/2,st.topMargin + centerView_Click.getMeasuredHeight()/2);
			//�������Ŀؼ����� ����  ��1��ʼ
			if(getChildCount()>1){
				switch (rorate_mode) {
				case DIRECTION_CW:
					calculate_Rect_Right(centerPoint,1);
					break;
				case DIRECTION_CCW:
					calculate_Rect_Right(centerPoint,-1);
					break;

				default:
					break;
				}
				//���ò���
				
			}
		}
		Log.d("gaga", "Pingmu:left_0 \t right_"+getMeasuredWidth()+" \t top_0 \t bottom"+getMeasuredHeight());
	}

	private void calculate_Rect_Right(Point centerPoint,int directionMode) {
		Log.d("vie"+0+"���ĵ�", centerPoint.x+":"+centerPoint.y);
		if(directionMode!=1&&directionMode!=-1){
			throw new IllegalArgumentException("������directionMode ������1 ����-1��");
		}
		//���Ƕ� ����
		int count=getChildCount();
		//��1��Ϊ�� �������Ǹ�viewȥ��
		for (int i = 0; i <count-1 ; i++) {
			View v=getChildAt(i);
			int viewWidth=v.getMeasuredWidth();
			int viewHeight=v.getMeasuredHeight();
			int outterViewIndex=i;
			
			Float averageAngel=spreadAngel/(count-2);
			Float viewAngel=averageAngel*outterViewIndex*directionMode+start_angle*directionMode;
			//sin cos�ǻ���  2PI  ��360��
			double rad = viewAngel*(2*Math.PI)/360;
			int cosX=(int)(Math.cos(rad)*mRadius);
			int cosY=(int)(Math.sin(rad)*mRadius);
			int index_Center_Width=cosX+centerPoint.x;
			int index_Center_Height=cosY+centerPoint.y;
			//��¼��ΧView ���ĵ� ��ʾ��Χ
			outter_viewCenterPointList.add(new Point(index_Center_Width,index_Center_Height));
			outter_viewList.add(v);
			Rect rectShow = new Rect(index_Center_Width-viewWidth/2, index_Center_Height-viewHeight/2, 
					index_Center_Width+viewWidth/2, index_Center_Height+viewHeight/2);
			outter_viewRectList.add(rectShow);
			
			v.layout(index_Center_Width-viewWidth/2, index_Center_Height-viewHeight/2, 
					index_Center_Width+viewWidth/2, index_Center_Height+viewHeight/2);
			final int position_Outter=outterViewIndex;
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					menuItemAnim(v);
					if (listener!=null) {
						listener.onItemlistener(v);
					}
					System.out.println("������ǵڼ�����"+position_Outter);
				}
			});
			v.setVisibility(INVISIBLE);
			Log.d("vie"+i+"���ĵ�", index_Center_Width+":"+index_Center_Height);
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v.equals(centerView_Click)){
			centerView_rotate(v);
			toggle();
		}
		
	}

	private void toggle() {

		int count=outter_viewList.size();
		for (int i = 0; i <count ; i++) {
			AnimationSet animset = new AnimationSet(true);
			final View view=outter_viewList.get(i);
			view.setVisibility(VISIBLE);

			Log.d("ani_outterView_"+i, "x��"+ centerPoint.x+"-->"+ outter_viewCenterPointList.get(i).x+"\ty:"+centerPoint.y
					+"-->"+outter_viewCenterPointList.get(i).y);
			Animation translateAnim = null;
			switch (toggleState) {
			case OPEN:
				//toClose
				translateAnim = new TranslateAnimation(0L, centerPoint.x- outter_viewCenterPointList.get(i).x,
						0L,centerPoint.y - outter_viewCenterPointList.get(i).y);
				break;
			case CLOSE:
				//toOpen   ����ӳ�100��
				animset.setInterpolator(new OvershootInterpolator(2F));
				translateAnim = new TranslateAnimation(centerPoint.x- outter_viewCenterPointList.get(i).x, 0L,
						centerPoint.y - outter_viewCenterPointList.get(i).y, 0L);
				translateAnim.setStartOffset((i * maxDelay_Duration) / (count - 1));
				break;

			default:
				break;
			}
		
			translateAnim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					System.out.println("onAnimationEnd");
					//����������ʾ�ؼ�
					switch (toggleState) {
					case OPEN:
						//��
						view.setClickable(true);
						
						break;
					case CLOSE:
						//�ر�
						view.setClickable(false);
						view.setVisibility(INVISIBLE);
						break;
					default:
						break;
					}
				}
			});
			
			
			translateAnim.setDuration(duration);  
			translateAnim.setFillAfter(true);
			Animation rotateAnim = new RotateAnimation(0, 720,Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f); 
			rotateAnim.setFillAfter(true);
			rotateAnim.setDuration(duration);
			//˳������ ���� �����������
			animset.addAnimation(rotateAnim);
			animset.addAnimation(translateAnim);
			
			view.startAnimation(animset);
		}
		//������ϸ���״̬
		switch (toggleState) {
		case OPEN:
			toggleState=ToggleState.CLOSE;
			break;
		case CLOSE:
			toggleState=ToggleState.OPEN;
			break;
		default:
			break;
		}
		
	}
	/**
	 * ��ʼ�˵������������MenuItem�Ŵ���ʧ����������С��ʧ
	 * @param item
	 */
	private void menuItemAnim(View v){
		for (View item : outter_viewList) {
			AnimationSet animationSet=new AnimationSet(true);
			if(v.equals(item)){
				//�Ŵ󶯻�
				Animation quitscale=new ScaleAnimation(1F, 4.0F,1F, 4.0F, Animation.RELATIVE_TO_SELF,
						0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
				Animation quitalpha=new AlphaAnimation(1F, 0F);
				quitscale.setDuration(duration);
				quitalpha.setDuration(duration);
				animationSet.addAnimation(quitscale);
				animationSet.addAnimation(quitalpha);
				
			}else{
				//��С����
				Animation quitscale=new ScaleAnimation(1F,0F, 1F, 0F, Animation.RELATIVE_TO_SELF,
						0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
				Animation quitalpha=new AlphaAnimation(1F, 0F);
				quitscale.setDuration(duration);
				quitalpha.setDuration(duration);
				animationSet.addAnimation(quitscale);
				animationSet.addAnimation(quitalpha);
			}
			animationSet.setFillAfter(true);
			item.startAnimation(animationSet);
			item.setVisibility(INVISIBLE);
			item.setClickable(false);
			toggleState=ToggleState.CLOSE;
		}
		
	}
	private void centerView_rotate(View v) {
		RotateAnimation animation=new RotateAnimation(0F, 270F,
				Animation.RELATIVE_TO_SELF, 0.5F,Animation.RELATIVE_TO_SELF, 0.5F);
		animation.setDuration(duration);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}
	public interface onItemlistener{
		public abstract void onItemlistener(View v);
	}
	public void setOnItemlistener(onItemlistener listener){
		this.listener=listener;
	}

}
