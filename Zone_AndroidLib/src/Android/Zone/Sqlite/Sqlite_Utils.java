package Android.Zone.Sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import Android.Zone.Utils.SharedUtils;
import Java.Zone.Log.PrintUtils;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//TODO ʵ����id�ֶε����   ʵ�����а���ʵ����Ĳ���
/**
 * ��Ϊ�˹���ͨ���������� ���� set get �Ͳ�������ϵͳ���ɵ����� ������ǻᱨ��
 * ������id  ��ʱ֧��String
 * ʵ���а���ʵ�� δʵ��  
 * ͨ���ֶθ��ĸ��ı�δʵ��  
 * @author Zone
 */
public class Sqlite_Utils {
	private static String DB_NAME = "Zone.db"; // ���ݿ�����
	//����������ñ��õ� 
	private  static final int version=7;//Ĭ�ϵ�һ���汾��0
	private  static final  String SHARE_TAG="zone_db_version";
	private Sqlite_Helper helper;
	private static Sqlite_Utils instance=null;
	private static OnUpgrade onUpgrade=null;
	private static OnCreate onCreate=null;
	private static boolean printLog=false;
	
	private  Sqlite_Utils(Context context) {
		helper=new Sqlite_Helper(DB_NAME, context, 9);
	}
	
	public static void  init_listener(Context context,OnCreate onCreate,OnUpgrade onUpgrade){
		Sqlite_Utils.onUpgrade=onUpgrade;
		Sqlite_Utils.onCreate=onCreate;
		Sqlite_Utils.getInstance(context);
		if(instance!=null){
			onCreate.onCreateTable(instance);	
			onUpgrade(context,instance);
		}
	}
	
