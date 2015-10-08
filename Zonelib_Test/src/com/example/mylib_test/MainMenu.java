package com.example.mylib_test;

import java.util.ArrayList;
import java.util.List;

import com.example.mylib_test.activity.AnimalTestActivity;
import com.example.mylib_test.activity.CustomViewTestActivity;
import com.example.mylib_test.activity.DbTestActivity;
import com.example.mylib_test.activity.Dialog_Pop_AdapterTestActivity;
import com.example.mylib_test.activity.FileTestActivity;
import com.example.mylib_test.activity.HttpTestActivity;
import com.example.mylib_test.activity.Photo_ShotTestActivity;
import com.example.mylib_test.activity.ThirdParty_TestActivity;
import com.example.mylib_test.activity.UtilsActivity;
import com.example.mylib_test.activity.Wifi3gTestActivity;
import com.example.mylib_test.entity.MenuEntity;

public class MainMenu {
	public static List<MenuEntity> menu=new ArrayList<MenuEntity>();
	
	static{
		menu.add(new MenuEntity("手机 文件/IO 测试", FileTestActivity.class)) ;
		menu.add(new MenuEntity("wifi 3g测试", Wifi3gTestActivity.class)) ;
		menu.add(new MenuEntity("db测试", DbTestActivity.class)) ;
		menu.add(new MenuEntity("http测试(和handle的测试)", HttpTestActivity.class)) ;
		menu.add(new MenuEntity("pop dialog测试(还有 文字高亮 链接等效果)", Dialog_Pop_AdapterTestActivity.class)) ;
		menu.add(new MenuEntity("照片和拍摄(图片裁剪等辅助测试)", Photo_ShotTestActivity.class)) ;
		menu.add(new MenuEntity("第三方 （ImageLoader等）", ThirdParty_TestActivity.class)) ;
		menu.add(new MenuEntity("动画(属性动画 和 正常动画(颜色过滤、画布等测试))", AnimalTestActivity.class)) ;
		menu.add(new MenuEntity("自定义控件", CustomViewTestActivity.class)) ;
		menu.add(new MenuEntity("工具箱Utils的测试", UtilsActivity.class)) ;
	}

}
