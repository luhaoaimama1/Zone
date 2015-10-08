package com.example.mylib_test;
/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/


import java.io.File;

import Android.Zone.Sqlite.Sqlite_Utils;
import Android.Zone.Sqlite.Sqlite_Utils.OnCreate;
import Android.Zone.Sqlite.Sqlite_Utils.OnUpgrade;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import entity.DbEntity;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class Apps extends Application {
	// SDCard·��
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
	Sqlite_Utils.init_listener(Apps.this,new OnCreate() {
			@Override
			public void onCreateTable(Sqlite_Utils instance) {
				// TODO Auto-generated method stub
				System.out.println("������");
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
		if (false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();
		initImageLoader(getApplicationContext());
	}

	/**
	 * �ڴ�����Ļ� �����ұʼ�
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		//·���ǣ�/data/data/com.example.mylib_test/cache Ҫ��image
		File cacheDir =new File(context.getCacheDir(),"images");
		
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		/** ==========================�̷߳��� =========================*/		
		// ������ʾͼƬ�̳߳ش�С��Ĭ��Ϊ3
		config.threadPoolSize(3);
		// �趨�̵߳ȼ�����ͨ��һ��
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		//�������ڼ��غ���ʾͼ�������Ķ��д������͡�
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		/** ==========================�ڴ滺��  =========================*/
		//ͼƬ�����ڼ�  ��bitmap�Ŀ�� Ĭ����  �ֻ����  
		config.memoryCacheExtraOptions(480, 800);
		// �趨�ڴ滺�� 
		config.memoryCache(new LruMemoryCache(2 * 1024 * 1024));
		//���浽�ڴ���������
		config.memoryCacheSize(2 * 1024 * 1024); 
		//�ļ�����
		config.diskCacheFileCount(1000); 
		// �趨ֻ����ͬһ�ߴ��ͼƬ���ڴ�
		config.denyCacheImageMultipleSizesInMemory();
		/** ==========================�ļ�����  =========================*/
		//����ͼƬ�� compress���浽�ļ��е� ���
		config.diskCacheExtraOptions(480, 800, null);
		// �趨�����SDcardĿ¼��UnlimitDiscCache�ٶ����
		config.diskCache(new UnlimitedDiskCache(cacheDir));
		// �趨���浽SDCardĿ¼���ļ�__����!
		config.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()); 
		//���浽�ļ����������   50 MiB
		config.diskCacheSize(50 * 1024 * 1024); 
		
		/** ==========================��ʱ ��log��ӡ  =========================*/
		// �趨�������ӳ�ʱ timeout: 10s ��ȡ�������ӳ�ʱread timeout: 60s
		config.imageDownloader(new BaseImageDownloader(context, 10000, 60000));
	
		//��ӡlog
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
}