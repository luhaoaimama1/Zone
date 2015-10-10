package Android.Zone.Sqlite;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Android.Zone.Sqlite.Annotation.utils.AnUtils;
import Android.Zone.Sqlite.Inner.TempUtils;
import Android.Zone.Sqlite.Sqlite_Utils.OnUpgrade;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Sqlite_Helper  extends SQLiteOpenHelper  {
	private static final String TAG="Sqlite_Helper";
	private static final String SQL_ERR="sqlִ��ʧ�� ִ�е�sqlΪ";
	
	private boolean tranCloseDb=true;

	
	public boolean isTran() {
		return tranCloseDb;
	}

	public void setTran(boolean tran) {
		this.tranCloseDb = tran;
	}

	public Sqlite_Helper(String DbNname, Context context, int version) {
		super(context,DbNname, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 *  �ⲿ�õĴ�����ķ��� 
	 * <br>�˿����ڴ������
	 * @param <T>
	 * @param <T>
	 * @param db2 �㶮�� 
	 * @param Create_Sql ���ⴴ���ı����
	 */
	public  <T> void createTableByEntity(Class<T> t,SQLiteDatabase db) {
		String Create_Sql=byClassToGenerateCreateSqlString(t);
		// --------------------------Create
		// table����-----------------------------------------------------
		// String sql = "CREATE TABLE "+TABLE_NAME + "("
		// + "_id INTEGER PRIMARY KEY,"
		// + "name TEXT,"
		// + "sex);";
		try {
			db.execSQL(Create_Sql);
		} catch (SQLException e) {
			Log.e(TAG+"������Ϣ",SQL_ERR + Create_Sql);
			e.printStackTrace();
		} finally {
				if (tranCloseDb) {
					db.close();
				}
		}
	}
	public <T> void addColumn(Class<T> table,String willAddColumnStr,SQLiteDatabase db){
//		 db.execSQL("ALTER TABLE "+ MyHelper.TABLE_NAME+" ADD sex TEXT");
		String[] columns = getColumnNames(table,db);
		for (String item : columns) {
			if(willAddColumnStr.equals(item))
				return;
		}
		//TODO �ֶγ���
		String sql="ALTER TABLE "+ 	AnUtils.getTableAnnoName(table)+" ADD "+willAddColumnStr+" TEXT(100) ";
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			Log.e(TAG+"������Ϣ",SQL_ERR+ sql);
			e.printStackTrace();
		} finally {
				if (tranCloseDb) {
					db.close();
				}
		}
	}
	public class UpdateColumn{
		public String  column_old;//ע��
		public String column_target;//ע��  ����������ֶԱ�
		public String targetLength;
	}
	/**
	 * ������ ɾ���ֶ�  ���� ���üȿ�  ����ɾ���ֶ�  ֱ�� length�޸�
	 * 
	 * 
	 * �����ֶ� ɾ���ֶε�ʱ���� ��һ�����ڰ汾���� ����db��bug ������ߵ��ú��ˣ� ���Ը���
	 * <br>�Դ�db�ر� ����
	 * @param <T>
	 * @param tableName
	 *            Ҫ�����ֶεı���
	 * @param columns_new_create_Param
	 *            �����±�����ĺ���
	 * columns_new_create = "(id INTEGER PRIMARY KEY AUTOINCREMENT , name varchar(20));";
	 * @param import_columns_Param
	 *            ԭ���ݵ��뵽�����ݵ��ֶ�
	 *   import_columns = " id, name ";
	 */
//	public <T> void column_updateOrDelete(Class<T> t, List<UpdateColumn> updateColumnList  ,SQLiteDatabase db ) {
////		���ӣ�dao.column_updateOrDelete(Sqlite_Dao.TABLE_NAME, Sqlite_Dao.columns_new_create,Sqlite_Dao.import_columns);		
//		
//		// Sqlite ��֧��ֱ���޸��ֶε����ơ�
//		// ���ǿ���ʹ�ñ�ķ�����ʵ���޸��ֶ�����
//		// 1���޸�ԭ�������
//		// ALTER TABLE table RENAME TO tableOld;
//		// 2���½��޸��ֶκ�ı�
//		// CREATE TABLE table(ID INTEGER PRIMARY KEY AUTOINCREMENT,
//		// Modify_Username text not null);
//		// 3���Ӿɱ��в�ѯ������ �������±�
//		// INSERT INTO table SELECT ID,Username FROM tableOld;
//		// 4��ɾ���ɱ�
//		// DROP TABLE tableOld;
//		Field[] fields = t.getDeclaredFields();
//		List<String> columnNames=new ArrayList<String>();
//		List<String> columnNames_copy=new  ArrayList<String>();
//		for (Field field : fields) {
//			field.setAccessible(true);
//			columnNames.add(field.getName());
//			columnNames_copy.add(field.getName());
//		}
//		List<String> column_old_list = Arrays.asList(column_old);
//		for (String string : column_old_list) {
//			if(!columnNames.contains(string))
//				throw new IllegalArgumentException("column_old is not exist to Entity");
//		}
//	
//		
////�����ж��ظ��� ��Ϊ ������ length�ı���
////		//�ж��Ƿ��ظ�
////		int j=0;
////		for (int i = 0; i < column_old.length; i++) {
////			if(column_old[i].equals(column_target[i])){
////				j++;
////			}
////		}
////		if(j==column_old.length){
////			//��ȫ�ظ�û��Ҫ����
////			return;
////		}
//		String tableName=AnUtils.getTableAnnoName(t);
//		//1��δ�����ɱ�֮ǰ �´���  ������� ��ɾ��
//		String sql_drop1 = "DROP TABLE IF EXISTS " + tableName + "_old";
//		try {
//			Operating(sql_drop1, new Object[] {},db);
//		} catch (Exception e) {
//			Log.e(TAG+"������Ϣ",SQL_ERR+ sql_drop1+"______________ɾ���˱��old��ı�ʧ��");
//			return;
//		}
//		//2��δ�����ɱ�֮ǰ �´���  ������� ��ɾ��
//		String rename = "ALTER TABLE " + tableName + "  RENAME TO " + tableName
//				+ "_old";
//		try {
//			db.execSQL(rename);
//		} catch (Exception e) {
//			Log.e(TAG+"������Ϣ",SQL_ERR+ rename+"______________�ѱ�������oldϵ��ʧ��");
//			if (tranCloseDb) {
//				db.close();
//			}
//			return;
//		}
//		//3���½��޸��ֶκ�ı�  �����ڵ��ഴ����
//		String table=byClassToGenerateCreateSqlString(t);
//		try {
//			db.execSQL(table);
//		} catch (Exception e) {
//			Log.e(TAG+"������Ϣ",SQL_ERR+ table+"______________�����µĿձ�ʧ��");
//			if (tranCloseDb) {
//				db.close();
//			}
//			return;
//		}
//		//���ֶ���""  ����ѯ�������Ӷζ�Ӧ�� �ֶ�ȥ��
//		//���ֶβ��ǿ�  ��ԭ�����ֶμ���
//		List<String> column_target_list=Arrays.asList(column_target);
//		for (String item : column_target_list) {
//			if("".equals(item)){
//				int index=column_target_list.indexOf(item);
//				String str=column_old[index];
//				columnNames_copy.remove(str);
//			}
//		}
//		String import_columns = " ";
//		for (String string : columnNames_copy) {
//			import_columns+=string+",";
//		}
////		String import_columns = " id, name ";
//		String import_columns_Param=import_columns.substring(0,import_columns.length()-1);
//		//4���Ӿɱ��в�ѯ������ �������±�
//		String ex = "insert into " + tableName + " select "
//				+ import_columns_Param + "  from " + tableName + "_old";
//		try {
//			Operating(ex, new Object[] {},db);
//		} catch (Exception e) {
//			Log.e(TAG+"������Ϣ",SQL_ERR+ ex+"______________old�����±�ʧ��");
//			if (tranCloseDb) {
//				db.close();
//			}
//			return;
//			
//		}
//		// 5��ɾ���ɱ�
//		String sql_drop = "DROP TABLE IF EXISTS " + tableName + "_old";
//		try {
//			Operating(sql_drop, new Object[] {},db);
//		} catch (Exception e) {
//			Log.e(TAG+"������Ϣ",SQL_ERR+ sql_drop+"______________ɾ��old��ʧ��");
//			if (tranCloseDb) {
//				db.close();
//			}
//			return;
//		}
//	
//		if (tranCloseDb) {
//			db.close();
//		}
//		StringBuilder print_column_old_sb=new StringBuilder();
//		StringBuilder print_column_target_sb=new StringBuilder();
//		for (int i = 0; i < column_old.length; i++) {
//			if(i==0){
//				print_column_old_sb.append("{"+column_old[i]);
//				print_column_target_sb.append("{"+column_target[i]);
//			}else{
//				print_column_old_sb.append(","+column_old[i]);
//				print_column_target_sb.append(","+column_target[i]);
//			}
//		}
//		print_column_old_sb.append("}");
//		print_column_target_sb.append("}");
//		if (Sqlite_Utils.getPrintLog()) {
//			Log.d(TAG,
//					"��" + t.getSimpleName() + "\t "
//							+ print_column_old_sb.toString() + "---->>>"
//							+ print_column_target_sb.toString()
//							+ "����/ɾ���ֶ�---------�ɹ�");
//		}
//
//	}
	/**
	 * ���ð����������ɱ����ݸ�����  ��ͨ�����ô����create_table ���� ������
	 *  <br> ���������ӿ��Խ�����   ע�͵Ĳ���Ϊ��ͨ�� ʵ���� �ഴ����
	 * @param <T>
	 * @param <T>
	 * 
	 * @param t  ������Ҫ���� ���磺DbEntity.class
	 * @return �õ�����  
	 */
	private  <T> String byClassToGenerateCreateSqlString(Class<T> t)
	{
		
//		String st = "CREATE TABLE  IF NOT EXISTS  "+ TABLE_NAME
//				+ " (id INTEGER PRIMARY KEY AUTOINCREMENT ,name varchar(20),sex INTEGER);";
		StringBuffer sb=new StringBuffer(100);
		sb.append(" CREATE TABLE  IF NOT EXISTS  ");
		Field[] fieds = t.getDeclaredFields();
		sb.append(AnUtils.getTableAnnoName(t) +"(");
		int i=0;
		for (Field field : fieds) {
			if (i == 0) {
				if (field.getName().equals("id")) {
					sb.append("  id INTEGER PRIMARY KEY AUTOINCREMENT ");
				} else {
					/** �ж�������  String */
					if(TempUtils.typeIsEquals(String.class, field.getGenericType()))
					sb.append(AnUtils.getAnnoColumn_Name_ByField(field, t) + "  text("+AnUtils.getAnnoColumn_Length_ByField(field, t)+")");
				}
			} else {
				if (field.getName().equals("id")) {
					sb.append(" , id INTEGER PRIMARY KEY AUTOINCREMENT ");
				} else {
					if(TempUtils.typeIsEquals(String.class, field.getGenericType()))
					sb.append(" ," + AnUtils.getAnnoColumn_Name_ByField(field, t) + "  text("+AnUtils.getAnnoColumn_Length_ByField(field, t)+")");
				}
			}
			i++;
		}
		sb.append(");");
		if (Sqlite_Utils.getPrintLog()) {
			Log.d(TAG, "��������䣺" + sb.toString());
		}
		return sb.toString();
	}
	/**
	 * �õ�ĳ����������ֶ� ���û���ֶλᷢ����ָ���쳣
	 * <br>�Դ�db�ر� ���� 
	 * @param <T>
	 * @param tableName
	 * @return  ����Ҫ��ѯ�ı��е��ֶ�
	 */
	public <T> String[] getColumnNames(Class<T> t,SQLiteDatabase db ) {
		String tableName=AnUtils.getTableAnnoName(t);
		String[] lin2 = null;
		String sql = "select * from " + tableName;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			lin2 = cursor.getColumnNames();
		} catch (Exception e) {
			Log.e(TAG+"������Ϣ","�������ֶΣ������ô˷���");
			e.printStackTrace();
		} finally {
			if (tranCloseDb) {
				db.close();
			}
			if(cursor!=null){
				cursor.close();
			}
		}

		return lin2;
	}
	public int queryCountById(String sql, String[] str,boolean log) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery(sql, str);
		return cursor.getCount();
	}
	
	/**
	 * <br>�Դ�db�ر� ����
	 * @param <T>  һ��new��ʵ����
	 * @param sql  query ��sql���
	 * @param str  ����Ϊnull Ҳ����new String[]{};
	 * @param log  �Ƿ��ڿ���̨��ӡ ��ѯ�ֶ�������
	 * @return  	List ��ʵ����
	 */
	public <T> List<T> queryToEntity(Class<T> t,String sql, String[] str,boolean log,SQLiteDatabase db ) {
		// ----------------------------------�޸ķ���-------------------------------------------------
				// SQLiteDatabase db = helper.getWritableDatabase();
				// String sql = "update " + MyParameters.TABLE_NAME
				// + " set name = ?,sex=?  where _id = ?;";
				// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id
				// });
				// db.close();
		//����Ҫ���ص�ʵ������
		List<T>  list_entity=new ArrayList<T>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, str);
			//�˲�ѯ�еõ����ֶ�
			String[] columnNames = cursor.getColumnNames();
			Field[] fieds = t.getDeclaredFields();
			//�õ�ʵ�����ֶ�
			List<Field> fieldList=new ArrayList<Field>();
			for (int i = 0; i < fieds.length; i++) {
				if(TempUtils.typeIsEquals(String.class, fieds[i].getGenericType())){
					fieldList.add(fieds[i]);
				}
					
			}
			if (cursor.getCount() == 0) {
			} else {
				//�α� ѭ������
				while (cursor.moveToNext()) {
					 //����һ��ʵ��
					T lin_entity=(T) t.newInstance();
					//set֮ǰ  ���ȿ����ݿ��еõ��ֶ� ʵ���ֶ���û��  �еĻ��Ϳ�����set����
					for (int i = 0; i < columnNames.length; i++) {
						boolean columnNameIsExist = false;
						int fieldIndex=-1;
						for (Field item : fieldList) {
							if (columnNames[i].equals(AnUtils.getAnnoColumn_Name_ByField(item, t))) {
								fieldIndex=fieldList.indexOf(item);
								columnNameIsExist = true;
							}
						}
						if (columnNameIsExist) {
//							 ����ֶδ��� ��ʼ��ʵ���� set
//							id   setId(String id)  Ч������
							String a = fieldList.get(fieldIndex).getName();
							String b=a.substring(0, 1).toUpperCase();
							String c=a.substring(1);
							String finalStr="set"+b+c;
							//����ֻ֧��String  
							Method method = t.getMethod(finalStr, String.class);
							//����ִ�� set����
							method.invoke(lin_entity, cursor.getString(cursor.getColumnIndex(columnNames[i]))+"");
						}
					}
					//�����ֶζ�set���  ��ʵ�� ���list��
					list_entity.add(lin_entity);
				}
			}
		} catch (Exception e) {
			Log.e(TAG+"������Ϣ", "��ѯ�α귢���쳣  �������ݿ������");
			e.printStackTrace();
		} finally {
			if (cursor!=null) {
				cursor.close();
			}
			if (tranCloseDb) {
				db.close();
			}
		}
		return list_entity;
	}
	
	
	/**
	 * <br>�Դ�db�ر� ����
	 * @param sql
	 * @param object   ���ܴ���null ���ԡ�new��Object[]{};
	 * @return  ���سɹ���ʧ��
	 */
	public boolean Operating(String sql, Object[] object,SQLiteDatabase db ) {
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
		try {
			db.execSQL(sql, object);
			return true;
		} catch (SQLException e) {
			Log.e(TAG+"������Ϣ",SQL_ERR + sql);
			return false;
		} finally {
			if (Sqlite_Utils.getPrintLog()) {
				System.out.println("�Ƿ�ر������ݿ⣺"+ (tranCloseDb == true ? "��" : "��"));
			}
			if (tranCloseDb) {
				db.close();
			}
		}
	}
}
