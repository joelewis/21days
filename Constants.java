package com.joe.timetablelite;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Constants {
	public static ArrayList<String> monday  = new ArrayList<String>();
	public static ArrayList<String> tuesday  = new ArrayList<String>();
	public static ArrayList<String> wednesday  = new ArrayList<String>();
	public static ArrayList<String> thursday  = new ArrayList<String>();
	public static ArrayList<String> friday  = new ArrayList<String>();
	
	public static ArrayList<String> autosuggest = new ArrayList<String>();
	
	public static int noOfPeriods;
	
	 public static final String title = "Habitator";
	   public static final String LOG_TAG = "Habitator";

	   public static final String FORCE_RELOAD = "FORCE_RELOAD";
	   public static int hourOfDay = 04;
	   public static int minutes = 22;
	   public static int seconds = 00;
	   public static Context context;
	   

	   // In real life, use AlarmManager.INTERVALs with longer periods of time 
	   // for dev you can shorten this to 10000 or such, but deals don't change often anyway
	   // (better yet, allow user to set and use PreferenceActivity)
	   //uncheck next line for using while developing it will trigger broadcast reciever every 3 seconds
	   //public static final long ALARM_INTERVAL = 10000;
	  // public static final long ALARM_INTERVAL = AlarmManager.INTERVAL_DAY;
	   public static final long ALARM_INTERVAL = AlarmManager.INTERVAL_DAY;
	   public static final long ALARM_TRIGGER_AT_TIME =  2000;
	   
	   public static void getSuggestions() {
		   TimeTableDbHelper mDbHelper;
			SQLiteDatabase db; 
			mDbHelper =  new TimeTableDbHelper(context);
			db = mDbHelper.getWritableDatabase();
			for(int j=0; j<5; j++) {
				Cursor cursor = db.query(TimeTableDbHelper.TABLE_NAME_TIMETABLE, TimeTableDbHelper.allColumns_mod, "id = " + (j+1), null, null, null, null);
			for(int i=1; i<cursor.getColumnCount(); i++) {
        		Log.i("data", cursor.getString(i));
        		if(cursor.getString(i).equalsIgnoreCase("nil")) {
        		} else {
        			if(autosuggest.contains(cursor.getString(i))) {
        				
        			} else {
        		    autosuggest.add(cursor.getString(i));
        			}
        		}
        		}
			
			}
	   }

}
