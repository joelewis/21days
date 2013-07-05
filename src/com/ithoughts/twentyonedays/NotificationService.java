
package com.ithoughts.twentyonedays;


import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;



// Use IntentService which will queue each call to startService(Intent) through onHandleIntent and then shutdown
//
// NOTE that this implementation intentionally doesn't use PowerManager/WakeLock or deal with power issues
// (if the device is asleep, AlarmManager wakes up for BroadcastReceiver onReceive, but then might sleep again)
// (can use PowerManager and obtain WakeLock here, but STILL might not work, there is a gap)
// (this can be mitigated but for this example this complication is not needed)
// (it's not critical if user doesn't see new deals until phone is awake and notification is sent, both)

public class NotificationService extends IntentService {

   //private static int count;



   public NotificationService() {
      super("Deal Service");
   }

   @Override
   public void onStart(Intent intent, int startId) {
      super.onStart(intent, startId);
    //  Intent intent1 = new Intent(this, StatusNotify.class);
      //startActivity(intent1);  
   }

   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@Override
   public void onHandleIntent(Intent intent) {
	       Log.i(Constants.LOG_TAG, "DealService invoked, checking for new deals (will notify if present)");
	            //Toast.makeText(getBaseContext(), "..." , Toast.LENGTH_LONG).show();


	        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    int unique_id = 145558;
		    Log.i("status bar" +unique_id , " ");
		   
		    	String title = "New Subjects Today!";
		    	String body = "Foo";
		    	
		    	Intent nintent = new Intent();
		    	nintent.setClass(this, LaunchActivity.class);
		    	PendingIntent pin = PendingIntent.getActivity(getApplicationContext(),0, nintent, 0);
		    	Notification n = new Notification(R.drawable.ic_launcher, body, System.currentTimeMillis());			    
		    
		    	n.contentIntent = pin;
		    	n.setLatestEventInfo(getApplicationContext(), title, body, pin);
		    	n.defaults = Notification.DEFAULT_ALL;
		    	n.flags |= Notification.FLAG_AUTO_CANCEL;
		 	   nm.notify(unique_id, n); 	  
		    }
		    	    
   }
   


        

/*
   
   private void sendNotification(Context context, int numNewDeals) {
      Intent notificationIntent = new Intent(context, null);
      notificationIntent.putExtra(Constants.FORCE_RELOAD, true);
      PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

      NotificationManager notificationMgr =
               (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      Notification notification =
               new Notification(android.R.drawable.star_on, "New Notification", System
                        .currentTimeMillis());
      notification.flags |= Notification.FLAG_AUTO_CANCEL;
      notification.setLatestEventInfo(context, "Title", "nothing", contentIntent);
      notificationMgr.notify(0, notification);
   } */

