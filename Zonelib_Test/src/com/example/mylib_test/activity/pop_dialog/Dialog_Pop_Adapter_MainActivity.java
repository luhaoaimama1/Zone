package com.example.mylib_test.activity.pop_dialog;

import com.example.mylib_test.R;
import com.example.mylib_test.activity.pop_dialog.pop.Pop_Photo;

import Zone.CustomView.DialogCustemZone;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Dialog_Pop_Adapter_MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_pop_dialog_adapter);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop:
//			PopCus pc=new PopCus(this, R.layout.poptest);
//			pc.showPop();
			Pop_Photo pop=new Pop_Photo(this, R.layout.popwindow_phone, R.id.flowLayoutZone1, R.id.ll_cancelId);
			pop.show();
			break;
		case R.id.dialog:
			//dialog�Զ������
			DialogCustemZone dcz=new DialogCustemZone(this) {
				
				@Override
				public void notSure() {
					toToast("���is not OK!");
				}
				
				@Override
				public void isSure() {
					toToast("���is OK,���ƣ�");
				}

				@Override
				public void addSetProperty(Builder db) {
					db.setIcon(R.drawable.ic_launcher);
				}
				
			};
			break;
		case R.id.adapter:
			//myAdatper����
			startActivity(new Intent(this, AdapterActivity.class));
			break;
		case R.id.textGaoLiang:
			//������ָ�����Ч��
			Button bt = (Button) findViewById(R.id.textGaoLiang);
			Spannable span = new SpannableString("I am what are you doing��fucking!");
			span.setSpan(new AbsoluteSizeSpan(20), 1, 16,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//�����С
			span.setSpan(new ForegroundColorSpan(Color.RED), 5, 16,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//ǰ��ɫ
			span.setSpan(new BackgroundColorSpan(Color.YELLOW), 11, 16,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//����ɫ
			bt.setText(span);
//			bt.setText(Html.fromHtml(  "<font color=#E61A6B>��ɫ����</font> "+
//			"<I><font color=#1111EE>��ɫб�����</font></I>"+"<u><i><font color=#1111EE>��ɫб��Ӵ����»��ߴ���</font></i></u>"));
			break;

		default:
			break;
		}
	}
	public void toToast(String str) {
		Toast.makeText(this, str, 1).show();
	}
}
