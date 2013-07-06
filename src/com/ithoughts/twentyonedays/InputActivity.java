package com.ithoughts.twentyonedays;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class InputActivity extends Activity {
	int id;
	DataShop datashop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		int id = getIntent().getExtras().getInt("id");
		String name = getIntent().getExtras().getString("name");
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(name);
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