	public  interface OnUpgrade{
		public  void onUpgrade(int oldVersion, int newVersion, Sqlite_Utils instance2);
	}
	public  interface OnCreate{
		public  void onCreateTable(Sqlite_Utils instance);
	}
	/**
	 * ���������ע��� ���ļ�����  ���� ��ʼ��
	 * @param activity
	 * @return
	 */
	public static Sqlite_Utils getInstance(Context context){
		if(onUpgrade==null||onCreate==null)
			throw new IllegalStateException(" Must first init method(init_addOnUpgrade_listener) ");
		if(instance==null){
			instance=new Sqlite_Utils(context);
		}
		return instance;
	}
	public  <T> String[] queryColumnNamesByEntity(Class<T> t){
		String[] columns=helper.getColumnNames(t);
		if (printLog) {
			StringBuilder sb = new StringBuilder(100);
			sb.append("TableName:" + t.getSimpleName() + "\t{");
			int i = 0;
			for (String string : columns) {
				if (i == 0) {
					sb.append(" \t" + string);
				} else {
					sb.append(", \t" + string);
				}
				i++;
			}
			sb.append("\t}");
			System.out.println(sb.toString());
		}
		return columns;
	}
	public <T> List<T> queryEntitysByCondition(Class<T> t,String whereStr,String[] markStrArr){
		String sql="select * from "+t.getSimpleName()+" "+whereStr;
		List<T> list = helper.queryToEntity(t, sql, markStrArr, true);
		if (printLog) {
			for (T t2 : list) {
				PrintUtils.printAllField(t2);
			}
		}
		return list;
	}
	public <T> T queryEntityById(Class<T> t,String id){
		String sql="select * from "+t.getSimpleName()+" where id=?";
		List<T> list = helper.queryToEntity(t, sql, new String[]{id}, true);
		if(list.size()>1){
			throw new IllegalStateException("id have same is not possible!");
		}
		if (list.size() == 1) {
			if (printLog) {
				PrintUtils.printAllField(list.get(0));
			}
			return list.get(0);
		} else
			return null;
		
	}
	public <T> List<T> queryAllByClass(Class<T> t){
		String sql="select * from "+t.getSimpleName();
		List<T> list = helper.queryToEntity(t, sql, new String[]{}, true);
		if (printLog) {
			for (T t2 : list) {
				PrintUtils.printAllField(t2);
			}
		}
		return list;
	}
	public <T> void addOrUpdateEntity(T t){
		String id=null;
		try {
			Field idField = t.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			id=(String) idField.get(t);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		if(id==null){
			//��Ӳ���
			addEntity(t);
		}else{
			//����ID�ظ�û 
			String sql="select * from "+t.getClass().getSimpleName()+" where id=?";
			int count = helper.queryCountById(sql, new String[]{id}, true);
			if(count==0){
				//���ظ������
				addEntity(t);
			}else{
				//�ظ����޸�
				UpdateEntity(t);
			}
		}
	
	}
	public <T> void addEntity(T t){
		// String
		// sql="insert into "+MyParameters.TABLE_NAME+"(name, sex) values(?,?)";
		// db.execSQL(sql, new Object[]{person.getName(),person.getSex()});
		// db.execSQL(sql, object);
		// db.close();
		StringBuilder sb=new StringBuilder(50);
		sb.append("insert into ");
		sb.append(t.getClass().getSimpleName()+" ");
		Field[] fields = t.getClass().getDeclaredFields();
		StringBuilder columnSb=new StringBuilder(50);
		StringBuilder markSb=new StringBuilder(50);
		columnSb.append("(");
		markSb.append("(");
		int i=0;
		List<Object> columeValue=new ArrayList<Object>();
		//TODO ���ֶ������� ��һ������  δ��
		for (Field field : fields) {
			if(!"id".equals(field.getName())){
				field.setAccessible(true);
				if (i==0) {
					columnSb.append(field.getName());
					markSb.append("?");
				}else{
					columnSb.append(","+field.getName());
					markSb.append(",?");
				}
				i++;
				try {
					columeValue.add((String) field.get(t));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		columnSb.append(")");
		markSb.append(")");
		sb.append(columnSb.toString()+" values "+markSb.toString());
		String sql=sb.toString();
		Object[] object=columeValue.toArray();
		helper.Operating(sql, object);
	}
	public <T> void UpdateEntity(T t){
		// SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "update " + MyParameters.TABLE_NAME
		// + " set name = ?,sex=?  where _id = ?;";
		// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id
		// });
		// db.close();
		StringBuilder sb=new StringBuilder(50);
		sb.append("update ");
		sb.append(t.getClass().getSimpleName()+" set ");
		Field[] fields = t.getClass().getDeclaredFields();
		int i=0;
		List<Object> columeValue=new ArrayList<Object>();
		//TODO ���ֶ������� ��һ������  δ��
		for (Field field : fields) {
			if(!"id".equals(field.getName())){
				field.setAccessible(true);
				if (i==0) 
					sb.append(field.getName()+"=?");
				else
					sb.append(","+field.getName()+"=?");
				i++;
				try {
					columeValue.add((String) field.get(t));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		sb.append(" where id=? ");
		try {
			Field idField = t.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			columeValue.add(idField.get(t));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		String sql=sb.toString();
		Object[] object=columeValue.toArray();
		helper.Operating(sql, object);
	}
	public <T> void UpdateByCondition(Class<T> t,String Set_Where_Str,String[] markStrArr){
		// SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "update " + MyParameters.TABLE_NAME
		// + " set name = ?,sex=?  where _id = ?;";
		// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id
		// });
		// db.close();
		
		String sql="update   "+t.getSimpleName()+" "+Set_Where_Str;
		helper.Operating(sql,markStrArr);
	}
	public <T> void removeEntity(T t){
//		 SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "delete  from " + MyParameters.TABLE_NAME
		// + " where  _id=? ";
		// db.execSQL(sql, new Object[] { id });
		// db.close();	
		Object id = null;
		try {
			Field idField = t.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			id=idField.get(t);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		if(id==null)
			throw new IllegalArgumentException("id may be null");
		String whereStr=" where id=? ";
		String sql="delete  from "+t.getClass().getSimpleName()+" "+whereStr;
		Object[] markStrArr=new Object[]{id};
		helper.Operating(sql, markStrArr);
	}

	public <T> void removeEntityByCondition(Class<T> t,String whereStr,String[] markStrArr){
		String sql="delete  from "+t.getSimpleName()+" "+whereStr;
		helper.Operating(sql, markStrArr);
	}
	public <T> void removeAllByClass(Class<T> t){
		String sql="delete  from "+t.getSimpleName()+" ";
		helper.Operating(sql,new Object[]{});
	}
	/**======================== update======================
	 * @param instance2 */
	private static  void onUpgrade(Context context, Sqlite_Utils instance2){
		int oldVersion=SharedUtils.getInstance(context).readSp().getInt(SHARE_TAG, 0);
		SharedUtils.getInstance(context).writeSp().putInt(SHARE_TAG, version).commit();
		if(oldVersion!=version)
			onUpgrade.onUpgrade(oldVersion, version,instance2);
	}
	//TODO ͨ��������   δʵ��
	public  <T> void createTableByEntity(Class<T> t){
		helper.createTableByEntity(t);
	}
	public <T> void dropTableByClass(Class<T> t){
		// ----------------------------------ɾ������-------------------------------------------------
				//delete from table �������ȫ������
				// ʹ��db.execSQL("delete from table",null);���Ǳ���?  
				// ԭ����db.execSQL("delete from table",null);�ڶ�����������Ϊnull������new
				// Object[]{}��OK�ˡ�
				// db.execSQL("DROP TABLE IF EXISTS students"); //�������
		String sql="DROP TABLE IF EXISTS "+t.getSimpleName()+" ";
		helper.Operating(sql,new Object[]{});
	}
	public <T> void dropTableByString(SQLiteDatabase db,String tableName){
		String sql="DROP TABLE IF EXISTS "+tableName+" ";
		helper.Operating(sql,new Object[]{});
	}
	public <T> void addColumn(Class<T> t,String willAddColumnStr){
		helper.addColumn(t, willAddColumnStr);
	}
	/**
	 * 
	 * @param t	
	 * @param column_old  id��Ҫ��  ��� ����Ҫ�ı� �ֶ� name sex�Ǿ�new String[]{"name","sex"}
	 * @param column_target   ��� name ��ɾ�� sex��ı�� hex �Ǿ�new String[]{"","hex"}
	 */
	public <T> void updateOrDeleteColumn(Class<T> t,String[] column_old,String[] column_target) {
		helper.column_updateOrDelete(t, column_old, column_target);
	}
	/**
	 * ���ǵ���ģʽ
	 * @return
	 */
	public Transaction getTransaction(){
		return new Transaction();
	}
	public  class Transaction{
		private SQLiteDatabase db=null;
		private boolean transactionIsSuccess=true;
		
		public void beginTransaction(){
			db=helper.getWritableDatabase();
			db.beginTransaction();  
		}
		public void endTransaction(){
			if (transactionIsSuccess) {
				db.setTransactionSuccessful();
			}
			db.endTransaction();
			db.close();
		}
		public <T> void removeEntityByCondition_Transaction(Class<T> t,String whereStr,String[] markStrArr){
			String sql="delete  from "+t.getSimpleName()+" "+whereStr;
			if(!helper.Operating_Transaction(sql, markStrArr,db))
			transactionIsSuccess=false;
		}
		public <T> void removeAllByClass_Transaction(Class<T> t){
			String sql="delete  from "+t.getSimpleName()+" ";
			if(!helper.Operating_Transaction(sql,new Object[]{},db))
				transactionIsSuccess=false;
		}
		public <T> void addOrUpdateEntity(T t){
			String id=null;
			try {
				Field idField = t.getClass().getDeclaredField("id");
				idField.setAccessible(true);
				id=(String) idField.get(t);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			if(id==null){
				//��Ӳ���
				addEntity_Transaction(t);
			}else{
				//����ID�ظ�û 
				String sql="select * from "+t.getClass().getSimpleName()+" where id=?";
				int count = helper.queryCountById(sql, new String[]{id}, true);
				if(count==0){
					//���ظ������
					addEntity_Transaction(t);
				}else{
					//�ظ����޸�
					UpdateEntity_Transaction(t);
				}
			}
		
		}
		public <T> void addEntity_Transaction(T t){
					// String
					// sql="insert into "+MyParameters.TABLE_NAME+"(name, sex) values(?,?)";
					// db.execSQL(sql, new Object[]{person.getName(),person.getSex()});
					// db.execSQL(sql, object);
					// db.close();
			StringBuilder sb=new StringBuilder(50);
			sb.append("insert into ");
			sb.append(t.getClass().getSimpleName()+" ");
			Field[] fields = t.getClass().getDeclaredFields();
			StringBuilder columnSb=new StringBuilder(50);
			StringBuilder markSb=new StringBuilder(50);
			columnSb.append("(");
			markSb.append("(");
			int i=0;
			List<Object> columeValue=new ArrayList<Object>();
			//TODO ���ֶ������� ��һ������  δ��
			for (Field field : fields) {
				if(!"id".equals(field.getName())){
					field.setAccessible(true);
					if (i==0) {
						columnSb.append(field.getName());
						markSb.append("?");
					}else{
						columnSb.append(","+field.getName());
						markSb.append(",?");
					}
					i++;
					try {
						columeValue.add((String) field.get(t));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
			columnSb.append(")");
			markSb.append(")");
			sb.append(columnSb.toString()+" values "+markSb.toString());
			String sql=sb.toString();
			Object[] object=columeValue.toArray();
			if(!helper.Operating_Transaction(sql, object,db))
				transactionIsSuccess=false;
		}
		public <T> void UpdateEntity_Transaction(T t){
			// SQLiteDatabase db = helper.getWritableDatabase();
			// String sql = "update " + MyParameters.TABLE_NAME
			// + " set name = ?,sex=?  where _id = ?;";
			// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id
			// });
			// db.close();
			StringBuilder sb=new StringBuilder(50);
			sb.append("update ");
			sb.append(t.getClass().getSimpleName()+" set ");
			Field[] fields = t.getClass().getDeclaredFields();
			int i=0;
			List<Object> columeValue=new ArrayList<Object>();
			//TODO ���ֶ������� ��һ������  δ��
			for (Field field : fields) {
				if(!"id".equals(field.getName())){
					field.setAccessible(true);
					if (i==0) 
					sb.append(field.getName()+"=?");
					else
					sb.append(","+field.getName()+"=?");
					i++;
					try {
						columeValue.add((String) field.get(t));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
			sb.append(" where id=? ");
			try {
				Field idField = t.getClass().getDeclaredField("id");
				idField.setAccessible(true);
				columeValue.add(idField.get(t));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			String sql=sb.toString();
			Object[] object=columeValue.toArray();
			if(!helper.Operating_Transaction(sql, object,db))
				transactionIsSuccess=false;
		}
		public <T> void UpdateByCondition_Transaction(Class<T> t,String Set_Where_Str,String[] markStrArr){
			// SQLiteDatabase db = helper.getWritableDatabase();
					// String sql = "update " + MyParameters.TABLE_NAME
					// + " set name = ?,sex=?  where _id = ?;";
					// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id
					// });
					// db.close();
			
			String sql="update   "+t.getSimpleName()+" "+Set_Where_Str;
			transactionIsSuccess=helper.Operating_Transaction(sql,markStrArr,db);
		}
		public <T> void removeEntity_Transaction(T t){
		//		 SQLiteDatabase db = helper.getWritableDatabase();
				// String sql = "delete  from " + MyParameters.TABLE_NAME
				// + " where  _id=? ";
				// db.execSQL(sql, new Object[] { id });
				// db.close();	
				Object id = null;
				try {
					Field idField = t.getClass().getDeclaredField("id");
					idField.setAccessible(true);
					id=idField.get(t);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				if(id==null)
					throw new IllegalArgumentException("id may be null");
				String whereStr=" where id=? ";
				String sql="delete  from "+t.getClass().getSimpleName()+" "+whereStr;
				Object[] markStrArr=new Object[]{id};
				if(!helper.Operating_Transaction(sql, markStrArr,db))
					transactionIsSuccess=false;
			}
	}

	public static boolean getPrintLog(){
		return printLog;
	}
	public static void setPrintLog(boolean printLog){
		Sqlite_Utils.printLog=printLog;
	}
}
