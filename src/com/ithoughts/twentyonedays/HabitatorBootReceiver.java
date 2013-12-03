package com.ithoughts.twentyonedays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class HabitatorBootReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {
	   
	   //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
       //long syncConnPref = sharedPref.getLong("timepicker", Calendar.getInstance().getTimeInMillis());
       
       //Calendar cal_alarm = Calendar.getInstance();
       //cal_alarm.setTimeInMillis(syncConnPref);
       DataShop datashop = new DataShop(context);
       datashop.open();
       ArrayList<TaskWithAlarm> tasks = new ArrayList<TaskWithAlarm>();
       tasks = datashop.get_all_tasks_with_alarm();
       datashop.close();
       
       for(int i=0; i<tasks.size(); i++) {
    	   Calendar cal_now = Calendar.getInstance();
    	   Calendar cal_alarm = Calendar.getInstance();
    	   long ms = tasks.get(i).ms;
    	   int id = tasks.get(i).id;
    	   cal_now.setTimeInMillis(ms);
    	   cal_alarm.set(Calendar.HOUR_OF_DAY, cal_now.get(Calendar.HOUR_OF_DAY));
    	   cal_alarm.set(Calendar.MINUTE, cal_now.get(Calendar.MINUTE));
    	   scheduleAlarmReciever(context, cal_alarm, id);
       }
   }
   
   public void scheduleAlarmReciever(Context context, Calendar cal_alarm, int id) {
	   Log.i(Constants.LOG_TAG, "DealBootReceiver invoked, configuring AlarmManager");
       AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
       Intent intent = new Intent(context, HabitatorAlarmReceiver.class);
       intent.putExtra("id", id);
       PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
       Log.i("bootReceiver", " ");
      // use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
      Date dat  = new Date();//initializes to now
      //Calendar cal_alarm = Calendar.getInstance();
      Calendar cal_now = Calendar.getInstance();
      cal_now.setTime(dat);
      //cal_alarm.setTime(dat);
      //cal_alarm.setTimeZone(TimeZone.getTimeZone("GMT"));
      //cal_alarm.set(Calendar.HOUR_OF_DAY, Constants.hourOfDay);//set the alarm time
      //cal_alarm.set(Calendar.MINUTE, Constants.minutes);
      //cal_alarm.set(Calendar.SECOND, Constants.seconds);
      if(cal_alarm.before(cal_now)){//if its in the past increment
          cal_alarm.add(Calendar.DATE,1);
    	  Log.i("","cal_alarm is in past");
      }
      alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
   }
}
