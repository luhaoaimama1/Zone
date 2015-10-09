package com.example.mylib_test.activity;

import java.util.List;

import Android.Zone.Sqlite.Sqlite_Utils;
import Android.Zone.Sqlite.Sqlite_Utils.Transaction;
import Java.Zone.Log.PrintUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mylib_test.R;
import com.example.mylib_test.entity.DbEntity;

public class DbTestActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_dbtest);
	}
	@Override
	public void onClick(View v) {
		DbEntity zeng=new DbEntity();
		zeng.setAge("I");
		zeng.setSj("you");
		zeng.setName("!");
		
		onClickTable(v,zeng);
		onClickAdd( v,zeng);
		onClickQuery( v,zeng);
		onClickUpdate( v,zeng);
		onClickDelete( v,zeng);
	}
	private void onClickDelete(View v, DbEntity zeng) {
		switch (v.getId()) {
		case R.id.db_delete:
			Sqlite_Utils.getInstance(this).removeEntity(zeng);
			break;
		case R.id.db_deleteAll:
			Sqlite_Utils.getInstance(this).removeAllByClass(DbEntity.class);	
			break;
		case R.id.db_deleteByC:
			Sqlite_Utils.getInstance(this).removeEntityByCondition(DbEntity.class, "where _id_ > ?", new String[]{"5"});
			break;

		default:
			break;
		}
	}
	private void onClickUpdate(View v, DbEntity zeng) {
		switch (v.getId()) {
		case R.id.db_update:
			zeng.setId("1");
			zeng.setName("pangci��˵");
//			//���Ӳ���
			Sqlite_Utils.getInstance(this).addOrUpdateEntity(zeng);
			break;
		case R.id.db_updateByC:
			Sqlite_Utils.getInstance(this).UpdateByCondition(DbEntity.class, " set _name_ =? " , new String[]{"pangci"});
			break;

		default:
			break;
		}
	}
	private void onClickQuery(View v, DbEntity zeng) {
		switch (v.getId()) {
		case R.id.db_query:
			Sqlite_Utils.getInstance(this).queryAllByClass(DbEntity.class);
//			Sqlite_Utils.getInstance(this).queryEntityById(DbEntity.class,"1");
			break;
		case R.id.db_queryByC:
			List<DbEntity> temp = Sqlite_Utils.getInstance(this).queryEntitysByCondition(DbEntity.class, "where _id_ in(?,?)", new String[]{"1","3"});
			break;

		default:
			break;
		}
	}
	private void onClickAdd(View v, DbEntity zeng) {
		switch (v.getId()) {
		case R.id.db_add:
//			Sqlite_Utils.getInstance(MainActivity.this).addColumn(DbEntity.class, "sbshibu");
			Sqlite_Utils.getInstance(this).addEntity(zeng);
			break;
		case R.id.db_addOrUpdateEntity:
			Sqlite_Utils.getInstance(this).addOrUpdateEntity(zeng);
			break;
		case R.id.tran_add:
			Transaction tran= Sqlite_Utils.getInstance(this).getTransaction();
			tran.beginTransaction();
			for (int i = 0; i < 10; i++) {
//				if(i==5)
//					throw new NullPointerException();
				tran.addEntity_Transaction(zeng);
			}
			tran.endTransaction();
			break;	
		default:
			break;
		}
	}
	private void onClickTable(View v, DbEntity zeng) {
		switch (v.getId()) {
		case R.id.db_create:
//			Sqlite_Utils.getInstance(MainActivity.this).addColumn(DbEntity.class, "sbshibu");
				//�޸��ֶ� ����ɾ���ֶ�
//			Sqlite_Utils.getInstance(MainActivity.this).updateOrDeleteColumn(DbEntity.class, new String[]{"he"}, new String[]{"hexihuan"});
			Sqlite_Utils.getInstance(this).createTableByEntity(DbEntity.class);
//			Sqlite_Utils.getInstance(this).createTableByEntity(MbKb.class);
			break;
	
		case R.id.db_Tabledelete:
			Sqlite_Utils.getInstance(this).dropTableByClass(DbEntity.class);
			break;
		case R.id.db_queryColumn:
			Sqlite_Utils.getInstance(this).queryColumnNamesByEntity(DbEntity.class);
			break;
			
		default:
			break;
		}
	}

}
