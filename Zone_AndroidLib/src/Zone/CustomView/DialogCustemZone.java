package Zone.CustomView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public abstract class DialogCustemZone {
	private Context context;
	public boolean isSure = false;

	public DialogCustemZone(Context context) {
		this.context = context;
		this.show();
	}

	public void show() {
		AlertDialog.Builder db = new Builder(context);
		db.setTitle("��ȷ��Ҫ���д˲�����");
		addSetProperty(db);
		db.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isSure();
			}
		});
		db.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				notSure();
			}
		});
		db.create().show();
	}
	/**
	 * ��Ҫ��������Կ������ set���� 
	 * @param db 
	 */
	public abstract void addSetProperty(Builder db);
	/**
	 * ȷ���ĺ��ߵķ���
	 */
	public abstract void isSure();
	/**
	 * ȡ����ĺ��ߵķ���
	 */
	public abstract void notSure() ;
}
