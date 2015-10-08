package view;
import java.util.ArrayList;
import java.util.List;

import view.utils.DragZoomRorateUtils;

import com.example.mylib_test.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * �����������Ŀؼ�
 * @author Zone
 *
 */
public class LabelView extends View {
	/**
	 * ����Ƕ�����Ļ������ ����������VIEW�еĿ��
	 */
	private List<MoveAttri> moveAttri_list = new ArrayList<MoveAttri>();
	private Mode mode=Mode.NONE;
	private int width,height;
	private boolean rimVisibility;
	private enum Mode{
		NONE,ZOOM,DRAG 
	}

	/**
	 * ����λ��Ϊ  quitRect ƫ��  �����ĵ� ��ʵ��λ�õĵ�    
	 * @author Zone
	 *
	 */
	public static  Bitmap quitBm=null;
	public static  Bitmap scaleBm=null;
	/** 
	 * 0Ϊ00
	 * 0---1---2 
	 * |       | 
	 * 7   8   3 
	 * |       | 
	 * 6---5---4  
	 */ 
	private class MoveAttri {
		//������Զ����  ÿ�ζ�����  �Լ���λ�� ��ת �Ŵ�  ���ľ��η�Χ
		public float downX = 0, downY = 0, offsetX = 0, offsetY = 0,
				offsetX_history = 0, offsetY_history = 0,scaleRadio=1F,scaleRadio_history=1,
				oriDis=0,ratote=0,ratoteHistory=0,oriRatote=0;
		public Matrix oriMatrix=new Matrix(),invert_Matrix=new Matrix(),scaleMatrix=new Matrix(),
				quitMatrix=new Matrix();
		
		/**---------------------dst���Ǳ仯�� ��������� start------------------ */
		public float centerLeft_first=0,centerTop_first=0;
		public Bitmap bitmap=null;
		public float [] srcPs , dstPs ;  
		public RectF rect_img_widget=new RectF(),dst_rect_img_widget=new RectF();
		public RectF quitRect=new RectF(), dst_quitRect=new RectF();
		public RectF scaleRect=new RectF(),dst_scaleRect=new RectF();
		/**---------------------���������    end------------------ */
		@Override
		public String toString() {
			System.out.println("{downX:"+downX+",downY"+downY+",offsetX"+offsetX+",offsetY"+offsetY+",offsetX_history"+offsetX_history
					+",offsetY_history"+offsetY_history+",centerLeft_first"+centerLeft_first+",centerTop_first"+centerTop_first+"}");
			return super.toString();
		}
		
	}
	/** 
	 * 0Ϊ00
	 * 0---1---2 
	 * |       | 
	 * 7   8   3 
	 * |       | 
	 * 6---5---4  
	 */ 
	private RectF getScaleRectF(MoveAttri item){
		getMainRect_WithRorate(item);
		 float zoomX = item.dstPs[4] ;
		 float zoomY = item.dstPs[5] ;
		 item.scaleMatrix.reset();
		 item.scaleMatrix.postTranslate(zoomX-item.scaleRect.width()/2,zoomY-item.scaleRect.height()/2);
		 item.scaleMatrix.postRotate(item.ratote+item.ratoteHistory,zoomX,zoomY);
		 item.scaleMatrix.mapRect(item.dst_scaleRect, item.scaleRect);
		return item.dst_scaleRect;
	}
	private Matrix getScaleMatrix(MoveAttri item){
		getScaleRectF(item);
		return 	item.scaleMatrix;
	}
	/** 
	 * 0Ϊ00
	 * 0---1---2 
	 * |       | 
	 * 7   8   3 
	 * |       | 
	 * 6---5---4  
	 */ 
	private RectF getQuitRectF(MoveAttri item){
		getMainRect_WithRorate(item);
		float quitX = item.dstPs[12] ;
		float quitY = item.dstPs[13] ;
		
		item.quitMatrix.reset();
		item.quitMatrix.postTranslate(quitX-item.quitRect.width()/2,quitY-item.quitRect.height()/2);
		item.quitMatrix.postRotate(item.ratote+item.ratoteHistory,quitX,quitY);
		item.quitMatrix.mapRect(item.dst_quitRect, item.quitRect);
		return item.dst_quitRect;
	}
	private Matrix getQuitMatrix(MoveAttri item){
		getQuitRectF(item);
		return 	item.quitMatrix;
	}
	private RectF getMainRect_WithRorate(MoveAttri item){
	
		float scaleRadioTotal=item.scaleRadio*item.scaleRadio_history;
		float rorateTotal=item.ratote+item.ratoteHistory;
		item.oriMatrix.reset();
		item.oriMatrix.postScale(scaleRadioTotal, scaleRadioTotal, item.rect_img_widget.centerX(), item.rect_img_widget.centerY());
		item.oriMatrix.postRotate(rorateTotal,item.rect_img_widget.centerX(), item.rect_img_widget.centerY());
		item.oriMatrix.postTranslate(item.offsetX+item.offsetX_history+1F*width/2-item.rect_img_widget.width()/2, 
				item.offsetY+item.offsetY_history+1F*height/2-item.rect_img_widget.height()/2);
		
		item.oriMatrix.mapRect(item.dst_rect_img_widget, item.rect_img_widget);
		item.oriMatrix.mapPoints(item.dstPs, item.srcPs);
		return item.dst_rect_img_widget;
	}
	private Matrix getMainMaritx_withRorate(MoveAttri item){
		getMainRect_WithRorate(item);
		return item.oriMatrix;
	}
	
