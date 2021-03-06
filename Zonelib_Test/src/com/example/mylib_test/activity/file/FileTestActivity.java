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
			//创建目录测试
			 File file2 = FileUtils_SD.FolderCreateOrGet("mammama", "mma", "heihei","bbcc.txt");
			 Log.e("xihuan",file2.getAbsolutePath() );
			break;
		case R.id.delete:
			if (FileUtils_SD.FolderCreateOrGet("mammama", "mma", "heihei").delete()) 
				//删除文件测试
				ToastUtils.showLong(this, "删除成功");
			else
				ToastUtils.showLong(this, "删除失败");
			break;
		case R.id.readFile:
			//得到bbcc.txt这个文件 读取文件
			File file = new File(FileUtils_SD.FolderCreateOrGet(""), "bbcc.txt");
			String text="我非常好,你说呢！";
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
			//把数据复制到另一个路径 的文件aabb.txt
			File inFile = new File(FileUtils_SD.FolderCreateOrGet(""), "bbcc.txt");
			File outFile = new File(FileUtils_SD.FolderCreateOrGet(""), "aabb.txt");
			ToastUtils.showLong(this, IOUtils.write(outFile, inFile, false) == true ? "复制成功": "复制失败");
			break;
		case R.id.sd:
			//测试 用完1000mb SD是否够用
			System.out.println("ri");
			SdSituation.IsSDspaceEnough(this, "1000mb");
			break;
		default:
			break;
		}
	}

}
