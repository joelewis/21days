package com.ithoughts.twentyonedays;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;




public class DataShop {
	
	
	  private SQLiteDatabase database;
	  private DbHelper dbhelper;
	  
	  
	  public String[] TableTasksAllColumns = { DbHelper.COLUMN_ID, DbHelper.COLUMN_TASK, DbHelper.COLUMN_TIME, DbHelper.COLUMN_INITDATE, DbHelper.COLUMN_PRIORITY };
	  
	  public String[] TableTrackerAllColumns = { DbHelper.COLUMN_NID, DbHelper.COLUMN_TID1, DbHelper.COLUMN_CURDATE, DbHelper.COLUMN_DAYOFYEAR, DbHelper.COLUMN_STATUS, DbHelper.COLUMN_DAYOFMONTH, DbHelper.COLUMN_DAYOFWEEK, DbHelper.COLUMN_MONTH, DbHelper.COLUMN_YEAR };

	  public String[] TableTaskDaysAllColumns = { DbHelper.COLUMN_AID, DbHelper.COLUMN_TID, DbHelper.COLUMN_DAYS, DbHelper.COLUMN_WEEKDAY };

	  public DataShop(Context context){
		  dbhelper = new DbHelper(context);
		  }
		  
	  public void open() throws SQLException {
			  database = dbhelper.getWritableDatabase();
		  }
		  
      public void close(){
			  dbhelper.close();
		  }
      
      private long fetchRowCount() {
  	    String sql = "SELECT COUNT(*) FROM " + DbHelper.TABLE_TASKS;
  	    SQLiteStatement statement = database.compileStatement(sql);
  	    return (int) statement.simpleQueryForLong();
  	  }
    
      
      public TaskPlus add_another_task(String taskName, long alarmTime, long initDate, boolean[] days) {
    	  ContentValues values = new ContentValues();
    	  values.put(DbHelper.COLUMN_TASK, taskName);
    	  values.put(DbHelper.COLUMN_TIME, alarmTime);
    	  values.put(DbHelper.COLUMN_INITDATE, initDate);
    	  long count = fetchRowCount();
    	  //Log.i("prio:"+prio,"");
    	  values.put(DbHelper.COLUMN_PRIORITY, count+1);
    	  long rowid;
          rowid = database.insert(DbHelper.TABLE_TASKS, null, values);
    	  ContentValues values1 = new ContentValues();
    	  for(int i=0; i<days.length; i++) {	
    	    values1.put(DbHelper.COLUMN_TID, rowid);  
    		if(days[i] == true)  {
    			values1.put(DbHelper.COLUMN_DAYS, 1);
    			values1.put(DbHelper.COLUMN_WEEKDAY, i+1);
    		} else {
    			values1.put(DbHelper.COLUMN_DAYS, 0);
    			values1.put(DbHelper.COLUMN_WEEKDAY, i+1);
    		}
    		
    	  	database.insert(DbHelper.TABLE_TASKDAYS, null, values1);
    	  }
    	  TaskPlus task = new TaskPlus();
    	  task.name = taskName;
    	  task.id = (int) rowid;
    	  task.dotColors = get_task_status_for_week(task.id);
    	  for(int i=0; i<task.dotColors.length; i++) {
    		  System.out.println("dotColor[ " + i + " " + task.dotColors[i] );
    	  }
    	  return task;
      }
      
      public void update_task(String taskName, long alarmTime, long initDate, boolean[] days, int id) {
    	  ContentValues values = new ContentValues();
    	  values.put(DbHelper.COLUMN_TASK, taskName);
    	  values.put(DbHelper.COLUMN_TIME, alarmTime);

    	  database.update(DbHelper.TABLE_TASKS, values, " _id = " + id, null);
    	  ContentValues values1 = new ContentValues();
    	  for(int i=0; i<days.length; i++) {	
    		if(days[i] == true)  {
    			values1.put("days", 1);
    			
    		} else {
    			values1.put("days", 0);   			
    		}
    		database.update(DbHelper.TABLE_TASKDAYS, values1, " tid = " + id + " and " + " weekday = " + (i+1), null);
    		
    	  	//database.update(DbHelper.TABLE_TASKDAYS, values1, " tid = " + id, null);
    	  	System.out.println("updated where tid = " + id);
    	  }
    	  /*
    	  TaskPlus task = new TaskPlus();
    	  task.name = taskName;
    	  task.id = (int) rowid;
    	  task.dotColors = get_task_status_for_week(task.id);
    	  for(int i=0; i<task.dotColors.length; i++) {
    		  System.out.println("dotColor[ " + i + " " + task.dotColors[i] );
    	  }
    	  return task;  */
      }
      
