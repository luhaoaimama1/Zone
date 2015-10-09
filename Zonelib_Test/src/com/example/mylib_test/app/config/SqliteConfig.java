package com.example.mylib_test.app.config;

import Android.Zone.Sqlite.Sqlite_Utils;
import Android.Zone.Sqlite.Sqlite_Utils.OnCreate;
import Android.Zone.Sqlite.Sqlite_Utils.OnUpgrade;
import android.content.Context;
import com.example.mylib_test.entity.DbEntity;

public class SqliteConfig {

	public static void initSqlite(Context context){
		Sqlite_Utils.setPrintLog(true);
		Sqlite_Utils.init_listener(context,new OnCreate() {
			@Override
			public void onCreateTable(Sqlite_Utils instance) {
				System.out.println("´´½¨±í");
				instance.createTableByEntity(DbEntity.class);
			}
		},new OnUpgrade() {
			@Override
			public void onUpgrade(int oldVersion, int newVersion,
					Sqlite_Utils instance2) {
				// TODO Auto-generated method stub	
				System.err.println("oldVersion:" + oldVersion);
				System.err.println("newVersion:" + newVersion);
				instance2.dropTableByClass(DbEntity.class);
				
			}
		});
	}
}
