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
		menu.add(new MenuEntity("�ֻ� �ļ�/IO ����", FileTestActivity.class)) ;
		menu.add(new MenuEntity("wifi 3g����", Wifi3gTestActivity.class)) ;
		menu.add(new MenuEntity("db����", DbTestActivity.class)) ;
		menu.add(new MenuEntity("http����(��handle�Ĳ���)", HttpTestActivity.class)) ;
		menu.add(new MenuEntity("pop dialog����(���� ���ָ��� ���ӵ�Ч��)", Dialog_Pop_AdapterTestActivity.class)) ;
		menu.add(new MenuEntity("��Ƭ������(ͼƬ�ü��ȸ�������)", Photo_ShotTestActivity.class)) ;
		menu.add(new MenuEntity("������ ��ImageLoader�ȣ�", ThirdParty_TestActivity.class)) ;
		menu.add(new MenuEntity("����(���Զ��� �� ��������(��ɫ���ˡ������Ȳ���))", AnimalTestActivity.class)) ;
		menu.add(new MenuEntity("�Զ���ؼ�", CustomViewTestActivity.class)) ;
		menu.add(new MenuEntity("������Utils�Ĳ���", UtilsActivity.class)) ;
	}

}