	private void removeLabel_Bitmap(MoveAttri moveAttri){
		if (moveAttri.bitmap!=null) {
			boolean deleteIsOk = true;
			for (MoveAttri item : moveAttri_list) {
				if (item == moveAttri) {
					continue;
				}
				if (item.bitmap == moveAttri.bitmap) {
					deleteIsOk = false;
				}
			}
			if (deleteIsOk) {
				moveAttri.bitmap.recycle();
				moveAttri.bitmap=null;
			}
			moveAttri_list.remove(moveAttri);
		}
	}
	public void removeAllLabels(){
		for (MoveAttri item : moveAttri_list) {
			removeLabel_Bitmap(item);
		}
	}
	
	public LabelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 scaleBm=BitmapFactory.decodeResource(context.getResources(), R.drawable.fangda);
		 quitBm=BitmapFactory.decodeResource(context.getResources(), R.drawable.guanbi);
		
		 
	}

	public LabelView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public LabelView(Context context) {
		this(context, null);
	}
	private MoveAttri tempTouch=null ;
	private MoveAttri showHelper=null ;
	private boolean quitDown=false;//�����¼�  �ж������õ�
	
	private float[] getInvertEventPoint(MoveAttri moveAttri,MotionEvent event,Type type){
		float[] src=new float[]{event.getX(),event.getY()};
		float[] dst=new float[src.length];
		switch (type) {
		case MAIN:
			getMainMaritx_withRorate(moveAttri).invert(moveAttri.invert_Matrix);
			moveAttri.invert_Matrix.mapPoints(dst, src);
			break;
		case SCALE:
			getScaleMatrix(moveAttri).invert(moveAttri.invert_Matrix);
			moveAttri.invert_Matrix.mapPoints(dst, src);
			break;
		case QUIT:
			getQuitMatrix(moveAttri).invert(moveAttri.invert_Matrix);
			moveAttri.invert_Matrix.mapPoints(dst, src);
			break;
		default:
			break;
		}
	
//		System.out.println("down :x"+event.getX()+"\ty"+event.getY());
//		System.out.println("����� x:"+dst[0]+"\ty:"+dst[1]);
		return dst;
	}
	private enum Type{
		MAIN,SCALE,QUIT
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:// ��ָѹ����Ļ
		
			//���ж� �������Ǹ�ͼƬ
			for (int i = moveAttri_list.size() - 1; i >= 0; i--) {
				MoveAttri temp = moveAttri_list.get(i);
				float[] main_invertEventPoint = getInvertEventPoint(temp, event,Type.MAIN);
				if(temp.rect_img_widget.contains(main_invertEventPoint[0], main_invertEventPoint[1])){
					System.out.println("������ǩ�� index:"+i);
					mode = Mode.DRAG;
					//��¼һЩ���� ���ʼһЩֵ
					temp.downX=event.getX();
					temp.downY=event.getY();
					tempTouch=temp;
					showHelper=temp;
					
					break;
				}
				if(showHelper!=null&&showHelper==temp){
					//����֪����������ʱ��  �Ƿ����ŵ�λ�������Ͻǻ������½�
					float[] scale_invertEventPoint = getInvertEventPoint(temp, event,Type.SCALE);
					float[] quit_invertEventPoint = getInvertEventPoint(temp, event,Type.QUIT);
					 if(temp.scaleRect.contains(scale_invertEventPoint[0],scale_invertEventPoint[1])){
						 mode = Mode.ZOOM;
						 tempTouch=showHelper;
						 tempTouch.oriDis = getHalf_Length_RectfDiagonal(getMainRect_WithRorate(showHelper), event);
						 tempTouch.oriRatote=getRotateDegrees_centerToRight(getMainRect_WithRorate(showHelper), event);
						 System.out.println("����you�ǵĵ���");
					 }
					 if(temp.quitRect.contains(quit_invertEventPoint[0],quit_invertEventPoint[1])){
						 quitDown=true;
						 System.out.println("�������½ǵĵ���");
						 mode=Mode.NONE;
						 removeLabel_Bitmap(showHelper);
						 tempTouch=null;
						 showHelper=null;
					 }
					 break;
				}
			}
			
			
			if(tempTouch==null)
				showHelper=null;
			invalidate();
			break;
			
//		case MotionEvent.ACTION_POINTER_DOWN:// ����Ļ�ϻ��д��㣨��ָ��������һ����ָѹ����Ļ
//			mode = Mode.ZOOM;
//			break;

		case MotionEvent.ACTION_MOVE:// ��ָ����Ļ�ƶ����� �¼��᲻�ϵش���
			if (tempTouch != null) {
				if (mode == Mode.DRAG) {
					tempTouch.offsetX = event.getX() - tempTouch.downX;
					tempTouch.offsetY = event.getY() - tempTouch.downY;

				} else if (mode == Mode.ZOOM) {// ��������ת
					//����
					float tempDis = getHalf_Length_RectfDiagonal(getMainRect_WithRorate(showHelper), event);
					tempTouch.scaleRadio=tempDis/tempTouch.oriDis;
					//��ת
					float tempRatote=getRotateDegrees_centerToRight(getMainRect_WithRorate(showHelper), event);
					tempTouch.ratote=tempRatote-tempTouch.oriRatote;
//					System.out.println("�����Ƕ��٣�"+	tempTouch.scaleRadio);
//					System.out.println("�Ƕ��Ƕ��٣�"+tempTouch.ratote);
				}
			}
			invalidate();
			break;

		case MotionEvent.ACTION_UP:// ��ָ�뿪��
			mode = Mode.NONE;
			if(tempTouch!=null){
				//��¼һЩֵ
				tempTouch.offsetX_history += tempTouch.offsetX;
				tempTouch.offsetX=0;
				tempTouch.offsetY_history += tempTouch.offsetY;
				tempTouch.offsetY=0;
				tempTouch.scaleRadio_history*=tempTouch.scaleRadio;
				tempTouch.scaleRadio=1F;
				tempTouch.ratoteHistory+=tempTouch.ratote;
				tempTouch.ratote=0;
				tempTouch=null;
			}
			break;
//		case MotionEvent.ACTION_POINTER_UP:// ����ָ�뿪��Ļ,����Ļ���д��㣨��ָ��
//			mode = Mode.NONE;
//			break;
		default:
			break;
		}
		if (!quitDown) {
			if (tempTouch != null) {
				//�㵽�˾�����  ��������
				return true;
			} else {
				return false;
			}
		}else{
			//�����ʱ�����ĵ�  ���ĵ�ʱ��ԭ��
			quitDown=false;
			return true;
		}
	}
	private static float getHalf_Length_RectfDiagonal(RectF rectf,MotionEvent event){
		PointF start=new PointF(rectf.centerX(),rectf.centerY());
		PointF end=new PointF(event.getX(), event.getY());
		return DragZoomRorateUtils.distance(start, end);
	}
	private static float getRotateDegrees_centerToRight(RectF rectf,MotionEvent event){
		PointF start=new PointF(rectf.centerX(),rectf.centerY());
		PointF end=new PointF(event.getX(), event.getY());
		return 	(float)(Math.toDegrees(Math.atan2(end.y-start.y, end.x-start.x)));
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width=getMeasuredWidth();
		height=getMeasuredHeight();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		for (MoveAttri item : moveAttri_list) {
//			item.toString();
//			System.out.println("λ�ã�"+moveAttri_list.indexOf(item)+"\tx:"+item.getReallyLocation_X()+"\t y:"+ item.getReallyLocation_Y());
			//ԭͼ�Ĳ���
			canvas.drawBitmap(item.bitmap,getMainMaritx_withRorate(item), null);
			if(showHelper!=null&&item==showHelper){
				Paint paint=new Paint();
				paint.setColor(Color.WHITE);
				paint.setStrokeWidth(3);
				paint.setStyle(Style.STROKE);
				if (rimVisibility) {
					//TODO ��ɫ���
					RectF tempRectF = getMainRect_WithRorate(item);
					canvas.drawRect(tempRectF, paint);
				}
				/** 
				 * 0Ϊ00
				 * 0---1---2 
				 * |       | 
				 * 7   8   3 
				 * |       | 
				 * 6---5---4  
				 */ 
				getMainRect_WithRorate(item);
				float[] dstPs = item.dstPs;
				canvas.drawLine(dstPs[0], dstPs[1], dstPs[4], dstPs[5], paint);
				canvas.drawLine(dstPs[4], dstPs[5], dstPs[8], dstPs[9], paint);
				canvas.drawLine(dstPs[8], dstPs[9], dstPs[12], dstPs[13], paint);
				canvas.drawLine(dstPs[12], dstPs[13], dstPs[0], dstPs[1], paint);
				//�Ŵ�ť
				canvas.drawBitmap(scaleBm, getScaleMatrix(item), null);
				//ɾ����ť
				canvas.drawBitmap(quitBm,getQuitMatrix(item), null);	
			}
		}
		super.onDraw(canvas);
	}
	public void hideHelper(){
		showHelper=null;
		invalidate();
	}
	public Bitmap save(){
		Bitmap save = Bitmap.createBitmap( width, height, Config.ARGB_8888);
		Canvas canvas=new Canvas(save);
		for (MoveAttri item : moveAttri_list) {
			canvas.drawBitmap(item.bitmap,getMainMaritx_withRorate(item), null);
		}
		return save;
	}
	public int  getSize(){
		return moveAttri_list.size();
	}
	/** 
	 * 0Ϊ00
	 * 0---1---2 
	 * |       | 
	 * 7   8   3 
	 * |       | 
	 * 6---5---4  
	 */ 
	public void addBitmap(Bitmap bt){
		//����map��ʱ�򲼾��Ѿ�����
		MoveAttri moveAttri=new MoveAttri();
		//λͼ
		moveAttri.bitmap=bt;
		float mainBmpWidth = 1F*bt.getWidth();
		float mainBmpHeight = 1F*bt.getHeight();
		
		//�����λ��
		moveAttri.centerLeft_first=1F*width/2-bt.getWidth()/2;
		moveAttri.centerTop_first=1F*height/2-bt.getHeight()/2;
		
		moveAttri.srcPs = new float[]{  
				 0,0,   
                 mainBmpWidth/2,0,   
                 mainBmpWidth,0,   
                 mainBmpWidth,mainBmpHeight/2,  
                 mainBmpWidth,mainBmpHeight,   
                 mainBmpWidth/2,mainBmpHeight,   
                 0,mainBmpHeight,   
                 0,mainBmpHeight/2,   
                 mainBmpWidth/2,mainBmpHeight/2  
	        }; 
		moveAttri.dstPs=new float[moveAttri.srcPs.length];
		//��¼ Rect 
		moveAttri.scaleRect.set(0, 0, 
				scaleBm.getWidth(),1F* scaleBm.getHeight());
		moveAttri.quitRect.set(0,0,
				quitBm.getWidth(), quitBm.getHeight());
		moveAttri.rect_img_widget.set(0,0,
				 bt.getWidth(), bt.getHeight());
		
		moveAttri_list.add(moveAttri);
		invalidate();
	}
	//rim�߿�
	public void setRimVisibility(boolean rim){
		rimVisibility=rim;
	}

}
