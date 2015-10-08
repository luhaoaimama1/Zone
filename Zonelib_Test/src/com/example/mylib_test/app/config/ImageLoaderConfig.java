package com.example.mylib_test.app.config;

import java.io.File;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class ImageLoaderConfig {
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
