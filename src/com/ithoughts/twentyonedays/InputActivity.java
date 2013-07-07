package com.ithoughts.twentyonedays;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InputActivity extends Activity {
	int id;
	DataShop datashop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		Intent intent = getIntent();
		id = intent.getExtras().getInt("id");
		String name = intent.getExtras().getString("name");
		System.out.println("id : " + id);
		System.out.println("name : " + name);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(name);
		
		TextView tv = (TextView) findViewById(R.id.task_name);
		tv.setText(name);
		
	
	}
	

	@Override
	public void onNewIntent(Intent intent){
		Bundle extras = intent.getExtras();
		if(extras != null){
			if(extras.containsKey("id"))
			{
				//setContentView(R.layout.viewmain);
				// extract the extra-data in the Notification
				//String msg = extras.getString("NotificationMessage");
				//txtView = (TextView) findViewById(R.id.txtMessage);
				//txtView.setText(msg);
				id = intent.getExtras().getInt("id");
				String name = intent.getExtras().getString("name");
				System.out.println("id : " + id);
				System.out.println("name : " + name);
				ActionBar actionBar = getActionBar();
				actionBar.setTitle(name);
				
				TextView tv = (TextView) findViewById(R.id.task_name);
				tv.setText(name);
			}
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input, menu);
		return true;
	}
	
	
	public void yes_button_clicked( View v) {
		
		((Button)v).setText("Saving..");
		datashop = new DataShop(this);
		datashop.open();
		datashop.update_ifnot_insert_stat(id, 1);
		datashop.close();
		super.finish();
	
	}
	
    public void no_button_clicked( View v) {
		
		((Button)v).setText("Saving..");
		datashop = new DataShop(this);
		datashop.open();
		datashop.update_ifnot_insert_stat(id, 0);
		datashop.close();
		super.finish();
	
	}
	

}