      public String getTaskNameById(int id) {
    	 
    	  Cursor cursor = database.query(DbHelper.TABLE_TASKS, TableTasksAllColumns,  " _id = " + id, null, null, null, null);    	   
    	  System.out.println("datashop: " +cursor.getCount());
    	  cursor.moveToFirst();
    	  int i = cursor.getColumnIndex("task");
    	  String name = cursor.getString(i);
    	  return name;
      }
      
      public long getTaskAlarmTimeById(int id) {
    	  
    	  Cursor cursor = database.query(DbHelper.TABLE_TASKS, TableTasksAllColumns, DbHelper.COLUMN_ID + " = " + id, null, null, null, null);
    	  cursor.moveToFirst();
    	  long ms; 
    	  ms = cursor.getLong(2);
    	  return ms;
      }
      
      public String[] getTaskDaysById(int id) {
    	  String[] days = new String[7];
    	  Cursor cursor = database.query(DbHelper.TABLE_TASKDAYS, TableTaskDaysAllColumns, DbHelper.COLUMN_TID + " = " + id, null, null, null, null);
    	  cursor.moveToFirst();
    	  for(int i=0; i<cursor.getCount(); i++) {
    		  switch(cursor.getInt(3)) {
    		  case 1:
    			  days[i] = "Sunday"; break;
    		  case 2:
    			  days[i] = "Monday"; break;
    		  case 3:
    			  days[i] = "Tuesday"; break;
    		  case 4:
    			  days[i] = "Wednesday"; break;
    		  case 5:
    			  days[i] = "Thursday"; break;
    		  case 6:
    			  days[i] = "Friday"; break;
    		  case 7:
    			  days[i] = "Saturday"; break;
    		  }
    		  if(!cursor.isLast()) {
    			  cursor.moveToNext();
    		  }
    	  }
    	  return days;
      }
      
      public int[] getValidDays(int id) {
    	  Cursor cursor = database.query(DbHelper.TABLE_TASKDAYS, TableTaskDaysAllColumns, DbHelper.COLUMN_TID + " = " + id, null, null, null, null);
    	  int[] valids = new int[7];
    	  cursor.moveToFirst();
    	  for(int i=0; i < cursor.getCount(); ++i) {
    		  valids[i] = cursor.getInt(2);
    		  System.out.println("" + valids[i]);
    		  if(!cursor.isLast()) {
    			  cursor.moveToNext();
    		  }
    	  }
    	  return valids;
      }
      
      public int getDayStatus(int id, int dayofweek) {
    	  System.out.println("id: " + id + ", dayofweek: " + dayofweek);
    	  Cursor cursor = database.query(DbHelper.TABLE_TASKDAYS, TableTaskDaysAllColumns, DbHelper.COLUMN_TID + " = " + id + " and " + DbHelper.COLUMN_WEEKDAY + " = " + dayofweek, null, null, null, null);
    	  cursor.moveToFirst();
    	  return cursor.getInt(2);
      }
      
      public void delete_task(int id) {
    	  database.delete(DbHelper.TABLE_TASKS, " _id = "+id, null);
      }
		
      
      public ArrayList<TaskPlus> get_all_tasks() {
    	 
    	  ArrayList<TaskPlus> tasks = new ArrayList<TaskPlus>();
    	  Cursor cursor = database.query(DbHelper.TABLE_TASKS, TableTasksAllColumns, null, null, null, null, DbHelper.COLUMN_PRIORITY+" DESC");
    	  cursor.moveToFirst();
    	  
    	  for(int i=0; i<cursor.getCount(); ++i) {
    		  TaskPlus task = new TaskPlus();
    		  task.id = cursor.getInt(0);
    		  task.name = cursor.getString(1);
    		  task.dotColors = get_task_status_for_week(task.id);
    		  for(int j=0; j<task.dotColors.length; j++) {
        		  System.out.println("dotColor[ " + j + " - " + task.dotColors[j] );
        	  }
    		  tasks.add(task);
    		  if(!cursor.isLast()) {
    		  cursor.moveToNext();
    		  }
    	  }
    	  
    	  return tasks;
      }
      
