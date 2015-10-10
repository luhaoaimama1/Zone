package Android.Zone.Sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import Android.Zone.Sqlite.Annotation.utils.AnUtils;
import Android.Zone.Utils.SharedUtils;
import Java.Zone.Log.PrintUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//TODO 实体类id字段的设计   实体类中包含实体类的操作
/**
 * 想要测试注解 就需要注解和字段完全不一样 然后打印每个方法里的sql看看对不对就OK了
 * 
 * 因为此工具通过反射做的 所以 set get 和参数都是系统生成的那种 如果不是会报错
 * 必须有id  暂时支持String
 * 实体中包含实体 未实现  
 * 通过字段更改更改表未实现  
 * @author Zone
 */
public class Sqlite_Utils {
	private static String DB_NAME = "Zone.db"; // 数据库名称
	//因为一个 应用就一个数据库 所以版本号  静态就OK了
	private  static  int version=1;//默认第一个版本是1  
	private  static final  String SHARE_TAG="zone_db_version";
	private Sqlite_Helper helper;
	private static Sqlite_Utils instance=null;
	private static OnUpgrade onUpgrade=null;
	private static OnCreate onCreate=null;
	private static boolean printLog=false;
	private SQLiteDatabase tranSQLiteDatabase=null;
	/**
	 * 
	 * @param context 只是不同的 SQLiteOpenHelper 但是一个应用就是一个数据库
	 */
	private  Sqlite_Utils(Context context) {
		helper=new Sqlite_Helper(DB_NAME, context,1);
	}
	/**
	 * 只要注册监听   就会 创建表  和监听版本切换 
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
	 * @param instance2  别改成instance  因为私有变量有instance了。。。
	 */
	private static  void onUpgrade(Context context, Sqlite_Utils instance2){
		int oldVersion=SharedUtils.getInstance(context).readSp().getInt(SHARE_TAG, 0);
		SharedUtils.getInstance(context).writeSp().putInt(SHARE_TAG, version).commit();
		if(oldVersion!=version&&oldVersion!=0)
			onUpgrade.onUpgrade(oldVersion, version,instance2);
	}
	public  interface OnUpgrade{
		/**
		 * TODO  这里是 每次升级的时候   所有实体类自动 在数据库中 变换
		 * @param oldVersion
		 * @param newVersion
		 * @param instance2
		 */
		public  void onUpgrade(int oldVersion, int newVersion, Sqlite_Utils instance2);
	}
	public  interface OnCreate{
		/**
		 * TODO 每次initListener的时候  建立表 不管表内部的变换
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
			// 结束事务
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
	 * 经证明 context 公用一个数据库  Sqlite_Utils 也是一个因为我用的是单例模式
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
			//添加操作
			addEntity(t);
		}else{
			//看看ID重复没 
			String sql="select * from "+AnUtils.getTableAnnoName(t.getClass())+" where id=?";
			if (printLog) {
				System.out.println("sql:" + sql);
			}
			int count = helper.queryCountById(sql, new String[]{id}, true);
			if(count==0){
				//不重复就添加
				addEntity(t);
			}else{
				//重复就修改
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
		//TODO 当字段有问题 是一个类呢  未作
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
		//TODO 当字段有问题 是一个类呢  未作
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
		// ----------------------------------删除表范例-------------------------------------------------
				//delete from table 输出表中全部数据
				// 使用db.execSQL("delete from table",null);总是报错?  
				// 原因是db.execSQL("delete from table",null);第二个参数不能为null，传递new
				// Object[]{}就OK了。
				// db.execSQL("DROP TABLE IF EXISTS students"); //这个好用
		String sql="DROP TABLE IF EXISTS "+AnUtils.getTableAnnoName(t)+" ";
		if (printLog) {
			System.out.println("sql:" + sql);
		}
		helper.Operating(sql,new Object[]{},getTranDatabase());
	}
	//TODO 通过包生成   未实现
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
	 * @param column_old  id不要变  别的 例如要改变 字段 name sex那就new String[]{"name","sex"}
	 * @param column_target   如果 name 想删除 sex想改变成 hex 那就new String[]{"","hex"}
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
