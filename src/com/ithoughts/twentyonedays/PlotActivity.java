package com.ithoughts.twentyonedays;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class PlotActivity extends Activity {
	DataShop datashop;
	Context context;
	int id;
	String taskName, value;
	GraphObject graph;
	SocialAuthAdapter adapter;
	int providerid;
	private static final String TAG = "DialogActivity";
    private static final int DLG_EXAMPLE1 = 0;
    private static final int TEXT_ID = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plot);
		context = this;
		id = (int) getIntent().getExtras().getLong("id");
		new QueryDb().execute(id);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Button sharebtn = (Button) findViewById(R.id.twitter);
     	//adapter = new SocialAuthAdapter(new ResponseListener());
     	
     	//adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
        //adapter.addProvider(Provider.TWITTER, R.drawable.twitter);

        //adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
        
        //adapter.enable(sharebtn);
        
		
	}
	
	public void tweet(View v) {
		adapter = new SocialAuthAdapter(new ResponseListener());
     	
     	//adapter.addProvider(Provider.FACEBOOK, R.drawable.twitter);
        adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
        //adapter.addProvider(Provider.GOOGLEPLUS, R.drawable.twitter);
        //adapter.addProvider(Provider.LINKEDIN, R.drawable.twitter);

        adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
        providerid = 1;
        
        //adapter.enable((Button)v);
        
        //adapter.authorize(this, Provider.TWITTER);
        showDialog(999); 
	}
	
	public void post(View v) {
		adapter = new SocialAuthAdapter(new ResponseListener());
     	
     	//adapter.addProvider(Provider.FACEBOOK, R.drawable.twitter);
        adapter.addProvider(Provider.FACEBOOK, R.drawable.twitter);
        providerid = 2;
        //adapter.addProvider(Provider.GOOGLEPLUS, R.drawable.twitter);
        //adapter.addProvider(Provider.LINKEDIN, R.drawable.twitter);

        //adapter.addCallBack(Provider.FACEBOOK, "http://localhost");
        /*
        String perm = Permission.DEFAULT.toString();
        try {
			adapter.addConfig(Provider.FACEBOOK, "393713114073741", "322bc797ffa4ff8ced4767f2e6870e90", perm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */

        //adapter.enable((Button)v);
        showDialog(999);
        
        
	}
	
	
	
	private final class ResponseListener implements DialogListener 
	{
	   public void onComplete(Bundle values) {
	     
	   //edit = (EditText) findViewById(R.id.editTxt);    
	   //adapter.updateStatus("Testing, oauth integration for my wonderful app!", new MessageListener(),false);
		   adapter.updateStatus(value, new MessageListener(),false);                      
	   }



	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		Log.d("ShareButton" , "Cancelled");
	}

	@Override
	public void onError(SocialAuthError arg0) {
		// TODO Auto-generated method stub
		 Log.d("ShareButton" , "Error");
	}



	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {

	   public void onExecute(Integer t) {
	   Integer status = t;
	   if (status.intValue() == 200 || status.intValue() == 201 ||status.intValue() == 204)
	   Toast.makeText(PlotActivity.this, "posted",Toast.LENGTH_LONG).show();
	   else
	   Toast.makeText(PlotActivity.this, "not posted",Toast.LENGTH_LONG).show();
	  }

	  public void onError(SocialAuthError e) {

	  }

	@Override
	public void onExecute(String arg0, Integer t) {
		// TODO Auto-generated method stub
		Integer status = t;
		   if (status.intValue() == 200 || status.intValue() == 201 ||status.intValue() == 204)
		   Toast.makeText(PlotActivity.this, "posted",Toast.LENGTH_LONG).show();
		   else
		   Toast.makeText(PlotActivity.this, "not posted",Toast.LENGTH_LONG).show();
		  }
		
	}
	
	/**
     * Called to create a dialog to be shown.
     */
    @Override
    protected Dialog onCreateDialog(int id) {
 
        switch (id) {
            case 999:
            	
                return createExampleDialog();
            default:
                return null;
        }
    }
 
    /**
     * If a dialog has already been created,
     * this is called to reset the dialog
     * before showing it a 2nd time. Optional.
     */
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
 
        switch (id) {
            case 999:
                // Clear the input box.
                //EditText text = (EditText) dialog.findViewById(TEXT_ID);
                //text.setText("");
                break;
        }
    }
 
    /**
     * Create and return an example alert dialog with an edit text box.
     */
    private Dialog createExampleDialog() {
 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Boast");
        
 
         // Use an EditText view to get user input.
         final EditText input = new EditText(this);
         input.setId(TEXT_ID);
         String text = "I'm tracking my activity, " + taskName + ", so far with " + graph.hits + " hits and " + graph.misses + " misses! \n #TwentyOneDaysApp";
         input.setText(text);
         builder.setView(input);
 
        builder.setPositiveButton("Boast", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                value = input.getText().toString();
                if(providerid == 2) {
                	adapter.authorize(context, Provider.FACEBOOK);
                } else {
                	adapter.authorize(context, Provider.TWITTER);
                }
               
                Log.d(TAG, "Boast: " + value);
                return;
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
 
        return builder.create();
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
		TextView days = (TextView) findViewById(R.id.genesis);
		TextView hit = (TextView) findViewById(R.id.hits);
		TextView miss = (TextView) findViewById(R.id.misses);
		String statistics =  graph.days_from_genesis + ", days since start";
		String hits = graph.hits + ", hits";
		String misses;
		if(graph.misses == 0) {
			misses = " no misses ";
		} else if(graph.misses == 1) {
			misses = " 1, miss ";
		} else {
			misses = graph.misses + ", misses";
		}

		days.setText(statistics);
		hit.setText(hits);
		miss.setText(misses);
	}
	
	public void edit_tracker(View v) {
		Intent intent = new Intent(this, UpdateTracker.class);
		intent.putExtra("id", id);
		System.out.println(id);
		startActivity(intent);
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
