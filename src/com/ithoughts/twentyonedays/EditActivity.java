package com.ithoughts.twentyonedays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ithoughts.twentyonedays.MultiSpinner.MultiSpinnerListener;
public class EditActivity extends Activity implements MultiSpinnerListener {
	Context context = this;
	DataShop datashop;
	
	String taskname;
	Calendar cal;
	String summary;
	boolean[] selectedItems;
	
	int hour;
	int minute;
	long mss;
	int id;
	
	static final int TIME_DIALOG_ID = 989;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		// Show the Up button in the action bar.
		setupActionBar();
		
		id = getIntent().getExtras().getInt("id");
		new QueryDb().execute(id);
		/*
		List<String> items = new ArrayList<String>();
		
		items.add("Sunday");
		items.add("Monday");
		items.add("Tuesday");
		items.add("Wednesday");
		items.add("Thursday");
		items.add("Friday");
		items.add("Saturday");
		
		
		MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.add_days);
		multiSpinner.setItems(items, "Repeat: All days in a week", this);
		//selectedItems = multiSpinner.getSelected();*/
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	
	class QueryDb extends AsyncTask<Integer, Void, Void> {
		 
		ArrayList<TaskPlus> tasks = new ArrayList<TaskPlus>();
        @Override
        protected Void doInBackground(Integer... objects) {
        	datashop = new DataShop(context);
        	datashop.open();        	
        	int id = objects[0];
        	System.out.println(id);
            taskname = datashop.getTaskNameById(id);
        	long ms = datashop.getTaskAlarmTimeById(id);
        	cal = Calendar.getInstance();
        	cal.setTimeInMillis(ms);
        	String[] days = datashop.getTaskDaysById(id);
        	String summary = days[1] +", " + days[2] +", " + days[3] + "...";
        	datashop.close();
        	return null;
        }
        
        
        @Override
        protected void onPostExecute(final Void unused){
        	
            //update UI with my objects
        	
        	Calendar cal_now = Calendar.getInstance();
        	cal_now.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        	cal_now.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        	hour = cal_now.HOUR_OF_DAY;
        	minute = cal_now.MINUTE;
        	cal = cal_now;
        	updateui();
        	
           
        }
    }
	
	
	public void updateui() {
		
		((EditText) findViewById(R.id.add_task_name)).setText(taskname);
		
		((Button) findViewById(R.id.add_alarm_time)).setText(cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE));
		List<String> items = new ArrayList<String>();
		
		items.add("Sunday");
		items.add("Monday");
		items.add("Tuesday");
		items.add("Wednesday");
		items.add("Thursday");
		items.add("Friday");
		items.add("Saturday");
		
		
		MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.add_days);
		Log.i("fdsadsf","updating ui");
		multiSpinner.setItems(items, "Repeat:", this);
		selectedItems = multiSpinner.getSelected();
		
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
 
		}
		return null;
	}
 
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
				hour = selectedHour;
				minute = selectedMinute;
				Calendar cal_alarm = Calendar.getInstance();
				//Date date = new Date();
				//cal_alarm.setTime(date);
				cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
				cal_alarm.set(Calendar.MINUTE, minute);
				mss = cal_alarm.getTimeInMillis();
			
 
			// set current time into textview
			TextView tv = (TextView) findViewById(R.id.add_alarm_time);
			tv.setText(hour+":"+minute);

		}
	};
	
	public void show_time_picker(View v) {
		showDialog(TIME_DIALOG_ID);
	}

	public void saveThis(View v) {
		Intent data = new Intent();
		data.putExtra("id", id);
		data.putExtra("name", ((TextView)findViewById(R.id.add_task_name)).getText().toString());
		data.putExtra("alarm-hour", hour);
		data.putExtra("alarm-minute", minute);
		data.putExtra("days", selectedItems); 
		System.out.println("mss: " + mss);
		data.putExtra("mss", mss);
		setResult(RESULT_OK, data);
		super.finish();
	}
	
	public void deleteThis(View v){
		datashop.open();
		long[] ids = new long[1];
		ids[0] = id;
		datashop.deleteMultipleTasks(ids);
		datashop.deleteNotify(ids);
		datashop.deleteTaskDays(ids);
		datashop.close();
		super.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
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
			
		case R.id.menu_edit:
			Intent data = new Intent();
			data.putExtra("id", id);
			data.putExtra("name", ((TextView)findViewById(R.id.add_task_name)).getText().toString());
			data.putExtra("alarm-hour", hour);
			data.putExtra("alarm-minute", minute);
			data.putExtra("days", selectedItems);
			data.putExtra("mss", mss);
			setResult(RESULT_OK, data);
			super.finish();
		
		case R.id.menu_delete:
			datashop.open();
			long[] ids = new long[1];
			ids[0] = id;
			datashop.deleteMultipleTasks(ids);
			datashop.deleteNotify(ids);
			datashop.deleteTaskDays(ids);
			datashop.close();
			super.finish();
			
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemsSelected(boolean[] selected) {
		// TODO Auto-generated method stub
		
	}

}
