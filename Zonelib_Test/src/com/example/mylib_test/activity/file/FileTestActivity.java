package com.example.mylib_test.activity.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.example.mylib_test.R;

import Android.Zone.Log.LogUtils;
import Android.Zone.Log.ToastUtils;
import Android.Zone.SD.FileUtils_SD;
import Android.Zone.SD.SdSituation;
import Java.Zone.IO.IOUtils;
import Java.Zone.Log.PrintUtils;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class FileTestActivity extends Activity implements OnClickListener{
//	static{
//		LogUtils.closeWriteLog();
//		PrintUtils.closeWritePrint();
//	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_filetest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create:
			//����Ŀ¼����
			 File file2 = FileUtils_SD.FolderCreateOrGet("mammama", "mma", "heihei","bbcc.txt");
			 Log.e("xihuan",file2.getAbsolutePath() );
			break;
		case R.id.delete:
			if (FileUtils_SD.FolderCreateOrGet("mammama", "mma", "heihei").delete()) 
				//ɾ���ļ�����
				ToastUtils.showLong(this, "ɾ���ɹ�");
			else
				ToastUtils.showLong(this, "ɾ��ʧ��");
			break;
		case R.id.readFile:
			//�õ�bbcc.txt����ļ� ��ȡ�ļ�
			File file = new File(FileUtils_SD.FolderCreateOrGet(""), "bbcc.txt");
			String text="�ҷǳ���,��˵�أ�";
			InputStream in=null;
			try {
				in = new ByteArrayInputStream(text.getBytes("GBK"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			IOUtils.write(file,in);
			Log.e("xihuan", IOUtils.read(file, "GBK"));
			break;
		case R.id.writeFileByFile:
			//�����ݸ��Ƶ���һ��·�� ���ļ�aabb.txt
			File inFile = new File(FileUtils_SD.FolderCreateOrGet(""), "bbcc.txt");
			File outFile = new File(FileUtils_SD.FolderCreateOrGet(""), "aabb.txt");
			ToastUtils.showLong(this, IOUtils.write(outFile, inFile, false) == true ? "���Ƴɹ�": "����ʧ��");
			break;
		case R.id.sd:
			//���� ����1000mb SD�Ƿ���
			System.out.println("ri");
			SdSituation.IsSDspaceEnough(this, "1000mb");
			break;
		default:
			break;
		}
	}

}
