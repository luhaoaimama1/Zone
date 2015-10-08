package Android.Zone.Sqlite;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * <br>���ܽ��ܣ�
 * <br> ����ͨ�� ʵ����  �����������ݿ� 1. byClassToGenerateCreateSqlString 2.create_table
 * <br>��ѯ�Ľ�����װ�����֣� 1.queryToListAddMap 2.queryToEntity �㶮��
 * <br>getColumnNames: ���Եõ��ֶ�����
 * <br>column_updateOrDelete :���ı��ֶ�����ɾ�����ֶ�  
 * <br>�����Ը����������ֶΣ�columns_new_create��import_columns Ȼ�󵱲�������ȥ Ҳ�����Լ��������ʽȥдȻ�󴫽�ȥһ��
 * <br>Operating ��д������������1.֧�ֵ���������ɾ��2.֧������ ����������ɾ��
 * @author Zone
 *
 */
public class Sqlite_Dao extends SQLiteOpenHelper   {

	public static String DB_NAME = "mydata.db"; // ���ݿ�����
	// ------------------------��һ����--�޸��ֶε�ʱ��������Ҫ�������޸�----------------------
	public static String TABLE_NAME = "xihuanbu"; // ����
	// ����������
	public static String CreateSql = "CREATE TABLE  IF NOT EXISTS  "
			+ TABLE_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT ,name varchar(20),sex INTEGER);";
	//--------------**************�汾���µ�ʱ��ִ��**************-------------------------------
	/**
	 * Sqlite_Dao���ı�(ɾ���ֶλ����޸��ֶ�)�����  ��Ҫ�µ��ֶ� 
	 */
	public static String columns_new_create = "(id INTEGER PRIMARY KEY AUTOINCREMENT , name varchar(20));";
	/**
	 * ���ı��ʱ��  �ɱ�Ҫ�����±���ֶ�
	 * ���磺" id, "",sex " �����ÿո�վλ ��Ϊ����ɾ����һ���ֶ�
	 */
	public static String import_columns = " id, name ";
	
	// ------------------------�ڶ�����-------------------------
	// ------------------------��������-------------------------
	
	/**
	 * 
	 * @param context  �㶮��
	 * @param Version  �汾��  ���ýӿڵ��Ǹ�
	 */
	public Sqlite_Dao(Context context,int Version) {
		super(context, DB_NAME, null, Version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ���ð����������ɱ����ݸ�����  ��ͨ�����ô����create_table ���� ������
	 *  <br> ���������ӿ��Խ�����   ע�͵Ĳ���Ϊ��ͨ�� ʵ���� �ഴ����
	 * 
	 * @param t  ������Ҫ���� ���磺DbEntity.class
	 * @return �õ�����  
	 */
	public static String byClassToGenerateCreateSqlString(Class t)
	{
//		//ͨ�� ʵ�����ഴ����
//		String table=Sqlite_Dao.getClassProperty(DbEntity.class);
//		System.out.println("�����������ʲô��"+dao.CreateSql);
//		dao.create_table(dao.getWritableDatabase(), dao.CreateSql);
//		String[] cu=dao.getColumnNames(table);//�õ� �ֶ�����  ��֤���Ƿ񴴽�������
		
		StringBuffer sb=new StringBuffer(100);
		sb.append(" CREATE TABLE  IF NOT EXISTS  ");
		 String st = "CREATE TABLE  IF NOT EXISTS  "+ TABLE_NAME
				+ " (id INTEGER PRIMARY KEY AUTOINCREMENT ,name varchar(20),sex INTEGER);";
		Field[] fieds = t.getDeclaredFields();
		String[] str=t.getName().split("[.]");
		sb.append(str[str.length-1] +"(");
		System.out.println("�����֣�"+str[str.length-1] );
		int i=0;
		for (Field field : fieds) {
			if (i == 0) {
				if (field.getName().equals("id")) {
					sb.append("  id INTEGER PRIMARY KEY AUTOINCREMENT ");
				} else {
					sb.append(field.getName() + "  text(100)");
				}
			} else {
				if (field.getName().equals("id")) {
					sb.append(" , id INTEGER PRIMARY KEY AUTOINCREMENT ");
				} else {
					sb.append(" ," + field.getName() + "  text(100)");
				}
			}
			System.out.println("���֣�" + field.getName());
			i++;
		}
		sb.append(");");
		System.out.println("��������䣺"+sb.toString());
		CreateSql=sb.toString();
		return str[str.length-1];
	}
	/**
	 * super(����1������2������3������4)�����в���4�Ǵ������ݿ�İ汾�� ��һ�����ڵ���1�����������Ҫ�޸ģ�����ֶΣ����е��ֶΣ�������
	 * һ���ȵ�ǰ�� ����4������� ���Ѹ��µ����д��onUpgrade(),��һ�� ����
	 */
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// ��Ϊ������һ�κ��Ѿ���schedule.db������ݿ��ļ��ˣ�������֮�󶼲������onCreate��������ˣ����FileExploer������ݿ��ļ�ɾ����OK�ˡ�
		// --------------------------Create
		// table����-----------------------------------------------------
		// String sql = "CREATE TABLE "+TABLE_NAME + "("
		// + "_id INTEGER PRIMARY KEY,"
		// + "name TEXT,"
		// + "sex);";
		try {
			db.execSQL(CreateSql);
			Log.e("Dao��Ϣ", TABLE_NAME + "======isExist:true");
		} catch (SQLException e) {
			Log.e("Dao������Ϣ","������ʧ��  ���������CreateSqlΪ" + String.valueOf(CreateSql));
			e.printStackTrace();
		} 
	}
	/**
	 *  �ⲿ�õĴ�����ķ��� 
	 * <br>�˿����ڴ������
	 * @param db2 �㶮�� 
	 * @param Create_Sql ���ⴴ���ı����
	 */
	public void create_table(SQLiteDatabase db2,String Create_Sql) {
		SQLiteDatabase db = db2;
		// --------------------------Create
		// table����-----------------------------------------------------
		// String sql = "CREATE TABLE "+TABLE_NAME + "("
		// + "_id INTEGER PRIMARY KEY,"
		// + "name TEXT,"
		// + "sex);";
		try {
			db.execSQL(Create_Sql);
			Log.e("Dao��Ϣ", TABLE_NAME + "======isExist:true");
		} catch (SQLException e) {
			Log.e("Dao������Ϣ","������ʧ��  ���������CreateSqlΪ" + String.valueOf(CreateSql));
			e.printStackTrace();
		} finally {
				db.close();
		}
	}

	/**
	 * �޸İ汾�� ʱ�� ���޸�������������� ���ľͺ��� ��ϧdb������ Ū���� ֻ������ߵ��÷��������ˣ���������
	 * <br>onUpgrade�Դ�dbҲ��ò��ر� ����
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// �޸İ汾�� ʱ�� ���޸�������������� ���ľͺ��� ��ϧdb������ Ū���� ֻ������ߵ��÷��������ˣ���������
		// db.execSQL("ALTER TABLE "+ MyHelper.TABLE_NAME+" ADD sex TEXT");
		// //�޸��ֶ�
		try {
			// column_updateOrDelete(TABLE_NAME, columns_new_create,
			// import_columns);// �޸ı��е��ֶ� �ɸ���
			Log.e("Dao��Ϣ", "���±�ɹ�");
		} catch (SQLException e) {
			Log.e("Dao������Ϣ", "�޸ı�ʧ��  ���������UpdateSqlΪ" + String.valueOf(""));
			e.printStackTrace();
		}finally{
//			db.close();
		}
	}

	/**
	 * <br>�Դ�db�ر� ����
	 * @param sql
	 * @param object   ���ܴ���null ���ԡ�new��Object[]{};
	 * @return  ���سɹ���ʧ��
	 */
	public boolean Operating(String sql, Object[] object) {
		// ----------------------------------�޸ķ���-------------------------------------------------
		// SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "update " + MyParameters.TABLE_NAME
		// + " set name = ?,sex=?  where _id = ?;";
		// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id
		// });
		// db.close();
		// ----------------------------------ɾ������-------------------------------------------------
		// SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "delete  from " + MyParameters.TABLE_NAME
		// + " where  _id=? ";
		// db.execSQL(sql, new Object[] { id });
		// db.close();
		// ----------------------------------���뷶��-------------------------------------------------
		// String
		// sql="insert into "+MyParameters.TABLE_NAME+"(name, sex) values(?,?)";
		// db.execSQL(sql, new Object[]{person.getName(),person.getSex()});
		// db.execSQL(sql, object);
		// db.close();
		// ----------------------------------ɾ������-------------------------------------------------
		//delete from table �������ȫ������
		// ʹ��db.execSQL("delete from table",null);���Ǳ���?  
		// ԭ����db.execSQL("delete from table",null);�ڶ�����������Ϊnull������new
		// Object[]{}��OK�ˡ�
		// db.execSQL("DROP TABLE IF EXISTS students"); //�������
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.execSQL(sql, object);
			String[] split_str=sql.split("[?]");
			String Mosaic_str="";
			for (int i = 0; i < split_str.length; i++) {
				if(i!=split_str.length-1)
				{Mosaic_str+=split_str[i]+"\""+object[i]+"\"";}
				else{Mosaic_str+=split_str[i];}
			}
			Log.e("Dao�ɹ���Ϣ", "Operating Success:"+Mosaic_str);
			return true;
		} catch (SQLException e) {
			Log.e("Dao������Ϣ", "Operating failed");
			return false;
		} finally {
			db.close();
		}

	}
	/**
	 * <br>�Դ�db�ر� ����
	 * <br> ��Ϊ��Ҫ���� �������ص�Operating �ķ��� 
	 * <br>ӵ������Ĺ���
	 * @param sql
	 * @param objects    List<Object[]>;
	 * @return  ���سɹ���ʧ��
	 */
	public boolean Operating(String sql, List<Object[]> objects) {
		// ----------------------------------�޸ķ���-------------------------------------------------
		// SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "update " + MyParameters.TABLE_NAME
		// + " set name = ?,sex=?  where _id = ?;";
		// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id
		// });
		// db.close();
		// ----------------------------------ɾ������-------------------------------------------------
		// SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "delete  from " + MyParameters.TABLE_NAME
		// + " where  _id=? ";
		// db.execSQL(sql, new Object[] { id });
		// db.close();
		// ----------------------------------���뷶��-------------------------------------------------
		// String
		// sql="insert into "+MyParameters.TABLE_NAME+"(name, sex) values(?,?)";
		// db.execSQL(sql, new Object[]{person.getName(),person.getSex()});
		// db.execSQL(sql, object);
		// db.close();
		// ----------------------------------ɾ������-------------------------------------------------
		//delete from table �������ȫ������
		// ʹ��db.execSQL("delete from table",null);���Ǳ���?  
		// ԭ����db.execSQL("delete from table",null);�ڶ�����������Ϊnull������new
		// Object[]{}��OK�ˡ�
		// db.execSQL("DROP TABLE IF EXISTS students"); //�������
		SQLiteDatabase db = this.getWritableDatabase();
		//��������  
		db.beginTransaction();  
		try {
			for (Object[] object : objects) {
				db.execSQL(sql, object);
				String[] split_str=sql.split("[?]");
				String Mosaic_str="";
				for (int i = 0; i < split_str.length; i++) {
					if(i!=split_str.length-1)
					{Mosaic_str+=split_str[i]+"\""+object[i]+"\"";}
					else{Mosaic_str+=split_str[i];}
					Log.e("Dao�ɹ���Ϣ", "Operating Success:"+Mosaic_str);
				}
			}
			//���������־Ϊ�ɹ�������������ʱ�ͻ��ύ����  
			db.setTransactionSuccessful();  
			return true;
		} catch (SQLException e) {
			Log.e("Dao������Ϣ", "Operating failed");
			return false;
		} finally {
			//��������  
			db.endTransaction(); 
			db.close();
		}
		
	}

	/**
	 * <br>�Դ�db�ر� ����
	 * @param sql
	 * @param str  ����Ϊ null  Ҳ����new String[]{};
	 * @param log
	 *            �Ƿ��ڿ���̨��ӡ ��ѯ�ֶ�������
	 * @return   ����һ��list��map<String,String>������
	 */
	public List<HashMap<String, String>> queryToListAddMap(String sql, String[] str,boolean log) {
		List<HashMap<String, String>> list_map = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, str);
			//�˲�ѯ�еõ����ֶ�
			String[] lin2 = cursor.getColumnNames();
			Log.e("Dao��Ϣ", "�ֶ���");
			for (int i = 0; i < lin2.length; i++) {
				Log.e("Dao��Ϣ", i + "L:" + lin2[i]);
			}
			Log.e("Dao��Ϣ", "����");
			// Log.e("piu1", lin.toString());
			// Log.e("piu2", lin2[0].toString() + ":" + lin2[1].toString());
			if (cursor.getCount() == 0) {
				Log.e("Dao��Ϣ", "cursor �������ĿΪ��0");
			} else {
				Log.e("Dao��Ϣ","cursor �������ĿΪ:" + String.valueOf(cursor.getCount()));
				while (cursor.moveToNext()) {
					HashMap<String, String> map = new HashMap<String, String>();
					StringBuffer loge_temp = new StringBuffer();
					for (int i = 0; i < lin2.length; i++) {
						map.put(lin2[i], cursor.getString(cursor
								.getColumnIndex(lin2[i])));
						loge_temp.append(","
								+ lin2[i]
								+ ":"
								+ cursor.getString(cursor
										.getColumnIndex(lin2[i])));
					}
					if (log) {
						Log.e("Dao��Ϣ", "��" + cursor.getPosition() + "������====="
								+ loge_temp.toString().substring(1));
					}
					list_map.add(map);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Dao������Ϣ", "��ѯ�α귢���쳣");
			e.printStackTrace();
		} finally {
			db.close();
			cursor.close();
		}
		return list_map;
	}
	/**
	 * <br>�Դ�db�ر� ����
	 * @param <T>  һ��new��ʵ����
	 * @param sql  query ��sql���
	 * @param str  ����Ϊnull Ҳ����new String[]{};
	 * @param log  �Ƿ��ڿ���̨��ӡ ��ѯ�ֶ�������
	 * @return  	List ��ʵ����
	 */
	public <T> List<T> queryToEntity(T t,String sql, String[] str,boolean log) {
		List<HashMap<String, String>> list_map = new ArrayList<HashMap<String, String>>();
		//����Ҫ���ص�ʵ������
		List<T>  list_entity=new ArrayList<T>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, str);
			//�˲�ѯ�еõ����ֶ�
			String[] lin2 = cursor.getColumnNames();
			Log.e("Dao��Ϣ", "�ֶ���");
			for (int i = 0; i < lin2.length; i++) {
				Log.e("Dao��Ϣ", i + "L:" + lin2[i]);
			}
			Log.e("Dao��Ϣ", "����");
			
			Field[] fieds = t.getClass().getDeclaredFields();
			//�õ�ʵ�����ֶ�
			String[] entityColumn=new String[fieds.length];
			for (int i = 0; i < fieds.length; i++) {
				entityColumn[i]=fieds[i].getName();
				System.out.println("ʵ�����ֶΣ�"+entityColumn[i]);
			}
			
			// Log.e("piu1", lin.toString());
			// Log.e("piu2", lin2[0].toString() + ":" + lin2[1].toString());
			if (cursor.getCount() == 0) {
				Log.e("Dao��Ϣ", "cursor �������ĿΪ��0");
			} else {
				Log.e("Dao��Ϣ","cursor �������ĿΪ:" + String.valueOf(cursor.getCount()));
				//�α� ѭ������
				while (cursor.moveToNext()) {
					 //����һ��ʵ��
					T lin_entity=(T) t.getClass().newInstance();
					//set֮ǰ  ���ȿ����ݿ��еõ��ֶ� ʵ���ֶ���û��  �еĻ��Ϳ�����set����
					for (int i = 0; i < lin2.length; i++) {
						boolean columnNameIsExist = false;
						for (int j = 0; j < entityColumn.length; j++) {
							if (lin2[i].equals(entityColumn[j])) {
								columnNameIsExist = true;
							}
						}
						if (columnNameIsExist) {
//							 ����ֶδ��� ��ʼ��ʵ���� set
//							id   setId(String id)  Ч������
							String a = lin2[i];
							String b=a.substring(0, 1).toUpperCase();
							String c=a.substring(1);
							String finalStr="set"+b+c;
							//����ֻ֧��String  
							Method method = t.getClass().getMethod(finalStr, String.class);
							//����ִ�� set����
							method.invoke(lin_entity, cursor.getString(cursor.getColumnIndex(lin2[i]))+"");
						}
					}
					//�����ֶζ�set���  ��ʵ�� ���list��
					list_entity.add(lin_entity);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Dao������Ϣ", "��ѯ�α귢���쳣");
			e.printStackTrace();
		} finally {
			db.close();
			cursor.close();
		}
		return list_entity;
	}

	/**
	 * �����ֶ� ɾ���ֶε�ʱ���� ��һ�����ڰ汾���� ����db��bug ������ߵ��ú��ˣ� ���Ը���
	 * <br>�Դ�db�ر� ����
	 * @param tableName
	 *            Ҫ�����ֶεı���
	 * @param columns_new_create_Param
	 *            �����±�����ĺ���
	 * @param import_columns_Param
	 *            ԭ���ݵ��뵽�����ݵ��ֶ�
	 */
	public void column_updateOrDelete(String tableName,String columns_new_create_Param, String import_columns_Param) {
//		���ӣ�dao.column_updateOrDelete(Sqlite_Dao.TABLE_NAME, Sqlite_Dao.columns_new_create,Sqlite_Dao.import_columns);		
		
		// Sqlite ��֧��ֱ���޸��ֶε����ơ�
		// ���ǿ���ʹ�ñ�ķ�����ʵ���޸��ֶ�����
		// 1���޸�ԭ�������
		// ALTER TABLE table RENAME TO tableOld;
		// 2���½��޸��ֶκ�ı�
		// CREATE TABLE table(ID INTEGER PRIMARY KEY AUTOINCREMENT,
		// Modify_Username text not null);
		// 3���Ӿɱ��в�ѯ������ �������±�
		// INSERT INTO table SELECT ID,Username FROM tableOld;
		// 4��ɾ���ɱ�
		// DROP TABLE tableOld;
		
		
		//1��δ�����ɱ�֮ǰ �´���  ������� ��ɾ��
		String sql_drop1 = "DROP TABLE IF EXISTS " + tableName + "_old";
		try {
			Operating(sql_drop1, new Object[] {});
		} catch (Exception e) {
			Log.e("Dao������Ϣ", "����/ɾ���ֶ�---------ɾ���˱��old��ı�ʧ��");
			//�쳣 ʧ��֮��Ͳ�������
			return;
		}
		//2��δ�����ɱ�֮ǰ �´���  ������� ��ɾ��
		SQLiteDatabase db = this.getWritableDatabase();
		String rename = "ALTER TABLE " + tableName + "  RENAME TO " + tableName
				+ "_old";
		try {
			db.execSQL(rename);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Dao������Ϣ", "����/ɾ���ֶ�---------�ѱ�������oldϵ��ʧ��");
			//�쳣 ʧ��֮��Ͳ�������
			db.close();
			return;
		}
		//3���½��޸��ֶκ�ı�
		String table = "CREATE TABLE  IF NOT EXISTS  " + tableName
				+ columns_new_create_Param;
		try {
			db.execSQL(table);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Dao������Ϣ", "����/ɾ���ֶ�---------�����µĿձ�ʧ��");
			//�쳣 ʧ��֮��Ͳ�������
			db.close();
			return;
		}
		//4���Ӿɱ��в�ѯ������ �������±�
		String ex = "insert into " + tableName + " select "
				+ import_columns_Param + "  from " + tableName + "_old";
		try {
			Operating(ex, new Object[] {});
			Log.e("Dao��Ϣ", "ԭ���ݵ���ɹ�������");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Dao������Ϣ", "����/ɾ���ֶ�---------old�����±�ʧ��");
			//�쳣 ʧ��֮��Ͳ�������
			db.close();
			return;
			
		}
		// 5��ɾ���ɱ�
		String sql_drop = "DROP TABLE IF EXISTS " + tableName + "_old";
		try {
			Operating(sql_drop, new Object[] {});
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Dao������Ϣ", "����/ɾ���ֶ�---------ɾ��old��ʧ��");
			//�쳣 ʧ��֮��Ͳ�������
			db.close();
			return;
		}
	
		db.close();
		Log.e("Dao��Ϣ", "����/ɾ���ֶ�---------�ɹ�");

	}

	/**
	 * �õ�ĳ����������ֶ� ���û���ֶλᷢ����ָ���쳣
	 * <br>�Դ�db�ر� ���� 
	 * @param tableName
	 * @return  ����Ҫ��ѯ�ı��е��ֶ�
	 */
	public String[] getColumnNames(String tableName) {
		String[] lin2 = null;
		String sql = "select * from " + tableName;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			lin2 = cursor.getColumnNames();
		} catch (Exception e) {
			Log.e("Dao������Ϣ", "�������ֶΣ������ô˷���");
			e.printStackTrace();
		} finally {
			db.close();
			cursor.close();
		}

		return lin2;
	}

}
