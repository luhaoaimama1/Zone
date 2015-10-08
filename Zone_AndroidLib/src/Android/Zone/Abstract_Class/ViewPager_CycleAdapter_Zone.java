package Android.Zone.Abstract_Class;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewPager_CycleAdapter_Zone<T> extends PagerAdapter implements OnPageChangeListener {
	private List<T> data = null;
	private ViewPager viewPager;
	private OnPageChangeListener_Zone listener;
	

	public ViewPager_CycleAdapter_Zone(List<T> data,ViewPager viewPager,  OnPageChangeListener_Zone listener) {
		this.data = data;
		this.viewPager=viewPager;
		if(listener!=null){
			this.listener=listener;
			this.viewPager.setOnPageChangeListener(this);
		}
	}
	public ViewPager_CycleAdapter_Zone(List<T> data,ViewPager viewPager) {
		this(data,viewPager,null);
	}
	

	public int getSize(){
		
		return data.size();
	}
	/**
	 * ����ViewPager���ܳ��ȵ�.
	 */
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	/**
	 * �ж��Ƿ�ʹ�û���, ���true, ʹ�û��� arg0 �����϶��Ķ��� arg1 �����Ķ���
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * ���ٶ��� position ���Ǳ����ٵĶ��������
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// ȡ���ڼ�����������λ��.
		int reallyPosition = position % data.size();
		// ��viewpager���Ƴ���ǰ�����Ķ���
		viewPager.removeView(getViewByList(reallyPosition));
	}

	/**
	 * ����item position �����ص�item������
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int reallyPosition = position % data.size();
		View view=getViewByList(reallyPosition);
		container.addView(view);
		return view;
	}

	/**
	 * ͨ��list���view
	 * @param position
	 * @return
	 */
	public abstract View getViewByList(int position);
	public interface OnPageChangeListener_Zone {
		public void onPageScrollStateChanged(int state);
		public void onPageScrolled(int position,float positionOffset, int positionOffsetPixels);
		public void onPageSelected(int position);
	}

	/**
	 * ��ҳ��Ĺ���״̬�ı�ʱ�ص� 
	 * 0:����״̬�����������
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		listener.onPageScrollStateChanged(state);
	}

	/**
	 * ������ʱ�ص�
	 */
	@Override
	public void onPageScrolled(int position ,float positionOffset, int positionOffsetPixels) {
		int reallyPosition = position % data.size();
		listener.onPageScrolled(reallyPosition, positionOffset, positionOffsetPixels);
	}

	/**
	 * ��ҳ�汻ѡ��ʱ�ص�
	 */
	@Override
	public void onPageSelected(int position) {
		if(ViewPagerCircle.class.isInstance(viewPager)){
			//����ҳ��ʱ�� �ǵö�ʱˢ��
			((ViewPagerCircle)viewPager).againTiming();
		}
		int reallyPosition = position % data.size();
		listener.onPageSelected(reallyPosition);
	}
}
