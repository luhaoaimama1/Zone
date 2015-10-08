package Android.Zone.Features;

import java.io.IOException;
import Android.Zone.Log.ToastUtils;
import Java.Zone.Log.PrintUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;

/**
 * ��Ҫ�ķ������£� <br>
 * {@code
 *  <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.FLASHLIGHT" />
 * } <br>
 * ����Խ� ��onclick�� ��super.onclick���ݽ���
 * 
 * @author Zone
 * 
 */
public abstract class Featrue_CustomCamera extends ExtraFeature {
	public Featrue_CustomCamera(Activity activity) {
		super(activity);
	}
	// Camera.getCameraInfo(cameraIndex, cameraInfo);// �õ�ÿһ������ͷ����Ϣ
	// parameters = camera.getParameters(); // ��ȡ�������
	private static final String TAG="CustomCameraActivity";
	private  Camera camera;
	private int cameraCount, cameraNow; 
	private  SurfaceView surfaceView;
	private boolean isBehind = true, focusIsOk = false, autoFocus_ing=false;

	/**
	 * ��ʼ����Ϣ �����ȵ��˷��� ��ɶ� �������������ĳ�ʼ��
	 * 
	 * @param surfaceView
	 * @param cam
	 */
	public void initSurfaceView(SurfaceView surfaceView, Camera cam) {
		this.camera = cam;
		cameraCount=Camera.getNumberOfCameras();
		this.surfaceView = surfaceView;
		surfaceView.setOnClickListener(this);
		// holder=surfaceView.getHolder();
//		surfaceView.getHolder().setFixedSize(176, 164);
		surfaceView.getHolder().addCallback(new Callback() {
			// TODO ��ʱ����Щ ���������ʱ�� ��Ū���� ���߳��󷽷�
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				releaseCamera(camera);
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (isBehind) {
					swapCamera(0);
				} else {
					swapCamera(1);
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});
	}

	/**
	 * ��ת�����
	 */
	public void reverseCamera() {
		if (cameraCount > 1) {
			isBehind = !isBehind;
		}
		if (isBehind) {
			swapCamera(0);
		} else {
			swapCamera(1);
		}
	}

	private void swapCamera(int cameraIndex) {
		if (camera != null) {
			releaseCamera(camera);
		}
		openCamera(cameraIndex);
		cameraFocus();
	}

	/**
	 * �Խ�
	 */
	@SuppressLint("ShowToast")
	protected void cameraFocus() {
		focusIsOk = false;
		camera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				autoFocus_ing=true;
				if (success) {
					ToastUtils.showLong(activity, "�Խ��ɹ�");
					focusIsOk = true;
				}
				camera.cancelAutoFocus();
				if (!success) {
					cameraFocus();
				}
				autoFocus_ing=false;
			}
		});
	}

	private boolean openCamera(int cameraIndex) {
		camera = Camera.open(cameraIndex);
		cameraNow = cameraIndex;
		camera.setDisplayOrientation(getPreviewDegree(activity));
		try {
			camera.setPreviewDisplay(surfaceView.getHolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
		return true;
	}

	/**
	 * ����
	 */
	public void shutter() {
		if (focusIsOk) {
			camera.takePicture(null, null, pictureCallback);
		}
	}

	private PictureCallback pictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap bMapRotate;
			bMapRotate = BitmapFactory.decodeByteArray(data, 0, data.length);

			if (cameraNow == 0) { // ������ͷ��ת90��
				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.postRotate(90);
				bMapRotate = Bitmap.createBitmap(bMapRotate, 0, 0,
						bMapRotate.getWidth(), bMapRotate.getHeight(), matrix,
						true);
			} else if (cameraNow == 1) {// ǰ������ͷ���·�ת
				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.postScale(1, -1);// ���·�ת
				matrix.postRotate(-90);
				bMapRotate = Bitmap.createBitmap(bMapRotate, 0, 0,
						bMapRotate.getWidth(), bMapRotate.getHeight(), matrix,
						true);
			}
			getShutterBitMap(bMapRotate);

		}

	};

	/**
	 * @param bMapRotate
	 *            ���պ󷵻ص�λͼ
	 */
	protected abstract void getShutterBitMap(Bitmap bMapRotate);

	private static int getPreviewDegree(Activity activity) {
		// ����ֻ��ķ���
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degree = 0;
		// �����ֻ��ķ���������Ԥ������Ӧ��ѡ��ĽǶ�
		switch (rotation) {
		case Surface.ROTATION_0:
			degree = 90;
			break;
		case Surface.ROTATION_90:
			degree = 0;
			break;
		case Surface.ROTATION_180:
			degree = 270;
			break;
		case Surface.ROTATION_270:
			degree = 180;
			break;
		}
		return degree;
	}

	/**
	 * �ͷ����
	 * 
	 * @param camera
	 */
	protected void releaseCamera(Camera camera) {
		if (camera != null) {
			camera.setPreviewCallback(null);
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == surfaceView) {
			PrintUtils.print("�Խ�");
			if (!autoFocus_ing) {
				cameraFocus();
			}
		}
	}

	@Override
	public void onCreate(Bundle bundle) {
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
	}

	@Override
	public void onDestroy() {
		
	}
	
	

}
