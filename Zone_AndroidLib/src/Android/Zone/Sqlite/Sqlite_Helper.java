package Android.Zone.Sqlite;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import Android.Zone.Sqlite.Annotation.utils.AnUtils;
import Android.Zone.Sqlite.GsonEntity.GsonColumn;
import Android.Zone.Sqlite.Inner.TempUtils;
import Android.Zone.Sqlite.Inner.UpdateColumn;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class Sqlite_Helper  extends SQLiteOpenHelper  {
	private static final String TAG="Sqlite_Helper";
	private static final String SQL_ERR="sqlִ��ʧ�� ִ�е�sqlΪ";
	
	private boolean noTran=true;

	
	public boolean isTran() {
		return noTran;
	}

	public void setTran(boolean tran) {
		this.noTran = tran;
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
	 * ��Ϊ  �������ڱ�ķ����е�������  db���ر�
	 * @param t
	 * @param db
	 * @return
	 */
	public <T> boolean existTable(Class<T> t,SQLiteDatabase db){
		boolean result=false;
		String sql="SELECT COUNT(*) FROM sqlite_master where type='table' and name='"+	AnUtils.getTableAnnoName(t)+"'";
		try {
			 Cursor cursor = db.rawQuery(sql, null);
             if(cursor.moveToNext()){
                     int count = cursor.getInt(0);
                     if(count>0){
                             result = true;
                     }
             }
		} catch (SQLException e) {
			Log.e(TAG+"������Ϣ",SQL_ERR + sql);
			e.printStackTrace();
		}
		return result;
	}
	/**
	 *  �ⲿ�õĴ�����ķ��� 
	 * <br>�˿����ڴ������
	 * @param <T>
	 * @param <T>
	 * @param db2 �㶮�� 
	 * @param Create_Sql ���ⴴ���ı����
	 */
	public  <T> boolean createTableByEntity(Class<T> t,SQLiteDatabase db) {
		if(existTable(t, db)){//���Ч��
			//�������ڼ����ù���
			if (noTran) {
				//���û������ �͹ر�
				db.close();
			}
			return false;
		}
		String Create_Sql=byClassToGenerateCreateSqlString(t);
		// --------------------------Create
		// table����-----------------------------------------------------
		// String sql = "CREATE TABLE "+TABLE_NAME + "("
		// + "_id INTEGER PRIMARY KEY,"
		// + "name TEXT,"
		// + "sex);";
		boolean success=false;
		try {
			db.execSQL(Create_Sql);
			success=true;
		} catch (SQLException e) {
			Log.e(TAG+"������Ϣ",SQL_ERR + Create_Sql);
			e.printStackTrace();
			return success;
		} finally {
				if (noTran) {
					db.close();
				}
		}
		return success;
	}
	public enum AddColumnStatue{
		Success,Fail,Exist;
	}
	public <T> AddColumnStatue addColumn(Class<T> table,String willAddColumnStr,String length,SQLiteDatabase db){
//		 db.execSQL("ALTER TABLE "+ MyHelper.TABLE_NAME+" ADD sex TEXT");
		String[] columns = getColumnNames(table,db,false);
		
		for (String item : columns) {
			if(willAddColumnStr.equals(item))
				return AddColumnStatue.Exist;
		}
		boolean success=false;
		String sql="ALTER TABLE "+ 	AnUtils.getTableAnnoName(table)+" ADD "+willAddColumnStr+" TEXT("+length+") ";
		try {
			db.execSQL(sql);
			success=true;
		} catch (SQLException e) {
			Log.e(TAG+"������Ϣ",SQL_ERR+ sql);
			e.printStackTrace();
			success=false;
		} finally {
			System.out.println("db shi openma ?"+db.isOpen());
				if (noTran&&db.isOpen()) {
					db.close();
				}
		}
		if(success){
			return AddColumnStatue.Success;
		}else{
			return AddColumnStatue.Fail;
		}
	}
	/**
	 * ������ ɾ���ֶ�  ���� ���üȿ�  ����ɾ���ֶ�  ֱ�� length�޸�
	 * �����ﲻ����  ��ԭʼ�� 
	 *            Ҫ�����ֶεı���
	 *            �����±�����ĺ���
	 * columns_new_create = "(id INTEGER PRIMARY KEY AUTOINCREMENT , name varchar(20));";
	 *            ԭ���ݵ��뵽�����ݵ��ֶ�
	 *   import_columns = " id, name ";
	 * @param t
	 * @param updateColumnList
	 * @param db
	 */
	public <T> boolean column_updateOrDelete(Class<T> t,List<GsonColumn> noUpdateList, List<UpdateColumn> updateColumnList  ,SQLiteDatabase db ) {
//		���ӣ�dao.column_updateOrDelete(Sqlite_Dao.TABLE_NAME, Sqlite_Dao.columns_new_create,Sqlite_Dao.import_columns);		
		
		// Sqlite ��֧��ֱ���޸��ֶε����ơ�
		// ���ǿ���ʹ�ñ�ķ�����ʵ���޸��ֶ�����
		// 1���޸�ԭ�������
		// ALTER TABLE table RENAME TO tableOld;
		// 2���½��޸��ֶκ�ı�
		// CREATE TABLE table(ID INTEGER PRIMARY KEY AUTOINCREMENT,
		// Modify_Username text not null);
		// 3���Ӿɱ��в�ѯ������ �������±�
		// INSERT INTO table(id,username) SELECT ID,Username FROM tableOld;
		// 4��ɾ���ɱ�
		// DROP TABLE tableOld;
		StringBuffer sb_noUpdateList=new StringBuffer();
		for (GsonColumn item : noUpdateList) {
			sb_noUpdateList.append(item.getName()+",");
		}
		String str_noUpdateList = sb_noUpdateList.toString();
		str_noUpdateList=str_noUpdateList.substring(0,str_noUpdateList.length()-1);
		
		List<String> columnNames=new ArrayList<String>();
		List<String> columnNames_Target=new  ArrayList<String>();
		for (UpdateColumn item : updateColumnList) {
			//��Ŀ��Ϊ""���ַ�����ʱ�� ��ô����ɾ�����ֶ�
			if(!("").equals(item.column_target.trim())){
				columnNames.add(item.column_old);
				columnNames_Target.add(item.column_target);
			}
		}
		
		String tableName=AnUtils.getTableAnnoName(t);
		//1��δ�����ɱ�֮ǰ �´���  ������� ��ɾ��
		String sql_drop1 = "DROP TABLE IF EXISTS " + tableName + "_old";
		try {
			Operating(sql_drop1, new Object[] {},db,false);
		} catch (Exception e) {
			Log.e(TAG+"������Ϣ",SQL_ERR+ sql_drop1+"______________ɾ���˱��old��ı�ʧ��");
			e.printStackTrace();
			return false;
		}
		//2��δ�����ɱ�֮ǰ �´���  ������� ��ɾ��
		String rename = "ALTER TABLE " + tableName + "  RENAME TO " + tableName
				+ "_old";
		try {
			db.execSQL(rename);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG+"������Ϣ",SQL_ERR+ rename+"______________�ѱ�������oldϵ��ʧ��");
			if (noTran) {
				db.close();
			}
			return false;
		}
	
		//3���½��޸��ֶκ�ı�  �����ڵ��ഴ����
//		String table=byClassToGenerateCreateSqlString(t);
//		String st = "CREATE TABLE  IF NOT EXISTS  "+ TABLE_NAME
//		+ " (id INTEGER PRIMARY KEY AUTOINCREMENT ,name varchar(20),sex INTEGER);";
		StringBuffer sb_table=new StringBuffer();
		sb_table.append("CREATE TABLE  IF NOT EXISTS "+tableName+" (id INTEGER PRIMARY KEY AUTOINCREMENT ");
//		willAddColumnStr+" TEXT("+length+") "
		for (GsonColumn gsonItem : noUpdateList) {
			if(!("id").equals(gsonItem.getName())&&!("").equals(gsonItem.getName().trim())){
				//������ID�ʹ���
				sb_table.append(","+gsonItem.getName()+" TEXT("+gsonItem.getLength()+")  ");
			}
		}
		for (UpdateColumn item : updateColumnList) {
			if(!("").equals(item.column_target.trim())){
				sb_table.append(","+item.column_target+" TEXT("+item.targetLength+")  ");
			}
		}
		sb_table.append(");");
		String table=sb_table.toString();
		try {
			db.execSQL(table);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG+"������Ϣ",SQL_ERR+ table+"______________�����µĿձ�ʧ��");
			if (noTran) {
				db.close();
			}
			return false;
		}
		//���ֶ���""  ����ѯ�������Ӷζ�Ӧ�� �ֶ�ȥ��
		//���ֶβ��ǿ�  ��ԭ�����ֶμ���
//		(id,username) SELECT ID,Username 

		boolean insertSucess=false;
		//4���Ӿɱ��в�ѯ������ �������±�
		String import_columns_Param="";
		String import_columns_Param_new="";
		if(("").equals(listMergeSymbol(columnNames, ","))){
			import_columns_Param=str_noUpdateList;
			import_columns_Param_new=str_noUpdateList;
		}else{
			import_columns_Param=str_noUpdateList+","+listMergeSymbol(columnNames, ",");
			import_columns_Param_new=str_noUpdateList+","+listMergeSymbol(columnNames_Target, ",");
		}
		
		String ex = "insert into " + tableName + " ("+import_columns_Param_new+") select "
				+ import_columns_Param + "  from " + tableName + "_old";
		try {
			insertSucess=Operating(ex, new Object[] {},db,false);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG+"������Ϣ",SQL_ERR+ ex+"______________old�����±�ʧ��");
			if (noTran) {
				db.close();
			}
			return false;
			
		}
		// 5��ɾ���ɱ�
		String sql_drop = "DROP TABLE IF EXISTS " + tableName + "_old";
		try {
			Operating(sql_drop, new Object[] {},db,false);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG+"������Ϣ",SQL_ERR+ sql_drop+"______________ɾ��old��ʧ��");
			if (noTran) {
				db.close();
			}
			return false;
		}
	
		if (noTran) {
			db.close();
		}
	
		if (Sqlite_Utils.getPrintLog()) {
			if (insertSucess) {
				Log.d(TAG,
						"��" + t.getSimpleName() + "\t "+ listMergeSymbol(columnNames, ",") + "---->>>"
								+ listMergeSymbol(columnNames_Target, ",")+ "����/ɾ���ֶ�---------�ɹ�");
			}
		}
		return true;

	}
	private String listMergeSymbol(List<String> noUpdateList,String symbol){
		StringBuffer temp=new StringBuffer();
		for (String string : noUpdateList) {
			temp.append(string+symbol);
		}
		String str_noUpdateList=temp.toString();
		if(str_noUpdateList.length()>=1){
			str_noUpdateList=str_noUpdateList.substring(0,temp.toString().length()-1);
		}
		return str_noUpdateList;
	}
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
					if(TempUtils.typeIsEquals(String.class, field.getGenericType())){
						sb.append(AnUtils.getAnnoColumn_Name_ByField(field, t) + "  text("+AnUtils.getAnnoColumn_Length_ByField(field, t)+")");
					}
				}
			} else {
				if (field.getName().equals("id")) {
					sb.append(" , id INTEGER PRIMARY KEY AUTOINCREMENT ");
				} else {
					if(TempUtils.typeIsEquals(String.class, field.getGenericType())){
						sb.append(" ," + AnUtils.getAnnoColumn_Name_ByField(field, t) + "  text("+AnUtils.getAnnoColumn_Length_ByField(field, t)+")");
					}
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
	public <T> String[] getColumnNames(Class<T> t,SQLiteDatabase db ,boolean closeDb) {
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
			if (noTran&&closeDb) {
				db.close();
			}
			if(cursor!=null){
				cursor.close();
			}
		}

		return lin2;
	}
	public <T> String[] getColumnNames(Class<T> t,SQLiteDatabase db ) {
		return getColumnNames(t, db,true);
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
			if (noTran) {
				db.close();
			}
		}
		return list_entity;
	}
	
	public boolean Operating(String sql, Object[] object,SQLiteDatabase db) {
		return Operating(sql, object, db,true);
	}
	/**
	 * <br>�Դ�db�ر� ����
	 * @param sql
	 * @param object   ���ܴ���null ���ԡ�new��Object[]{};
	 * @return  ���سɹ���ʧ��
	 */
	public boolean Operating(String sql, Object[] object,SQLiteDatabase db,boolean dbClose) {
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
				System.out.println("�Ƿ�ر������ݿ⣺"+ (noTran == true ? "��" : "��"));
			}
			if (noTran&&dbClose) {
				db.close();
			}
		}
	}
}