      public ArrayList<TaskWithAlarm> get_all_tasks_with_alarm() {
     	 
    	  ArrayList<TaskWithAlarm> tasks = new ArrayList<TaskWithAlarm>();
    	  Cursor cursor = database.query(DbHelper.TABLE_TASKS, TableTasksAllColumns, null, null, null, null, DbHelper.COLUMN_PRIORITY+" DESC");
    	  cursor.moveToFirst();    	  
    	  for(int i=0; i<cursor.getCount(); ++i) {
    		  TaskWithAlarm task = new TaskWithAlarm();
    		  task.id = cursor.getInt(0);
    		  task.ms = cursor.getLong(2);
    		  tasks.add(task);
    		  if(!cursor.isLast()) {
    		  cursor.moveToNext();
    		  }
    	  }
    	  
    	  return tasks;
      }
      

      public int[] get_task_status_for_week( int id ) {
    	  int[] dotColors = new int[500];
    	  
    	  int dayofyear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    	  Cursor cursor = database.query(DbHelper.TABLE_TRACKER, TableTrackerAllColumns, DbHelper.COLUMN_TID1 + " = " + id , null, null, null, null);
    	  cursor.moveToFirst();
    	  for(int i=0; i<cursor.getCount(); ++i) {
    		  if(cursor.getInt(4) == 1) {
    			  dotColors[i] = R.drawable.presence_online;
    		  } else if(cursor.getInt(4) == 0) {
    			  dotColors[i] = R.drawable.presence_busy;
    		  } else {
    			  dotColors[i] = R.drawable.presence_invisible;
    		  }
       		  if(!cursor.isLast()) {
    			  cursor.moveToNext();
    		  }
    	  }
    	  System.out.println("count "+ cursor.getCount() + " dotColors: ");
    	  
    	  int[] stats = new int[7];    	  
    	  if(cursor.getCount() < 7) {
    		  int i=0;    		  
    		  
    		  for(i=0; i<cursor.getCount(); i++) {
    			  
    			  stats[i] = dotColors[i];
    			  
    		  }
    		  
    		  for(int k=cursor.getCount(); k<7; k++){
    			  stats[k] = R.drawable.presence_invisible;
    		  }
    		  
    	  } else {
    		  int j=0;
    		  for(int i = (cursor.getCount() - 7); i < cursor.getCount(); i++) {
    			  System.out.println("i: " + dotColors[i]);
    			  stats[j] = dotColors[i];
    			  j++;
    		  }
    	  }
    	  
    	  return stats;
    		
    	  
     }
      
      public GraphObject get_task_status_for_graph( int id ) {
    	  GraphObject object = new GraphObject();
    	  int days, hits=0, misses=0;

    	  int dayofyear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    	  int[] dotColors = new int[500];
    	  int i=0;
    	  
    	  Cursor cursor = database.query(DbHelper.TABLE_TRACKER, TableTrackerAllColumns, DbHelper.COLUMN_TID1 + " = " + id, null, null, null, null);
    	  days = cursor.getCount();
    	  cursor.moveToFirst();
    	  
    	  for(i=0; i<cursor.getCount(); ++i) {
    		  if(cursor.getInt(4) == 1) {
    			  dotColors[i] = R.drawable.presence_online;
    			  ++hits;
    		  } else if(cursor.getInt(4) == 0) {
    			  dotColors[i] = R.drawable.presence_busy;
    			  ++misses;
    		  } else {
    			  dotColors[i] = R.drawable.presence_invisible;
    		  }
    		  
    		  
    		  if(!cursor.isLast()) {
    			  cursor.moveToNext();
    		  }
    	  }
    	  
    	  
    	  for(int k=i; k<500; k++) {
    		  dotColors[k] = R.drawable.presence_invisible;
    	  }
    	  
    	  
    	  System.out.println("Size of stats: " + dotColors.length);
    	  object.dotColors = dotColors;
    	  object.days_from_genesis = days;
    	  object.hits = hits;
    	  object.misses = misses;
    	  return object;
      }
      
      public int get_stat_for_day(  int id, int dayofyear) {
    	  int prestat;
    	  Cursor cursor = database.query(DbHelper.TABLE_TRACKER, TableTrackerAllColumns, DbHelper.COLUMN_TID1 + " = " + id + " and " + DbHelper.COLUMN_DAYOFYEAR + " = " + dayofyear, null, null, null, null);
    	  if(cursor.getCount() == 0) {
    		  prestat = 2;
    	  } else {
    		  cursor.moveToFirst();
    		  if(cursor.getInt(4) == 1) {
    			  prestat = 1;
    		  } else {
    			  prestat = 0;
    		  }
    	  }
    	  return prestat;
      }
      
