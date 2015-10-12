package com.example.mylib_test.activity.custom_view;

import java.util.ArrayList;

import com.example.mylib_test.R;

import Android.Zone.Abstract_Class.ViewPagerCircle;
import Android.Zone.Abstract_Class.ViewPager_CycleAdapter_Zone;
import Android.Zone.Abstract_Class.ViewPager_CycleAdapter_Zone.OnPageChangeListener_Zone;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ViewPagerActivity extends Activity implements OnPageChangeListener_Zone {
	private int[] imageResIDs;
	private String[] imageTextArray;
	private ArrayList<ImageView> imageViewList;
	private ViewGroup llPointGroup;
	private ViewPagerCircle mViewPager;
	private TextView tvImageDescription;
	private int previousPointPosition = 0; // ǰһ����ѡ�еĵ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_viewpager);
		mViewPager = (ViewPagerCircle) findViewById(R.id.viewpager);
		tvImageDescription = (TextView) findViewById(R.id.tv_image_description);
		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
		initData();
		mViewPager.setAdapter(new ViewPager_CycleAdapter_Zone<ImageView>(imageViewList,mViewPager,this) {

			@Override
			public View getViewByList(int position) {
				return imageViewList.get(position);
			}

		},1000);
		// ��ʼ��һ��, ͼƬ������, �͵��ѡ��״̬
		tvImageDescription.setText(imageTextArray[0]);

	}

	/**
	 * ׼������
	 */
	private void initData() {
		imageResIDs = new int[] { R.drawable.akb, R.drawable.b, R.drawable.c,
				R.drawable.d, R.drawable.e };

		imageTextArray = new String[] { "���������ף��ҾͲ��ܵ���", "�����ֻ��������ٳ������ϸ������˴�ϳ�",
				"���ر�����Ӱ�������", "������TV�������", "��Ѫ��˿�ķ�ɱ" };

		imageViewList = new ArrayList<ImageView>();

		ImageView iv;
		View view;
		LayoutParams params;
		for (int i = 0; i < imageResIDs.length; i++) {
			iv = new ImageView(this);
			iv.setBackgroundResource(imageResIDs[i]); // ��ͼƬ���ø�imageview�ؼ�
			imageViewList.add(iv);

			// ��LinearLayout�����һ��view����, ���ñ���Ϊ��ı���
			view = new View(this);
			params = new LayoutParams(5, 5);
			params.leftMargin = 10;
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.point_bg);
			view.setEnabled(false); // �ѵ���Ϊûѡ�е�״̬
			llPointGroup.addView(view);
		}
	}


	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position,float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		System.out.println("��ǰѡ�е���: " + position);
		// �Ѷ�Ӧpositionλ�õĵ�ѡ��, ͼƬ������Ҫ�л���positionλ����
		tvImageDescription.setText(imageTextArray[position]);

		llPointGroup.getChildAt(previousPointPosition).setEnabled(false); // ��ǰһ������Ϊδѡ��
		llPointGroup.getChildAt(position).setEnabled(true);// �ѵ�ǰ�ĵ���Ϊѡ��

		previousPointPosition = position; // �ѵ�ǰ�ĵ���ǰһ����ı�����¼����

	}
}
