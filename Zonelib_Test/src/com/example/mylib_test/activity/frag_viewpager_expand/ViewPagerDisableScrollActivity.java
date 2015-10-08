package com.example.mylib_test.activity.frag_viewpager_expand;

import java.util.ArrayList;
import java.util.List;

import com.example.mylib_test.R;

import Android.Zone.viewPager.ViewPaperDisableScroll;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ViewPagerDisableScrollActivity extends FragmentActivity {
	private ViewPaperDisableScroll vPager = null;
	private static final int TAB_COUNT = 3;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private MyImageView image1;
	private  CheckBox cb;
	List<TextView> tvlist = new ArrayList<TextView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_frag_scoll);
		Ui_init();

		vPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		// vPager.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// //vPager.getChildAt(vPager.getCurrentItem()).getLeft();
		// //String str="Touch1:";
		// //tv4.setText(str);
		// return false;
		// }
		// });
		vPager.setOnPageChangeListener(new OnPageChangeListener() {
			//���������  �Ǹ������еļ���android.support.v4.view
			@Override
			public void onPageSelected(int arg0) {
				//������ҳ   �Ǹ��ı�����
				// TODO Auto-generated method stub
				for (int i = 0; i < tvlist.size(); i++) {
					tvlist.get(i).setTextColor(Color.BLACK);
				}
				tvlist.get(arg0).setTextColor(Color.RED);

			}

			@SuppressLint("NewApi")
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				// int kuan = image1.getWidth();
				float ko = image1.getY();
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int kuan = dm.widthPixels;// ���height = dm.heightPixels ;//�߶�

				float a = (((float) (arg0)) + arg1) * kuan;
				float b = (((float) (arg0)) + arg1) / 3 * kuan;
				String str = "  1:" + String.valueOf(arg0) + "   2:"
						+ String.valueOf(arg1) + "   3:" + String.valueOf(arg2)
						+ "  ��:" + String.valueOf(kuan) + "  ��ǰ:"
						+ String.valueOf(b) + "  x:" + String.valueOf(ko);
				// tv4.setText(str);

				image1.onScrolled(b, kuan);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new Tab1();
			case 1:
				return new Tab2();
			case 2:
				return new Tab3();
			}
			return null;
		}

		@Override
		public int getCount() {
			return TAB_COUNT;
		}
	}

	// ��ʼ��
	private void Ui_init() {
		vPager = (ViewPaperDisableScroll) findViewById(R.id.viewpager);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tvlist.add(tv1);
		tvlist.add(tv2);
		tvlist.add(tv3);
		tv1.setTextColor(Color.RED);
		tv1.setOnClickListener(jt);
		tv2.setOnClickListener(jt);
		tv3.setOnClickListener(jt);
		tv4 = (TextView) findViewById(R.id.tv4);
		image1 = (MyImageView) findViewById(R.id.image_1);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int kuan = dm.widthPixels;// ���height = dm.heightPixels ;//�߶�
		image1.onScrolled(0, kuan);
		cb=(CheckBox) findViewById(R.id.cb);
		
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				vPager.setDisableScroll(isChecked);
			}
		});
		cb.performClick();
	}

	// ��� �ı�������ҳ
	public OnClickListener jt = new OnClickListener() {

		// tvlist.setTextColor(Color.RED);
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for (int i = 0; i < tvlist.size(); i++) {
				tvlist.get(i).setTextColor(Color.BLACK);
			}
			((TextView) v).setTextColor(Color.RED);

			switch (v.getId()) {
			case R.id.tv1:
				vPager.setCurrentItem(0);
				break;
			case R.id.tv2:
				vPager.setCurrentItem(1);
				break;
			case R.id.tv3:
				vPager.setCurrentItem(2);
				break;
			default:
				break;
			}

		}
	};

}