      public void update_ifnot_insert_stat( int id, int stat ) {
    	  int dayofyear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    	  Cursor cursor = database.query(DbHelper.TABLE_TRACKER, TableTrackerAllColumns, DbHelper.COLUMN_TID1 + " = " + id + " and " + DbHelper.COLUMN_DAYOFYEAR + " = " + dayofyear, null, null, null, null);
    	  ContentValues values = new ContentValues();

    	  if(cursor.getCount() > 0) {
    		  values.put(DbHelper.COLUMN_STATUS, stat);
    		
    		  database.update(DbHelper.TABLE_TRACKER, values, DbHelper.COLUMN_DAYOFYEAR + " = " + dayofyear + " and " + DbHelper.COLUMN_TID1 + " = " + id, null);
    		  System.out.println("updated status");
    	  }
    	  else {
    		  Calendar cal = Calendar.getInstance();
    		  values.put(DbHelper.COLUMN_TID1, id);
    		  values.put(DbHelper.COLUMN_CURDATE, cal.getTimeInMillis());
    		  values.put(DbHelper.COLUMN_DAYOFYEAR, dayofyear);
    		  values.put(DbHelper.COLUMN_STATUS, stat);
    		  values.put(DbHelper.COLUMN_DAYOFMONTH, cal.get(Calendar.DAY_OF_MONTH));
    		  values.put(DbHelper.COLUMN_DAYOFWEEK, cal.get(Calendar.DAY_OF_WEEK));
    		  values.put(DbHelper.COLUMN_MONTH, cal.get(Calendar.MONTH));
    		  values.put(DbHelper.COLUMN_YEAR, cal.get(Calendar.YEAR));

    		  database.insert(DbHelper.TABLE_TRACKER, null, values);
    		  System.out.println("inserted status");
    	  
    	  }
      }
      
      
      public void update_ifnot_insert_stat( int id, int stat, int dayofyear, Calendar cal ) {
    	  //int dayofyear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    	  Cursor cursor = database.query(DbHelper.TABLE_TRACKER, TableTrackerAllColumns, DbHelper.COLUMN_TID1 + " = " + id + " and " + DbHelper.COLUMN_DAYOFYEAR + " = " + dayofyear, null, null, null, null);
    	  
    	  ContentValues values = new ContentValues();

    	  if(cursor.getCount() > 0) {
    		  values.put(DbHelper.COLUMN_STATUS, stat);
    		
    		  database.update(DbHelper.TABLE_TRACKER, values, DbHelper.COLUMN_DAYOFYEAR + " = " + dayofyear + " and " + DbHelper.COLUMN_TID1 + " = " + id, null);
    		  System.out.println("updated status");
    	  }
    	  else {
    		  //Calendar cal = Calendar.getInstance();
    		  values.put(DbHelper.COLUMN_TID1, id);
    		  values.put(DbHelper.COLUMN_CURDATE, cal.getTimeInMillis());
    		  values.put(DbHelper.COLUMN_DAYOFYEAR, dayofyear);
    		  values.put(DbHelper.COLUMN_STATUS, stat);
    		  values.put(DbHelper.COLUMN_DAYOFMONTH, cal.get(Calendar.DAY_OF_MONTH));
    		  values.put(DbHelper.COLUMN_DAYOFWEEK, cal.get(Calendar.DAY_OF_WEEK));
    		  values.put(DbHelper.COLUMN_MONTH, cal.get(Calendar.MONTH));
    		  values.put(DbHelper.COLUMN_YEAR, cal.get(Calendar.YEAR));

    		  database.insert(DbHelper.TABLE_TRACKER, null, values);
    		  System.out.println("inserted status");
    	  
    	  }
      }
      
      
      public void deleteMultipleTasks(long[] taskIds) {
    	  for (int id=0; id<taskIds.length; id++) {
    	  database.delete(DbHelper.TABLE_TASKS, DbHelper.COLUMN_ID + " = " + taskIds[id], null);
    	  }
      }

      public void deleteTaskDays(long[] taskIds)
      {
    	  for (int id=0; id<taskIds.length; id++){
    		  database.delete(DbHelper.TABLE_TASKDAYS, DbHelper.COLUMN_TID + "=" + taskIds[id], null);
    	  }
      }

      public void deleteNotify(long taskIds[])
      {
    	  for(int id=0; id<taskIds.length;id++){
    		  database.delete(DbHelper.TABLE_TRACKER, DbHelper.COLUMN_TID1 + "=" + taskIds[id], null);
    	  }
      }
      
      
		  
	}

	
