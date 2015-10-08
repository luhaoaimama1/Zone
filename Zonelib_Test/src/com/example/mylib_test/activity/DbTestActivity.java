package com.example.mylib_test.activity;

import com.example.mylib_test.R;
import entity.DbEntity;
import Android.Zone.Sqlite.Sqlite_Utils;
import Android.Zone.Sqlite.Sqlite_Utils.Transaction;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class DbTestActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_dbtest);
	}
	@Override
	public void onClick(View v) {
		Toast.makeText(this, "aaa", 1);
		System.out.println("gansha");
		if(v.getId()==R.id.db_query){
			Sqlite_Utils.getInstance(this).queryAllByClass(DbEntity.class);
//			Sqlite_Utils.getInstance(this).queryEntityById(DbEntity.class,"1");
//			Sqlite_Utils.getInstance(this).queryEntitysByCondition(DbEntity.class, "where id=?", new String[]{"1"});
			return ;
		}
		DbEntity zeng=new DbEntity();
		zeng.setAge("I");
		zeng.setId("LOVE");
		zeng.setSj("you");
		zeng.setName("!");
//		DbEntity update = Sqlite_Utils.getInstance(this).queryEntityById(DbEntity.class,"2");
//		if(update!=null){
//			update.setSj("Forever");
//		}
		switch (v.getId()) {
		case R.id.db_create:
//			Sqlite_Utils.getInstance(this).addUpgradeListener(new OnUpgrade() {
//			
//			@Override
//			public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
//				Sqlite_Utils.getInstance(MainActivity.this).addColumn(DbEntity.class, "sbshibu");
//			}
//		});
//		Sqlite_Utils.getInstance(MainActivity.this).addColumn(DbEntity.class, "sbshibu");
//		Sqlite_Utils.getInstance(MainActivity.this).updateOrDeleteColumn(DbEntity.class, new String[]{"he"}, new String[]{"hexihuan"});
		Sqlite_Utils.getInstance(this).createTableByEntity(DbEntity.class);
//		Sqlite_Utils.getInstance(this).createTableByEntity(MbKb.class);
//		Sqlite_Utils.getInstance(this).queryColumnNamesByEntity(DbEntity.class);
//		Sqlite_Utils.getInstance(this).queryColumnNamesByEntity(MbKb.class);
			break;
		case R.id.db_add:
//			Sqlite_Utils.getInstance(MainActivity.this).addColumn(DbEntity.class, "sbshibu");
			Sqlite_Utils.getInstance(this).addEntity(zeng);	
			break;
		case R.id.db_update:
//			Sqlite_Utils.getInstance(this).UpdateEntity(db);
//			//ÃÌº”≤‚ ‘
//			Sqlite_Utils.getInstance(this).addOrUpdateEntity(zeng);
//			//–ﬁ∏ƒ≤‚ ‘
//			Sqlite_Utils.getInstance(this).addOrUpdateEntity(update);
			Transaction tran= Sqlite_Utils.getInstance(this).getTransaction();
			tran.beginTransaction();
			for (int i = 0; i < 10; i++) {
//				if(i==5)
//					throw new NullPointerException();
				tran.addEntity_Transaction(zeng);
			}
			tran.endTransaction();
			
//			Sqlite_Utils.getInstance(this).UpdateByCondition(DbEntity.class, " set name =? " , new String[]{"pangci"});
//			Sqlite_Utils.getInstance(this).UpdateByCondition(DbEntity.class, " set name =? " , new String[]{"pangci"});
			
			break;
		case R.id.db_delete:
//			Sqlite_Utils.getInstance(this).removeEntity(update);
//			Sqlite_Utils.getInstance(this).removeEntityByCondition(DbEntity.class, "where id < ?", new String[]{5});
			Sqlite_Utils.getInstance(this).removeAllByClass(DbEntity.class);
			
			break;

		default:
			break;
		}
	}

}
