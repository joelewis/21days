package com.ithoughts.twentyonedays;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {


  public static final String TABLE_TASKS = "tasks";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_TASK = "task";
  public static final String COLUMN_TIME = "time";
  public static final String COLUMN_INITDATE = "initdate";
  public static final String COLUMN_PRIORITY="priority";
  
  public static final String TABLE_TASKDAYS = "taskdays";
  public static final String COLUMN_AID = "_aid";
  public static final String COLUMN_TID = "tid";
  public static final String COLUMN_DAYS= "days";
  public static final String COLUMN_WEEKDAY= "weekday";

  
  public static final String TABLE_TRACKER = "tracker";
  public static final String COLUMN_NID = "_nid";
  public static final String COLUMN_TID1 = "tid1";
  public static final String COLUMN_CURDATE = "cur_date";
  public static final String COLUMN_DAYOFYEAR = "day_of_year";
  public static final String COLUMN_STATUS = "status";
  public static final String COLUMN_DAYOFMONTH = "day_of_month";
  public static final String COLUMN_DAYOFWEEK = "day_of_week";
  public static final String COLUMN_MONTH = "month";
  public static final String COLUMN_YEAR = "year";
  
  private static final String DATABASE_NAME = "tasks.db";
  //private static final String DATABASE2_NAME = "alarm_repeat.db";

  private static final int DATABASE_VERSION = 1;
  
  private static final String TABLE_TASKS_CREATE = "create table " + TABLE_TASKS + "("
	+ COLUMN_ID + " integer primary key autoincrement, "+ COLUMN_TASK
	+ " text not null, "+ COLUMN_TIME+" bigint not null, "+ COLUMN_INITDATE+" bigint not null, "+ COLUMN_PRIORITY +" integer );";
  
  private static final String TABLE_TRACKER_CREATE = "create table tracker(_nid integer primary key autoincrement, tid1 integer FOREIGNKEY REFERENCES tasks ( _id ), cur_date bigint not null, day_of_year int not null, status integer not null, day_of_month int not null, day_of_week int not null, month int not null, year int not null) ";
  /*private static final String DATABASE2_CREATE = "create table " + TABLE_REPEAT + "("
			+ COLUMN_AID + " integer primary key autoincrement, "+  " FOREIGN KEY (" + COLUMN_TID + ") REFERENCES " + TABLE_TASKS + " ( " + COLUMN_ID + " ), "+ COLUMN_DAYS + " text not null );";
 */
  private static final String TABLE_TASKDAYS_CREATE = "create table taskdays(_aid integer primary key autoincrement, tid integer FOREIGNKEY REFERENCES tasks ( _id ), days integer not null, weekday integer not null )";

  public DbHelper(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
  
 /* public MySqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
*/
  

  
@Override
  public void onCreate(SQLiteDatabase database) {
	  database.execSQL(TABLE_TASKS_CREATE);
	  database.execSQL(TABLE_TRACKER_CREATE);
	  database.execSQL(TABLE_TASKDAYS_CREATE);
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  Log.w(DbHelper.class.getName(),
			  "Upgrading database from version" + oldVersion + "to" + newVersion + 
			  ", which will destroy all old data");
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS );
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKDAYS );
	  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKER );

	  onCreate(db);
  }
  
}
