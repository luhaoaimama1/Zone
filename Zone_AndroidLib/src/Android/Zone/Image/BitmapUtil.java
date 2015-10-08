package Android.Zone.Image;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
	//����ĳЩ�ֻ����սǶ���ת������
//	public static String compressImage(Context context,String filePath,String fileName,int q) throws FileNotFoundException {
//
//        Bitmap bm = getSmallBitmap(filePath);
//
//        int degree = readPictureDegree(filePath);
//
//        if(degree!=0){//��ת��Ƭ�Ƕ�
//            bm=rotateBitmap(bm,degree);
//        }
//
//        File imageDir = SDCardUtils.getImageDir(context);
//
//        File outputFile=new File(imageDir,fileName);
//
//        FileOutputStream out = new FileOutputStream(outputFile);
//
//        bm.compress(Bitmap.CompressFormat.JPEG, q, out);
//
//        return outputFile.getPath();
//    }
//      �ж���Ƭ�Ƕ�
//	public static int readPictureDegree(String path) {
//        int degree = 0;
//        try {
//            ExifInterface exifInterface = new ExifInterface(path);
//            int orientation = exifInterface.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                degree = 90;
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                degree = 180;
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                degree = 270;
//                break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return degree;
//    }
	
	/**
	 * 
	 * @param bitmap   λͼ
	 * @param degress  Ҫ��ת�ĽǶ�
	 * @return   ��ת��Ƭ
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
	        if (bitmap != null) {
	            Matrix matrix = new Matrix();
	          //��תͼƬ ����   
	            matrix.postRotate(degress); 
	            // �����µ�ͼƬ   
	            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
	                    bitmap.getHeight(), matrix, true);
	            return bitmap;
	        }
	        return null;
	    }
	/**
	 * 
	 * @param bitmap   λͼ
	 * @param sx	���������
	 * @param sy	���������
	 * @return   ��ת��Ƭ
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap,float sx,float sy) {
		if (bitmap != null) {
			Matrix matrix = new Matrix();
			//��תͼƬ ����   
			matrix.postScale(sx,sy);
			// �����µ�ͼƬ   
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			return bitmap;
		}
		return null;
	}
}
