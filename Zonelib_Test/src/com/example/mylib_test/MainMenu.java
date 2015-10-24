package com.example.mylib_test;

import java.util.ArrayList;
import java.util.List;

import com.example.mylib_test.activity.animal.Animal_MainActivity;
import com.example.mylib_test.activity.custom_view.CustomView_MainActivity;
import com.example.mylib_test.activity.db.Db_MainActivity;
import com.example.mylib_test.activity.db.entity.MenuEntity;
import com.example.mylib_test.activity.file.FileTestActivity;
import com.example.mylib_test.activity.http.Http_MainActivity;
import com.example.mylib_test.activity.photo_shot.Photo_Shot_MainActivity;
import com.example.mylib_test.activity.pop_dialog.Dialog_Pop_Adapter_MainActivity;
import com.example.mylib_test.activity.three_place.ThirdParty_MainActivity;
import com.example.mylib_test.activity.utils.Utils_MainActivity;
import com.example.mylib_test.activity.wifi.Wifi3g_MainActivity;

public class MainMenu {
	public static List<MenuEntity> menu=new ArrayList<MenuEntity>();
	
	static{
		menu.add(new MenuEntity("�ֻ� �ļ�/IO ����", FileTestActivity.class)) ;
		menu.add(new MenuEntity("wifi 3g����", Wifi3g_MainActivity.class)) ;
		menu.add(new MenuEntity("db����", Db_MainActivity.class)) ;
		menu.add(new MenuEntity("http����(��handle�Ĳ���)", Http_MainActivity.class)) ;
		menu.add(new MenuEntity("pop dialog����(���� ���ָ��� ���ӵ�Ч��)", Dialog_Pop_Adapter_MainActivity.class)) ;
		menu.add(new MenuEntity("��Ƭ������(ͼƬ�ü��ȸ�������)", Photo_Shot_MainActivity.class)) ;
		menu.add(new MenuEntity("������ ��ImageLoader�ȣ�", ThirdParty_MainActivity.class)) ;
		menu.add(new MenuEntity("����(���Զ��� �� ��������(��ɫ���ˡ������Ȳ���))", Animal_MainActivity.class)) ;
		menu.add(new MenuEntity("�Զ���ؼ�", CustomView_MainActivity.class)) ;
		menu.add(new MenuEntity("������Utils�Ĳ���", Utils_MainActivity.class)) ;
	}

}
