package Android.Zone.Sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import Android.Zone.Sqlite.Annotation.utils.AnUtils;
import Android.Zone.Utils.SharedUtils;
import Java.Zone.Log.PrintUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//TODO ʵ����id�ֶε����   ʵ�����а���ʵ����Ĳ���
/**
 * ��Ҫ����ע�� ����Ҫע����ֶ���ȫ��һ�� Ȼ���ӡÿ���������sql�����Բ��Ծ�OK��
 * 
 * ��Ϊ�˹���ͨ���������� ���� set get �Ͳ�������ϵͳ���ɵ����� ������ǻᱨ��
 * ������id  ��ʱ֧��String
 * ʵ���а���ʵ�� δʵ��  
 * ͨ���ֶθ��ĸ��ı�δʵ��  
 * @author Zone
 */
public class Sqlite_Utils {
	private static String DB_NAME = "Zone.db"; // ���ݿ�����
	//��Ϊһ�� Ӧ�þ�һ�����ݿ� ���԰汾��  ��̬��OK��
	private  static  int version=1;//Ĭ�ϵ�һ���汾��1  
	private  static final  String SHARE_TAG="zone_db_version";
	private Sqlite_Helper helper;
	private static Sqlite_Utils instance=null;
	private static OnUpgrade onUpgrade=null;
	private static OnCreate onCreate=null;
	private static boolean printLog=false;
	private SQLiteDatabase tranSQLiteDatabase=null;
	/**
	 * 
	 * @param context ֻ�ǲ�ͬ�� SQLiteOpenHelper ����һ��Ӧ�þ���һ�����ݿ�
	 */
	private  Sqlite_Utils(Context context) {
		helper=new Sqlite_Helper(DB_NAME, context,1);
	}
	/**
	 * ֻҪע�����   �ͻ� ������  �ͼ����汾�л� 
	 * @param context
	 * @param onCreate
	 * @param onUpgrade
	 */
	public static void  init_listener(Context context,OnCreate onCreate,OnUpgrade onUpgrade){
		Sqlite_Utils.onUpgrade=onUpgrade;
		Sqlite_Utils.onCreate=onCreate;
		Sqlite_Utils.getInstance(context);
		if(instance!=null){
			onCreate.onCreateTable(instance);	
			onUpgrade(context,instance);
		}
	}
	/**======================== update======================
	 * @param instance2  ��ĳ�instance  ��Ϊ˽�б�����instance�ˡ�����
	 */
	private static  void onUpgrade(Context context, Sqlite_Utils instance2){
		int oldVersion=SharedUtils.getInstance(context).readSp().getInt(SHARE_TAG, 0);
		SharedUtils.getInstance(context).writeSp().putInt(SHARE_TAG, version).commit();
		if(oldVersion!=version&&oldVersion!=0)
			onUpgrade.onUpgrade(oldVersion, version,instance2);
	}
	public  interface OnUpgrade{
		/**
		 * TODO  ������ ÿ��������ʱ��   ����ʵ�����Զ� �����ݿ��� �任
		 * @param oldVersion
		 * @param newVersion
		 * @param instance2
		 */
		public  void onUpgrade(int oldVersion, int newVersion, Sqlite_Utils instance2);
	}
	public  interface OnCreate{
		/**
		 * TODO ÿ��initListener��ʱ��  ������ ���ܱ��ڲ��ı任
		 * @param instance
		 */
		public  void onCreateTable(Sqlite_Utils instance);
	}
	public void workWithTran(Work work) {
		try {
			if(work==null)
				throw new IllegalArgumentException("work may be null");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		tranSQLiteDatabase = helper.getWritableDatabase();
		helper.setTran(false);
		tranSQLiteDatabase.beginTransaction();
		try {
			work.work();
			tranSQLiteDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ��������
			tranSQLiteDatabase.endTransaction();
			tranSQLiteDatabase.close();
			helper.setTran(true);
			tranSQLiteDatabase=null;
		}
	}
	public interface Work{
		public abstract void work();
	}
	/**
	 * ��֤�� context ����һ�����ݿ�  Sqlite_Utils Ҳ��һ����Ϊ���õ��ǵ���ģʽ
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
	private SQLiteDatabase getTranDatabase(){
		if (tranSQLiteDatabase != null) {
			return tranSQLiteDatabase;
		} else {
			return helper.getWritableDatabase();
		}
	}
	
	/**
	 * @param t
	 * @return
	 */
	public  <T> String[] queryColumnNamesByEntity(Class<T> t){
		String[] columns=helper.getColumnNames(t,getTranDatabase());
		if (printLog) {
			StringBuilder sb = new StringBuilder(100);
			sb.append("TableName:" + AnUtils.getTableAnnoName(t) + "\t{");
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
	/**
	 * @param t
	 * @param whereStr
	 * @param markStrArr
	 * @return
	 */
	public <T> List<T> queryEntitysByCondition(Class<T> t,String whereStr,String[] markStrArr){
		String sql="select * from "+ AnUtils.getTableAnnoName(t) +" "+AnUtils.replaceByAnnoColumn(whereStr, t);
		List<T> list = helper.queryToEntity(t, sql, markStrArr, true,getTranDatabase());
		if (printLog) {
			System.out.println("sql:"+sql);
			for (T t2 : list) {
				PrintUtils.printAllField(t2);
			}
		}
		return list;
	}
	/**
	 * @param t
	 * @param id
	 * @return
	 */
	public <T> T queryEntityById(Class<T> t,String id){
		String sql="select * from "+AnUtils.getTableAnnoName(t)+" where id=?";
		List<T> list = helper.queryToEntity(t, sql, new String[]{id}, true,getTranDatabase());
		if(list.size()>1){
			try {
				throw new IllegalStateException("id have same is not possible!");
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		if (list.size() == 1) {
			if (printLog) {
				System.out.println("sql:"+sql);
				PrintUtils.printAllField(list.get(0));
			}
			return list.get(0);
		} else
			return null;
		
	}
	/**
	 * @param t
	 * @return
	 */
	public <T> List<T> queryAllByClass(Class<T> t){
		String sql="select * from "+AnUtils.getTableAnnoName(t);
		List<T> list = helper.queryToEntity(t, sql, new String[]{}, true,getTranDatabase());
		if (printLog) {
			System.out.println("sql:"+sql);
			for (T t2 : list) {
				PrintUtils.printAllField(t2);
			}
		}
		return list;
	}
	/**
	 * @param t
	 */
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
			String sql="select * from "+AnUtils.getTableAnnoName(t.getClass())+" where id=?";
			if (printLog) {
				System.out.println("sql:" + sql);
			}
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
	/**
	 * @param t
	 */
	public <T> void addEntity(T t){
		// String
		// sql="insert into "+MyParameters.TABLE_NAME+"(name, sex) values(?,?)";
		// db.execSQL(sql, new Object[]{person.getName(),person.getSex()});
		// db.execSQL(sql, object);
		// db.close();
		StringBuilder sb=new StringBuilder(50);
		sb.append("insert into ");
		sb.append(AnUtils.getTableAnnoName(t.getClass())+" ");
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
					columnSb.append(AnUtils.getAnnoColumn_Name_ByField(field, t.getClass()));
					markSb.append("?");
				}else{
					columnSb.append(","+AnUtils.getAnnoColumn_Name_ByField(field, t.getClass()));
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
		if (printLog) {
			System.out.println("sql:" + sql);
		}
		Object[] object=columeValue.toArray();
		helper.Operating(sql, object,getTranDatabase());
	}
	/**
	 * @param t
	 */
	public <T> void UpdateEntity(T t){
		// SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "update " + MyParameters.TABLE_NAME
		// + " set name = ?,sex=?  where _id = ?;";
		// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id
		// });
		// db.close();
		StringBuilder sb=new StringBuilder(50);
		sb.append("update ");
		sb.append(AnUtils.getTableAnnoName(t.getClass())+" set ");
		Field[] fields = t.getClass().getDeclaredFields();
		int i=0;
		List<Object> columeValue=new ArrayList<Object>();
		//TODO ���ֶ������� ��һ������  δ��
		for (Field field : fields) {
			if(!"id".equals(field.getName())){
				field.setAccessible(true);
				if (i==0) 
					sb.append(AnUtils.getAnnoColumn_Name_ByField(field, t.getClass())+"=?");
				else
					sb.append(","+AnUtils.getAnnoColumn_Name_ByField(field, t.getClass())+"=?");
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
		if (printLog) {
			System.out.println("sql:" + sql);
		}
		Object[] object=columeValue.toArray();
		helper.Operating(sql, object,getTranDatabase());
	}
	/**
	 *  " set name = ?,sex=?  where _id = ?;";
	 * db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id});
	 * @param t
	 * @param Set_Where_Str
	 * @param markStrArr
	 */
	public <T> void UpdateByCondition(Class<T> t,String Set_Where_Str,String[] markStrArr){
		// SQLiteDatabase db = helper.getWritableDatabase();
		// String sql = "update " + MyParameters.TABLE_NAME
		// + " set name = ?,sex=?  where _id = ?;";
		// db.execSQL(sql, new Object[] { person.getName(), person.getSex(), id});
		// db.close();
		String sql="update   "+AnUtils.getTableAnnoName(t)+" "+AnUtils.replaceByAnnoColumn(Set_Where_Str, t);
		if (printLog) {
			System.out.println("sql:" + sql);
		}
		helper.Operating(sql,markStrArr,getTranDatabase());
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
		try {
			if(id==null)
				throw new IllegalArgumentException("id may be null");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		System.err.println("id:"+id);
		String whereStr=" where id=? ";
		String sql="delete  from "+AnUtils.getTableAnnoName(t.getClass())+" "+whereStr;
		if (printLog) {
			System.out.println("sql:" + sql);
		}
		Object[] markStrArr=new Object[]{id};
		helper.Operating(sql, markStrArr,getTranDatabase());
	}

	public <T> void removeEntityByCondition(Class<T> t,String whereStr,String[] markStrArr){
		String sql="delete  from "+AnUtils.getTableAnnoName(t)+" "+	AnUtils.replaceByAnnoColumn(whereStr, t);
		if (printLog) {
			System.out.println("sql:" + sql);
		}
		helper.Operating(sql, markStrArr,getTranDatabase());
	}
	public <T> void removeAllByClass(Class<T> t){
		String sql="delete  from "+AnUtils.getTableAnnoName(t)+" ";
		if (printLog) {
			System.out.println("sql:" + sql);
		}
		helper.Operating(sql,new Object[]{},getTranDatabase());
	}

	public <T> void dropTableByClass(Class<T> t){
		// ----------------------------------ɾ������-------------------------------------------------
				//delete from table �������ȫ������
				// ʹ��db.execSQL("delete from table",null);���Ǳ���?  
				// ԭ����db.execSQL("delete from table",null);�ڶ�����������Ϊnull������new
				// Object[]{}��OK�ˡ�
				// db.execSQL("DROP TABLE IF EXISTS students"); //�������
		String sql="DROP TABLE IF EXISTS "+AnUtils.getTableAnnoName(t)+" ";
		if (printLog) {
			System.out.println("sql:" + sql);
		}
		helper.Operating(sql,new Object[]{},getTranDatabase());
	}
	//TODO ͨ��������   δʵ��
	public  <T> void createTableByEntity(Class<T> t){
		helper.createTableByEntity(t,getTranDatabase());
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  <T> void createTableByList(List<Class> classList){
		for (Class class1 : classList) {
			helper.createTableByEntity(class1,getTranDatabase());
		}
	}
	public <T> void addColumn(Class<T> t,String willAddColumnStr){
		helper.addColumn(t, willAddColumnStr,getTranDatabase());
	}
	/**
	 * 
	 * @param t	
	 * @param column_old  id��Ҫ��  ��� ����Ҫ�ı� �ֶ� name sex�Ǿ�new String[]{"name","sex"}
	 * @param column_target   ��� name ��ɾ�� sex��ı�� hex �Ǿ�new String[]{"","hex"}
	 */
	public <T> void updateOrDeleteColumn(Class<T> t,String[] column_old,String[] column_target) {
//		helper.column_updateOrDelete(t, column_old, column_target,getTranDatabase());
	}
	public static boolean getPrintLog(){
		return printLog;
	}
	public static void setPrintLog(boolean printLog){
		Sqlite_Utils.printLog=printLog;
	}
	public static int getVersion() {
		return version;
	}
	public static void setVersion(int version) {
		Sqlite_Utils.version = version;
	}
}
