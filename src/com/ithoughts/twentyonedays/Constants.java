package com.ithoughts.twentyonedays;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Constants {

	

	
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
	   


}
