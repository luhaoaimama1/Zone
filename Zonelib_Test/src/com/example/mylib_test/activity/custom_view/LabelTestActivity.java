package com.example.mylib_test.activity.custom_view;

import com.example.mylib_test.R;

import view.LabelView;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LabelTestActivity extends Activity implements OnClickListener{
	private LabelView labelView1;
	private boolean i=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_label_test);
		labelView1=(LabelView) findViewById(R.id.labelView1);
		labelView1.addBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.a1));
		labelView1.addBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.ic_launcher));
		//���ñ߿��Ƿ�����
//		labelView1.setRimVisibility(false);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.labelView1:
			if (i) {
				//����ע���� �ؼ���ontouch ò�Ƹ���������  ��Ȼ�����������û���ж� �������Ƚ���  ���� ��������
//				labelView1.addBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.a1));
//				labelView1.addBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.ic_launcher));
				i=false;
			}
			break;

		default:
			break;
		}
		
	}
	
}
