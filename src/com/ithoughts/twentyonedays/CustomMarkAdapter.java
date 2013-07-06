package com.ithoughts.twentyonedays;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CustomMarkAdapter extends ArrayAdapter {

	
    private List<TaskPlus> tasks;
   
    private static Context context;
    //Resources res;
    String[] days = new String[7];
    String[] dates = new String[7];
    int[] stats = new int[7];
    int[] valids = new int[7];
    int[] prestats = new int[7];
    

public CustomMarkAdapter(Context contextt, String[] dayss, String[] datess, int[] validss, int[] prestatss) {
    super(contextt, R.layout.simple_list_item_2, R.id.text11, dayss);
    context = contextt;
    days = dayss;
    dates = datess;
    valids = validss;
    prestats = prestatss;
	//res = context.getResources();
}



//calculate todays date
//fetch the flag for todays day of the year and greater
//write its color to 7th dot
//make a loop for six
//for each loop decrement one day and calculate milliseconds 
//get flag for that day i.e greater than that date and lesser than last calculated milliseconds value
//after the loop get six flags and set them inorder
//dats it!

public int[] getValids() {
	
	return stats;
	
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
    View v = super.getView(position, convertView, parent);
    LinearLayout linearLayout = (LinearLayout) v;
   
   TextView day = (TextView) linearLayout.findViewById(R.id.text11);
   day.setText(days[position]);
   TextView date = (TextView) linearLayout.findViewById(R.id.text22);
   date.setText(dates[position]);
   ToggleButton tb = (ToggleButton) linearLayout.findViewById(R.id.radio);
   if(valids[position]==1) {
	   
	   tb.setEnabled(true);
	   if(prestats[position] == 1) {
		   
		   tb.setChecked(true);
	   } else {
		   tb.setChecked(false);
	   }
   }
   else {
	   tb.setEnabled(false);
   }
   final ToggleButton tb1 = tb;
   final int i = position;
   
   if(tb.isEnabled() == false) {
	   stats[i] = 2;
   } else if(tb1.getText().toString().equalsIgnoreCase("Yes")) {
	   stats[i] = 1;
   } else {
	   stats[i] = 0;
   }
   
   tb.setOnClickListener(new OnClickListener() {

       public void onClick(View v) {
    	   
    	   if(tb1.getText().toString().equalsIgnoreCase("Yes")) {
    		   stats[i] = 1;
    	   }
    	   else {
    		   stats[i] = 0;
    	   }
           // TODO Auto-generated method stub
           //Toast.makeText(context, tb1.getText().toString(), Toast.LENGTH_LONG).show();
       }
   });

    return linearLayout;
}
}