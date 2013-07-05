package com.ithoughts.twentyonedays;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ithoughts.twentyonedays.MultiSpinner.MultiSpinnerListener;

public class AddActivity extends Activity implements MultiSpinnerListener {

	boolean[] selectedItems;
	String name;
	long alarmTime;
    static final int TIME_DIALOG_ID = 999;
	private int hour = 9;
	private int minute = 30;
	long ms;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		// Show the Up button in the action bar.
		setupActionBar();
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
		selectedItems = multiSpinner.getSelected();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute, false);
 
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
			Date date = new Date();
			cal_alarm.setTime(date);
			cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
			cal_alarm.set(Calendar.MINUTE, minute);
			ms = cal_alarm.getTimeInMillis();
			
 
			// set current time into textview
			TextView tv = (TextView) findViewById(R.id.add_alarm_time);
			tv.setText(hour+":"+minute);

		}
	};
 

	public void show_days_function(View v) {
		
	}
	
	public void show_time_picker(View v) {
		showDialog(TIME_DIALOG_ID);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
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
			
		case R.id.add_task_done:
			Intent data = new Intent();
			data.putExtra("name", ((TextView)findViewById(R.id.add_task_name)).getText().toString());
			data.putExtra("alarm-hour", hour);
			data.putExtra("alarm-minute", minute);
			data.putExtra("days", selectedItems);
			data.putExtra("ms", ms);
			setResult(RESULT_OK, data);
			super.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemsSelected(boolean[] selected) {
		// TODO Auto-generated method stub
		System.out.println("selected" + selected.length);
		selectedItems = selected;
	}

}
