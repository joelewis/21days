package com.ithoughts.twentyonedays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
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
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Your Activities");
		actionBar.setSubtitle("Touch + to add a new activity");
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
        	long alarmTime = (Long) objects[4];
        	
        	TaskPlus task = datashop.add_another_task((String) objects[2],(Long) objects[4], (Long) objects[1], (boolean[]) objects[3]);
            Log.i("async", "name: " + name+" itemlength: " + days.length);
            task1 = task;
            
             Calendar cal_alarm = Calendar.getInstance();
	         cal_alarm.setTimeInMillis(alarmTime);
	         Calendar cal_now = Calendar.getInstance();
	         cal_now.set(Calendar.HOUR_OF_DAY, cal_alarm.get(Calendar.HOUR_OF_DAY));
	         cal_now.set(Calendar.MINUTE, cal_alarm.get(Calendar.MINUTE));
            
            scheduleAlarmReceiver(cal_now, task.id); 
            
        	
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
	
		
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		    	 System.out.println("back to caller...");
		         long alarmTime = (data.getIntExtra("alarm-hour", 9) * 100) + data.getIntExtra("alarm-minute", 30);
		         long initdate = Calendar.getInstance().getTimeInMillis();
		         Calendar cal_now = Calendar.getInstance();
		         long ms = data.getExtras().getLong("ms");
		         
		         new AddTask().execute(alarmTime, initdate, data.getStringExtra("name"), data.getBooleanArrayExtra("days"), ms);
		          
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		  
		  if (requestCode == 2) {

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
	    	         //Intent inten1=new Intent(getBaseContext(), PlotActivity.class);
	    	         //inten1.putExtra("id", itemid);
	    	         //startActivity(inten1);    
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
		
		//registerForContextMenu(listView);
		
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

		    @Override
		    public void onItemCheckedStateChanged(ActionMode mode, int position,
		                                          long id, boolean checked) {
		        // Here you can do something when items are selected/de-selected,
		        // such as update the title in the CAB
		    }

		    @Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        // Respond to clicks on the actions in the CAB
		        switch (item.getItemId()) {
		            case R.id.menu_delete:
		                //deleteSelectedItems();
		                mode.finish(); // Action picked, so close the CAB
		                return true;
		            default:
		                return false;
		        }
		    }

		    @Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        // Inflate the menu for the CAB
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.floating, menu);
		        return true;
		    }

		    @Override
		    public void onDestroyActionMode(ActionMode mode) {
		        // Here you can make any necessary updates to the activity when
		        // the CAB is removed. By default, selected items are deselected/unchecked.
		    }

		    @Override
		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        // Here you can perform updates to the CAB due to
		        // an invalidate() request
		        return false;
		    }
		});

		
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
	        	startActivityForResult(intent,2);
	            //editNote(info.id);
	            return true;
	        case R.id.delete_option:
	            //deleteNote(info.id);
	        	datashop.open();
	        	int idd = ((ArrayAdapter<TaskPlus>) lvv.getAdapter()).getItem(info.position).id;
	        	((ArrayAdapter<TaskPlus>) lvv.getAdapter()).remove(((ArrayAdapter<TaskPlus>) lvv.getAdapter()).getItem(info.position));
				long[] ids = new long[1];
				ids[0] = idd;
				datashop.deleteMultipleTasks(ids);
				datashop.deleteNotify(ids);
				datashop.deleteTaskDays(ids);
				datashop.close();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
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
	            
	        case R.id.edit_task:
	        	Intent edit = new Intent(this, EditListActivity.class);
	            edit.putExtra("intention", 1);
	            //intent.putExtra("intention", 1);
	            startActivity(edit);
	            return true;
	        /*
	        case R.id.delete_task:
	        	Intent delete = new Intent(this, EditListActivity.class);
	            delete.putExtra("intention", 1);
	            //intent.putExtra("intention", 1);
	            startActivity(delete);
	            return true;*/
	        	
	        	
	        	
	       
	        //case R.id.action_settings:
	        	//Intent settings = new Intent(this, SettingsActivity.class);
	        	//startActivity(settings);
	        	//return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
