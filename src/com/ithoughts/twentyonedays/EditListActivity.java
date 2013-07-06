package com.ithoughts.twentyonedays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.ithoughts.twentyonedays.LaunchActivity.AddTask;
import com.ithoughts.twentyonedays.LaunchActivity.UpdateTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class EditListActivity extends Activity {

	
	DataShop datashop;
	Context context = this;
	ListView listView;
	int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_list);
		// Show the Up button in the action bar.
		setupActionBar();
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Edit Activity");
		actionBar.setSubtitle("Touch activity to edit");
		id = getIntent().getExtras().getInt("id"); 
		new QueryDb().execute();
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	class QueryDb extends AsyncTask<Void, Void, Void> {
		 
		ArrayList<TaskPlus> tasks = new ArrayList<TaskPlus>();
        @Override
        protected Void doInBackground(Void... objects) {
        	datashop = new DataShop(context);
        	datashop.open();        	
        	tasks = datashop.get_all_tasks();
        	
        	datashop.close();
        	return null;
        }
        
        
        @Override
        protected void onPostExecute(final Void unused){
        	
            //update UI with my objects

        	updateUi(tasks);
        	
           
        }
    }
	
	public void updateUi(ArrayList<TaskPlus> tasks) {
		ListView lv = (ListView) findViewById(R.id.task_list);
		listView = lv;
		ArrayAdapter<TaskPlus> adapter = new ArrayAdapter<TaskPlus>(this, android.R.layout.simple_list_item_1, tasks);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			//lv.setOnItemClickListener(new OnItemClickListener() {

		      //  public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3) {
	    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    				
	    	         ArrayAdapter<TaskPlus> adapter1 = (ArrayAdapter<TaskPlus>) listView.getAdapter();
	    	         TaskPlus task = (TaskPlus) parent.getAdapter().getItem(position);
	    	         int itemid = task.id;
	    	         System.out.println("id: " + itemid);
	    	         Intent inten1=new Intent(getBaseContext(), EditActivity.class);
	    	         inten1.putExtra("id", itemid);
	    	         startActivityForResult(inten1, 1);    
	    		}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		    	 System.out.println("back to caller...");
		    	 int id = data.getExtras().getInt("id");
		         long alarmTime = (data.getIntExtra("alarm-hour", 9) * 100) + data.getIntExtra("alarm-minute", 30);
		         long initdate = Calendar.getInstance().getTimeInMillis();
		         Calendar cal_now = Calendar.getInstance();
		         long ms = data.getExtras().getLong("mss");
		         cal_now.setTimeInMillis(ms);
		         Calendar cal_alarm = Calendar.getInstance();
		         cal_alarm.set(Calendar.HOUR_OF_DAY, cal_now.get(Calendar.HOUR_OF_DAY));
		         cal_alarm.set(Calendar.MINUTE, cal_now.get(Calendar.MINUTE));
		         scheduleAlarmReceiver(cal_alarm, id); 
		         new UpdateTask().execute(id, initdate, data.getStringExtra("name"), data.getBooleanArrayExtra("days"), ms);
		          
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		  
		  if (requestCode == 2) {

			     if(resultCode == RESULT_OK){      
			    	 
			          
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result
			     }
			  }
		}//onActivityResult
	
	
class UpdateTask extends AsyncTask<Object, Void, Void> {
		 
		
		TaskPlus task1;
        @Override
        protected Void doInBackground(Object... objects) {
        	datashop = new DataShop(context);
        	datashop.open();        	
        	//tasks = datashop.get_all_tasks();
        	//datashop.close();
        	/*
        	int id = (Integer) objects[0];
        	long[] ids = new long[1];
    		ids[0] = id;
    		datashop.deleteMultipleTasks(ids);
    		datashop.deleteNotify(ids);
    		datashop.deleteTaskDays(ids);
        	
    		String name = (String) objects[2];
        	boolean[] days = (boolean[]) objects[3];
        	long initdate = (Long) objects[1];
        	//long alarmTime = (Long) objects[0];
        	*/
        	int id = (Integer) objects[0];
        	String taskName = (String) objects[2];
        	long alarmTime = (Long) objects[4];
        	boolean[] days = (boolean[]) objects[3];
        	long initdate = (Long) objects[1];
       
        	System.out.println("alarmTime" + alarmTime);
        	for(int i=0; i<days.length; i++) {
        		System.out.println("day[i]" + days[i]);
        	}
        	
        	datashop.update_task(taskName, alarmTime, initdate, days, id);
            //Log.i("async", "name: " + name+" itemlength: " + days.length);
            //task1 = task;
            
            
        	
            return null;
        }
        
        
        @Override
        protected void onPostExecute(final Void unused){
        	
            //update UI with my objects

        	//updateUi(tasks);
        	ListView lv = (ListView) findViewById(R.id.task_list);
            ArrayAdapter<TaskPlus> adapter =  (ArrayAdapter<TaskPlus> )lv.getAdapter();
        	//adapter.add(task1);
        	adapter.notifyDataSetChanged();
        	
        	
           
        }
    }
	
	
	public void scheduleAlarmReceiver(Calendar cal_alarm, int id) {
	      AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	      PendingIntent pendingIntent =  PendingIntent.getBroadcast(this, id, new Intent(this, HabitatorAlarmReceiver.class).putExtra("id", id),
		                        PendingIntent.FLAG_CANCEL_CURRENT);
	      Date dat  = new Date();//initializes to now
	      //Calendar cal_alarm = Calendar.getInstance();
	      Calendar cal_now = Calendar.getInstance();
	      cal_now.setTime(dat);
	      //cal_alarm.setTime(dat);
	      //cal_alarm.setTimeZone(TimeZone.getTimeZone("GMT"));
	      //cal_alarm.set(Calendar.HOUR_OF_DAY,);//set the alarm time
	      //cal_alarm.set(Calendar.MINUTE, Constants.minutes);
	      //cal_alarm.set(Calendar.SECOND, Constants.seconds);
	      if(cal_alarm.before(cal_now)){//if its in the past increment
	          cal_alarm.add(Calendar.DATE,1);
	    	  Log.i("","cal_alarm is in past");
	    	  
	      }
	      alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	      Log.i("alarm", "alarm set@ "+ "now hour|day: "+ cal_now.get(Calendar.HOUR_OF_DAY)+"|"+ cal_now.get(Calendar.DATE) + "alarm hour|min|date: "+ cal_alarm.get(Calendar.HOUR_OF_DAY) +"|"+cal_alarm.get(Calendar.MINUTE) +"|"+cal_alarm.get(Calendar.DATE) );
	       
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
