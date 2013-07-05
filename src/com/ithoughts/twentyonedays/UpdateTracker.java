package com.ithoughts.twentyonedays;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class UpdateTracker extends Activity {

	Calendar today = Calendar.getInstance();
	Calendar todayagain = Calendar.getInstance();
	String[] day = new String[7];
	String[] dateString = new String[7];
	int[] valids = new int[7];
	Context context;
	DataShop datashop;
	ListView listview;
	int id;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_tracker);
		// Show the Up button in the action bar.
		setupActionBar();
		context = this;
		id = getIntent().getExtras().getInt("id");
		System.out.println("id: " + id);
		
		datashop = new DataShop(this);
		datashop.open();
		for(int i=0; i<7; ++i) {						
			Date date = today.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM d, ''yy", Locale.US);
			String asWeek = dateFormat.format(date);
			String fullDate = dateFormat2.format(date);
			valids[i] = datashop.getDayStatus(id, today.get(Calendar.DAY_OF_WEEK));
			day[i] = asWeek;
			dateString[i] = fullDate;
			today.roll(Calendar.DAY_OF_YEAR, false);
		}
		
		

		//valids = datashop.getValidDays(id);
		datashop.close();
		
		
		
		
		
		CustomMarkAdapter adapter = new CustomMarkAdapter(this, day, dateString, valids);
		ListView lv = (ListView) findViewById(R.id.mark_list);
		lv.setAdapter(adapter);
		listview = lv;
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	class UpdateDb extends AsyncTask<Object, Void, Void> {
		 
		ArrayList<TaskPlus> tasks = new ArrayList<TaskPlus>();
        @Override
        protected Void doInBackground(Object... objects) {
        	
        	DataShop datashop = new DataShop(getApplicationContext());
        	datashop.open();
        	int[] valids = (int[]) objects[0];
        	/*
        	for(int i=(valids.length-1); i>=0; i--) {
        		
        		today.add(Calendar.DAY_OF_YEAR, 1);
        		if(valids[i] == 2) {
        			
        		} else {
        			datashop.update_ifnot_insert_stat(id, valids[i], today.get(Calendar.DAY_OF_YEAR), today);
        		}
        	}
			*/
        	
        	for(int i=0; i<valids.length; i++) {
        		System.out.println("i: " + valids[i]);
        		if(valids[i] == 2) {
        			
        		} else {
        			datashop.update_ifnot_insert_stat(id, valids[i], todayagain.get(Calendar.DAY_OF_YEAR), todayagain);
        			
        		}
        		todayagain.roll(Calendar.DAY_OF_YEAR, false);
        	}
        	return null;
        }
        
        
        @Override
        protected void onPostExecute(final Void unused){
        	
            //update UI with my objects
        	updateui();
        	
        	
           
        }
    }

	public void updateui() {
		super.finish();				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_tracker, menu);
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
			
		case R.id.upda_tracker:
			
			CustomMarkAdapter adapter = (CustomMarkAdapter) listview.getAdapter();
			int[] valids = adapter.getValids();
			new UpdateDb().execute(valids);
			
		}
		return super.onOptionsItemSelected(item);
	}

}
