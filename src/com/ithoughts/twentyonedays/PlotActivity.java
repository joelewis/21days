package com.ithoughts.twentyonedays;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

public class PlotActivity extends Activity {
	DataShop datashop;
	Context context;
	int id;
	String taskName;
	GraphObject graph;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plot);
		context = this;
		id = (int) getIntent().getExtras().getLong("id");
		new QueryDb().execute(id);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	class QueryDb extends AsyncTask<Integer, Void, Void> {
		 
		int dotColors[] = new int[500];
        @Override
        protected Void doInBackground(Integer... objects) {
        	datashop = new DataShop(context);
        	datashop.open();        	
        	int id = objects[0];
        	graph = datashop.get_task_status_for_graph(id);
        	taskName = datashop.getTaskNameById(id);
        	datashop.close();
        	return null;
        }
        
        
        @Override
        protected void onPostExecute(final Void unused){
        	
            //update UI with my objects
        	
        	updateui(dotColors);
        	
        	
           
        }
    }
	
	public void updateui(int[] dotColors) {
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(taskName);
		GridView gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setAdapter(new ImageAdapter(this, graph.dotColors));
		TextView tv = (TextView) findViewById(R.id.hit_miss);
		String statistics =  graph.days_from_genesis + " days, since start with " + graph.hits + " hits and ";
		String misses;
		if(graph.misses == 0) {
			misses = " no misses ";
		} else if(graph.misses == 1) {
			misses = " one miss ";
		} else {
			misses = graph.misses + " misses";
		}
		statistics = statistics + misses;
		tv.setText(statistics);
		
	}

	
	public void onResume() {
		new QueryDb().execute(id);
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plot, menu);
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
			
		case R.id.edit_tracker:
			Intent intent = new Intent(this, UpdateTracker.class);
			intent.putExtra("id", id);
			System.out.println(id);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
