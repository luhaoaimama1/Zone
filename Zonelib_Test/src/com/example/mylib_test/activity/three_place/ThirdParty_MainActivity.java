package com.example.mylib_test.activity.three_place;

import other_project.pinyin_sidebar.SideBarActivity;

import com.example.mylib_test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ThirdParty_MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_thirdparty);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageLoader:
			startActivity(new Intent(this, ImageLoaderActivity.class));
			break;
		case R.id.sideBar:
			startActivity(new Intent(this, SideBarActivity.class));
			break;

		default:
			break;
		}
	}

}
