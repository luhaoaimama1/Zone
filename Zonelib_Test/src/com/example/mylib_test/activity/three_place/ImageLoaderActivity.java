package com.example.mylib_test.activity.three_place;

import com.example.mylib_test.R;
import com.example.mylib_test.R.drawable;
import com.example.mylib_test.R.id;
import com.example.mylib_test.R.layout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ImageLoaderActivity extends Activity{
	private ListView lv;
	private String[] imageThumbUrls;
	private DisplayImageOptions options;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageloader);
		lv=(ListView) findViewById(R.id.listView);
		imageThumbUrls=Images.imageThumbUrls;
		lv.setAdapter(new ImageBaseAdapter());
		// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions
		
//		options = new DisplayImageOptions.Builder().build();
		options = new DisplayImageOptions.Builder()
				// ����ͼƬ�����ڼ���ʾ��ͼƬ  ���Բ����þ��ǿհױ�
//				.showImageOnLoading(R.drawable.ic_stub)
				// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.ic_empty)
				// ����ͼƬ���ػ��������з���������ʾ��ͼƬ	
				.showImageOnFail(R.drawable.ic_error)
				// �����Ƿ�View�ڼ���ǰ��λ
//				.resetViewBeforeLoading(true)
				//����������������ǰ���ӳ�ʱ�䡣Ĭ��-���ӳ١�
//				.delayBeforeLoading(1000)
				/**
				   ����ͼƬ���ŷ�ʽ
				 EXACTLY: ͼ����ȫ��������С��Ŀ���С
				 EXACTLY_STRETCHED: ͼƬ�����ŵ�Ŀ���С
				 IN_SAMPLE_INT: ͼ�񽫱����β�����������
				 IN_SAMPLE_POWER_OF_2: ͼƬ������2����ֱ����һ���ٲ��裬ʹͼ���С��Ŀ���С
				 NONE: ͼƬ�������
				 */
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) 
				// ����ͼƬ�ı����ʽΪRGB_565���˸�ʽ��ARGB_8888��
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheInMemory(true)
				// �������ص�ͼƬ�Ƿ񻺴���SD����
				.cacheOnDisk(true)
				//�����Ƿ�imageloader��JPEGͼ���EXIF��������ת����ת��
				.considerExifParams(true)
				//�����Զ�����ʾ��    ��������Բ��,����Ҫ��ɾ��
				.displayer(new RoundedBitmapDisplayer(20))
				.build();
	};
	
	private class ImageBaseAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageThumbUrls.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return imageThumbUrls[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView==null){
				 vh=new ViewHolder();
				convertView=LayoutInflater.from(ImageLoaderActivity.this).inflate(R.layout.imageitem, null);
				ImageView iv=(ImageView) convertView.findViewById(R.id.iv);
				vh.iv=iv;
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(imageThumbUrls[position], vh.iv, options,new SimpleImageLoadingListener(){
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					 fadeInDisplay((ImageView)view, loadedImage);
				}
			
				private void fadeInDisplay(ImageView view, Bitmap loadedImage) {
					final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(android.R.color.transparent);
					final TransitionDrawable transitionDrawable =
			                new TransitionDrawable(new Drawable[]{
			                        TRANSPARENT_DRAWABLE,
			                        new BitmapDrawable(view.getResources(), loadedImage)
			                });
					view.setImageDrawable(transitionDrawable);
			        transitionDrawable.startTransition(500);
				}
			});
//			ImageLoader.getInstance().displayImage(imageThumbUrls[position], vh.iv);
			return convertView;
		}
		public class ViewHolder{
			ImageView iv;
		}
	}
}
