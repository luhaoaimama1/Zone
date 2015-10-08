﻿package Android.Zone.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
/**
 * 图片采样工具 bitmap保存工具
 * @author Zone
 *
 */
public class Compress_Sample_Utils {
	/**
	 * @param filePath  仅仅解析边界的路径
	 * @return 仅仅解析边界的Options
	 */
	public static Options justDecodeBounds(String filePath) {
		Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		return opts;
	}

	// 
	/**
	 * @param opts   传入一个仅仅解析原图边界的Options 就是已经计算出本身宽高的optoions
	 * @param targetWidth	为null的时候 以 height为标准
	 * @param targetHeight 为null的时候 以 width为标准
	 * @return 计算图片的采样值
	 */
	public static int calculateInSampleSize(BitmapFactory.Options opts,
			Integer targetWidth, Integer targetHeight) {
		int simpleScale = 0;
		if (targetWidth == null) {
			simpleScale = opts.outHeight / targetHeight;
		}
		if (targetHeight == null) {
			simpleScale = opts.outWidth / targetWidth;
		} else {
			float h_scale = opts.outHeight / targetHeight;
			float w_scale = opts.outWidth / targetWidth;
			System.out.println("横向缩放比：h_scale:" + h_scale);
			System.out.println("总想缩放比：w_scale" + w_scale);
			// 谁 缩放比例大用
			simpleScale = (int) ((h_scale > w_scale) ? h_scale : w_scale);
		}
		
		System.out.println("用的缩放比：simpleScale" + simpleScale);
		if (simpleScale <= 1) {
			// 不缩放 即原图大小
			simpleScale = 1;
		} else {
			for (int i = 0; i < 4; i++) {
				if (simpleScale < Math.pow(2, i)) {
					simpleScale =(int) Math.pow(2, i-1);
				}
			}
		}
		System.out.println("最终缩放比：simpleScale" + simpleScale);
		return simpleScale;
	}

	/**
	 *一般是640*960   480* 800  720*1280
	 * @param filePath 文件路径
	 * @param targetWidth	为null的时候 以 height为标准
	 * @param targetHeight 为null的时候 以 width为标准
	 * @return  返回 原图到目标宽高 等比缩放后的 采样位图
	 */
	public static Bitmap getSampleBitmap(String filePath,Integer targetWidth, Integer targetHeight) {
		Options options = justDecodeBounds(filePath);
		options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 
	 * @param bt	 想要压缩位图
	 * @param targetSize  目标Kb
	 * @param qualityMin  最低质量
	 * @return  压缩后的位图
	 */
	public static Bitmap compressBitmap(Bitmap bt,int targetSize,int qualityMin) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bt.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		System.out.println("压缩的时候_开始_大小："+baos.toByteArray().length / 1024*4+"kb");
		int options = 100;
		int step = 10;
		while (baos.toByteArray().length / 1024 * 4 > targetSize) {
			// 循环判断如果压缩后图片是否大于300kb 左右,大于继续压缩 经过我的实践貌似的*4
			baos.reset();// 重置baos即清空baos
			bt.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			if (options < qualityMin) {
				break;
			}
			if (options > step) {
				options -= step;// 每次都减少10
			} 
			else {
				break;
			}
		}
		System.out.println("压缩的时候_完成后_大小："+baos.toByteArray().length / 1024*4+"kb");
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 保存位图到输出的路径
	 * @param outPath  想要输出的路径
	 * @param bitmap 想要保存的bitmap
	 * @return  保存成功与否
	 */
	public static boolean saveBitmap(String outPath, Bitmap bitmap) {
		try {
			FileOutputStream fos = new FileOutputStream(outPath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
