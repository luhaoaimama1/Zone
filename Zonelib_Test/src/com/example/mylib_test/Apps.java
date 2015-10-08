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
	// SDCard路径
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
	Sqlite_Utils.init_listener(Apps.this,new OnCreate() {
			@Override
			public void onCreateTable(Sqlite_Utils instance) {
				// TODO Auto-generated method stub
				System.out.println("创建表");
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
	 * 内存溢出的话 可以找笔记
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		//路径是：/data/data/com.example.mylib_test/cache 要加image
		File cacheDir =new File(context.getCacheDir(),"images");
		
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		/** ==========================线程方面 =========================*/		
		// 设置显示图片线程池大小，默认为3
		config.threadPoolSize(3);
		// 设定线程等级比普通低一点
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		//设置用于加载和显示图像的任务的队列处理类型。
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		/** ==========================内存缓存  =========================*/
		//图片解码期间  中bitmap的宽高 默认是  手机宽高  
		config.memoryCacheExtraOptions(480, 800);
		// 设定内存缓存 
		config.memoryCache(new LruMemoryCache(2 * 1024 * 1024));
		//缓存到内存的最大数据
		config.memoryCacheSize(2 * 1024 * 1024); 
		//文件数量
		config.diskCacheFileCount(1000); 
		// 设定只保存同一尺寸的图片在内存
		config.denyCacheImageMultipleSizesInMemory();
		/** ==========================文件缓存  =========================*/
		//下载图片后 compress保存到文件中的 宽高
		config.diskCacheExtraOptions(480, 800, null);
		// 设定缓存的SDcard目录，UnlimitDiscCache速度最快
		config.diskCache(new UnlimitedDiskCache(cacheDir));
		// 设定缓存到SDCard目录的文件__命名!
		config.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()); 
		//缓存到文件的最大数据   50 MiB
		config.diskCacheSize(50 * 1024 * 1024); 
		
		/** ==========================超时 与log打印  =========================*/
		// 设定网络连接超时 timeout: 10s 读取网络连接超时read timeout: 60s
		config.imageDownloader(new BaseImageDownloader(context, 10000, 60000));
	
		//打印log
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
}