package view;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class FlowLayout_Zone extends LinearLayout{
	private boolean log=false;
	private static final String TAG="FlowLayout_Zone";
	public FlowLayout_Zone(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
		
	//line���ԣ� lineWidthlist lineHeightList
	private List<Integer> lineWidthlist;
	private List<Integer> lineHeightList;
	private List<List<View>> lineAllView;

	{
		lineWidthlist=new ArrayList<Integer>();
		lineHeightList=new ArrayList<Integer>();
		lineAllView=new ArrayList<List<View>>();

	}
	/**
	 * ͨ���������ӵĴ�С  �����������Լ���Ҫ���
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(log)
			System.out.println("onMeasure");
		//�����������ʱ�� �ѻ���ֵ���
		lineWidthlist.clear();
		lineHeightList.clear();
		lineAllView.clear();
		
		int widthMode=MeasureSpec.getMode(widthMeasureSpec);
		int heightMode=MeasureSpec.getMode(heightMeasureSpec);
		int widthSize=MeasureSpec.getSize(widthMeasureSpec);
		int heightSize=MeasureSpec.getMode(heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		//�Լ������Ŀ��   ��Ҫ���� AT_MOST
		int width=0;
		int height=0;
		
		//lineMaxLength     lineTotallength
		int lineWidth=0;//�п�
		int lineHeight=0;//�и�
		
		//��view
		List<View> lineView=new ArrayList<View>();
		
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child=getChildAt(i);
			Point point=getRealWidth_Height(child);
			int cRealWidth=point.x;
			int cRealHeight=point.y;
			
				//������
				if (lineWidth + cRealWidth > widthSize) {
					/** �������һ������ �����Ŀ�ȴ� */
					//���û���ӵĿ�ȼ�¼Ϊline���
					lineWidthlist.add(lineWidth);
					//�����е�ʱ�� �߶�Ҳ��¼����
					lineHeightList.add(lineHeight);
					//����view��ӽ�ȥ
					lineAllView.add(lineView);
					
					/**
					 * �� view��ӵ��ڶ���
					 */
					//�����view�߸�ֵ���и߶�
					lineHeight = cRealHeight;
					//������ؼ���ֵ���п��  
					lineWidth = cRealWidth;
					lineView=new ArrayList<View>();;
					lineView.add(child);
					/**
					 * ���к� ���������һ��  ��ô�Ѵ���Ҳ���ύ��
					 */
					if(i==count-1){
						//���û���ӵĿ�ȼ�¼Ϊline���
						lineWidthlist.add(lineWidth);
						//�����е�ʱ�� �߶�Ҳ��¼����
						lineHeightList.add(lineHeight);
						lineAllView.add(lineView);
					}
				} else {
					/** û�������Ŀ�� */
					if (i!=count-1) {
						/** ���_����_���һ�� */
						//��ǰ�еĿ������
						lineWidth += cRealWidth;
						//��¼��ǰ�����ĸ߶�
						lineHeight = Math.max(lineHeight, cRealHeight);
						lineView.add(child);
					}else{
						/** ��������һ�� */
						//��ǰ�еĿ������
						lineWidth += cRealWidth;
						//��¼��ǰ�����ĸ߶�
						lineHeight = Math.max(lineHeight, cRealHeight);
						
						//���û���ӵĿ�ȼ�¼Ϊline���
						lineWidthlist.add(lineWidth);
						//�����е�ʱ�� �߶�Ҳ��¼����
						lineHeightList.add(lineHeight);
						//����view��ӽ�ȥ
						lineView.add(child);
						lineAllView.add(lineView);
					}
				}
				
		}
		//������ɺ�  �ۼƸߺ������󳤶�
		for (Integer item : lineHeightList) {
			height+=item;
		}
		for (Integer item : lineWidthlist) {
			width=Math.max(width, item);
		}

		/**
		 * �����wrap_content����Ϊ���Ǽ����ֵ
		 * ����ֱ������Ϊ�����������ֵ
		 */
		setMeasuredDimension(widthMode==MeasureSpec.EXACTLY?widthSize:width, 
				heightMode==MeasureSpec.EXACTLY?heightSize:height);
	}
	/**
	 * ֪���Լ�����   Ȼ������Ӳ���
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(log)
			System.out.println("onLayout");
		int left=0;
		int top=0;
		int right=0;
		int bottom=0;
		for (List<View> line : lineAllView) {
			int lineIndex=lineAllView.indexOf(line);
			bottom+=lineHeightList.get(lineIndex);
			for (View view : line) {
				int viewIndex=line.indexOf(view);
				
				Point point=getRealWidth_Height(view);
				int cRealWidth=point.x;
				int cRealHeight=point.y;
				LayoutParams lp =  (LayoutParams) view.getLayoutParams();
				
				right+=cRealWidth;
				
				int view_Top_Gap=(lineHeightList.get(lineIndex)-cRealHeight)/2;
				
				view.layout(left+lp.leftMargin, top+lp.topMargin+view_Top_Gap, right-lp.rightMargin, bottom-lp.bottomMargin-view_Top_Gap);
				if(log)
				Log.i("lineIndex:"+lineIndex+"_viewIndex:"+viewIndex+"", "left:"+left+"__top:"+top+"__right��"+right+"__bottom:"+bottom);
				//�����Ժ���߼�
				left+=cRealWidth;
			}
			//view���в���
			top+=lineHeightList.get(lineIndex);
			left=0;
			right=0;
		}
	}

	private Point getRealWidth_Height(View view) {
		int cWidth = view.getMeasuredWidth();
		int cHeight = view.getMeasuredHeight();
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		int cRealWidth = cWidth + lp.leftMargin + lp.rightMargin;
		int cRealHeight = cHeight + lp.topMargin + lp.bottomMargin;
		Point point = new Point(cRealWidth, cRealHeight);
		return point;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(log)
			System.out.println("onDraw");
	}
	
}
