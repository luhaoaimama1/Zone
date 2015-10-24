package com.example.mylib_test.app.config;

import java.util.ArrayList;
import java.util.List;

import Android.Zone.Sqlite.Sqlite_Utils;
import Android.Zone.Sqlite.Sqlite_Utils.OnCreate;
import Android.Zone.Sqlite.Sqlite_Utils.OnUpgrade;
import android.content.Context;

import com.example.mylib_test.activity.db.entity.DbEntity;
import com.example.mylib_test.activity.db.entity.MbKb;
public class SqliteConfig {
	@SuppressWarnings("rawtypes")
	public static List<Class> classList=new ArrayList<Class>();
	static{
		classList.add(DbEntity.class);
		classList.add(MbKb.class);
	}

	public static void initSqlite(final Context context){
		Sqlite_Utils.setPrintLog(true);
		Sqlite_Utils.init_listener(context,new OnCreate() {
			@Override
			public void onCreateTable(Sqlite_Utils instance) {
				System.out.println("������");
				//Ϊʲô��ôд�� ��Ϊ lib���� Ҳ��sqlitConfig ��list��ͬ
				instance.createTableByList(classList);
			}
		},new OnUpgrade() {
			@Override
			public void onUpgrade(int oldVersion, int newVersion,
					Sqlite_Utils instance2) {
				System.err.println("oldVersion:" + oldVersion);
				System.err.println("newVersion:" + newVersion);
				 instance2.updateLengthOrAddColumn_Auto(DbEntity.class);
			
			}
			@Override
			public void annoColumn_DeleOrUpdate(Sqlite_Utils instance2) {
				instance2.updateAnnoColumn(DbEntity.class, "danteng", "");
			}
		});
	}
}
