package com.ithoughts.twentyonedays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LaunchActivity extends Activity {

	DataShop datashop;
	Context context = this;
	ListView lvv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);	
		new QueryDb().execute();
	}
	
	
	
	class QueryDb extends AsyncTask<Integer, Void, Void> {
		 
		ArrayList<TaskPlus> tasks = new ArrayList<TaskPlus>();
        @Override
        protected Void doInBackground(Integer... objects) {
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
	
	
	class AddTask extends AsyncTask<Object, Void, Void> {
		 
		
		TaskPlus task1;
        @Override
        protected Void doInBackground(Object... objects) {
        	datashop = new DataShop(context);
        	datashop.open();        	
        	//tasks = datashop.get_all_tasks();
        	//datashop.close();
        	String name = (String) objects[2];
        	boolean[] days = (boolean[]) objects[3];
        	long initdate = (Long) objects[1];
        	long alarmTime = (Long) objects[0];
        	
        	TaskPlus task = datashop.add_another_task((String) objects[2],(Long) objects[4], (Long) objects[1], (boolean[]) objects[3]);
            Log.i("async", "name: " + name+" itemlength: " + days.length);
            task1 = task;
            
            
        	
            return null;
        }
        
        
        @Override
        protected void onPostExecute(final Void unused){
        	
            //update UI with my objects

        	//updateUi(tasks);
        	ListView lv = (ListView) findViewById(R.id.task_list);
            ArrayAdapter<TaskPlus> adapter =  (ArrayAdapter<TaskPlus> )lv.getAdapter();
        	adapter.add(task1);
        	adapter.notifyDataSetChanged();
        	
        	
           
        }
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		    	 System.out.println("back to caller...");
		         long alarmTime = (data.getIntExtra("alarm-hour", 9) * 100) + data.getIntExtra("alarm-minute", 30);
		         long initdate = Calendar.getInstance().getTimeInMillis();
		         Calendar cal_alarm = Calendar.getInstance();
		         long ms = data.getExtras().getLong("ms");
		         cal_alarm.setTimeInMillis(ms);
		         scheduleAlarmReceiver(cal_alarm); 
		         new AddTask().execute(alarmTime, initdate, data.getStringExtra("name"), data.getBooleanArrayExtra("days"), ms);
		          
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		}//onActivityResult
	
	public void updateUi(List<TaskPlus> tasks) {
		ListView lv = (ListView) findViewById(R.id.task_list);
		MyArrayAdapter adapter = new MyArrayAdapter (context, tasks);
		lv.setAdapter(adapter);
		
	   final ListView listView;
	   listView = lv;
	   lvv = lv;
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			//lv.setOnItemClickListener(new OnItemClickListener() {

		      //  public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3) {
	    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    				
	    	         ArrayAdapter<TaskPlus> adapter1 = (ArrayAdapter<TaskPlus>) listView.getAdapter();
	    	         TaskPlus task = (TaskPlus) parent.getAdapter().getItem(position);
	    	         long itemid = task.id;
	    	         System.out.println("id: " + itemid);
	    	         Intent inten1=new Intent(getBaseContext(), PlotActivity.class);
	    	         inten1.putExtra("id", itemid);
	    	         startActivityForResult(inten1, 1);    
	    		}
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			//lv.setOnItemClickListener(new OnItemClickListener() {

		      //  public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3) {
	    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    				
	    	         ArrayAdapter<TaskPlus> adapter1 = (ArrayAdapter<TaskPlus>) listView.getAdapter();
	    	         TaskPlus task = (TaskPlus) parent.getAdapter().getItem(position);
	    	         long itemid = task.id;
	    	         System.out.println("id: " + itemid);
	    	         Intent inten1=new Intent(getBaseContext(), PlotActivity.class);
	    	         inten1.putExtra("id", itemid);
	    	         startActivity(inten1);    
	    		}

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				ArrayAdapter<TaskPlus> adapter1 = (ArrayAdapter<TaskPlus>) listView.getAdapter();
   	         	TaskPlus task = (TaskPlus) parent.getAdapter().getItem(position);
   	         	long itemid = task.id;
   	         	
				
   	         	
   	         	return false;
			}
		});
		
		registerForContextMenu(listView);
		
		//I
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.floating, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.edit_option:
	        	System.out.println("id:" + info.getClass());
	        	int id = ((ArrayAdapter<TaskPlus>) lvv.getAdapter()).getItem(info.position).id;
	        	System.out.println("id: " + id);
	        	Intent intent = new Intent(this, EditActivity.class);
	        	intent.putExtra("id", id);
	        	startActivity(intent);
	            //editNote(info.id);
	            return true;
	        case R.id.delete_option:
	            //deleteNote(info.id);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	public void scheduleAlarmReceiver(Calendar cal_alarm) {
	      AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	      PendingIntent pendingIntent =  PendingIntent.getBroadcast(this, 0, new Intent(this, HabitatorAlarmReceiver.class),
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
	
	public void onResume() {
		new QueryDb().execute();
		super.onResume();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.add_task:
	            Intent intent = new Intent(this, AddActivity.class);
	            intent.putExtra("intention", 1);
	            //intent.putExtra("intention", 1);
	            startActivityForResult(intent, 1);
	            return true;
	       
	        //case R.id.action_settings:
	        	//Intent settings = new Intent(this, SettingsActivity.class);
	        	//startActivity(settings);
	        	//return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
