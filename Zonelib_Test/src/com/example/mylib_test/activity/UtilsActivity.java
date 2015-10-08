package com.example.mylib_test.activity;

import com.example.mylib_test.R;
import com.example.mylib_test.activity.frag_viewpager_expand.FramentSwitchAcitiviy;

import Android.Zone.Utils.KeyBoardUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class UtilsActivity extends Activity implements OnClickListener{
	private EditText keyboard;
	private View view1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_utils_test);
		keyBoardTest();
	
	}

	private void keyBoardTest() {
		keyboard=(EditText) findViewById(R.id.keyboard);
		view1=findViewById(R.id.view1);
		new KeyBoardUtils() {
			
			@Override
			public void openState(int keyboardHeight) {
				System.out.println("键盘：openState 高度:"+keyboardHeight);
			}
			
			@Override
			public void closeState(int keyboardHeight) {
				System.out.println("键盘：closeState 高度:"+keyboardHeight);
			}
		}.monitorKeybordState(findViewById(R.id.flowLayoutZone1));;	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.openKeyboard:
			KeyBoardUtils.openKeybord(keyboard, this);
			break;
		case R.id.closeKeyboard:
			KeyBoardUtils.closeKeybord(keyboard, this);
			break;
		case R.id.frammentSwitch:
			startActivity(new Intent(this,FramentSwitchAcitiviy.class));
			break;

		default:
			break;
		}
		
	}

}
