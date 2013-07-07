package com.ithoughts.twentyonedays;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyArrayAdapter extends ArrayAdapter {

	
    private List<TaskPlus> tasks;
   
    private Context context;
    Resources res;
    //ImageView red;
    //ImageView green;
    //Bitmap img;
	//private int imagesNR =7;
    //ImageView[] redi = new ImageView[imagesNR ];
    //ImageView[] greeni = new ImageView[imagesNR ];
    //LinearLayout lay;


public MyArrayAdapter(Context context, List<TaskPlus> content) {
    super(context, R.layout.two_line_week, R.id.text1, content);
    tasks = content;
    
	res = context.getResources();
}



//calculate todays date
//fetch the flag for todays day of the year and greater
//write its color to 7th dot
//make a loop for six
//for each loop decrement one day and calculate milliseconds 
//get flag for that day i.e greater than that date and lesser than last calculated milliseconds value
//after the loop get six flags and set them inorder
//dats it!

@Override
public View getView(int position, View convertView, ViewGroup parent) {
    View v = super.getView(position, convertView, parent);
   

    LinearLayout linearLayout = (LinearLayout) v;
   
    // ImageView imgView = (ImageView)findViewById(R.id.image3);

   /* Date dat  = new Date();//initializes to now
    Calendar cal_now = Calendar.getInstance();
    cal_now.setTime(dat);
    Calendar cal_today = Calendar.getInstance();
    cal_today.set(Calendar.DATE, cal_now.get(Calendar.DATE));
    cal_today.set(Calendar.HOUR_OF_DAY, 0);
    cal_today.set(Calendar.MINUTE, 0);
    cal_today.set(Calendar.SECOND, 0);
    Log.i("cal", "today ms: " + cal_today.getTimeInMillis() +"| now ms: " + cal_now.getTimeInMillis());
    Log.i("","view|convertView|position "+v + "|" +convertView +"|"+position+"|"+parent);
    */
   
    ImageView im1 = (ImageView) linearLayout.findViewById(R.id.day1); 
    im1.setImageDrawable(res.getDrawable(tasks.get(position).dotColors[6]));
    ImageView im2 = (ImageView) linearLayout.findViewById(R.id.day2); 
    im2.setImageDrawable(res.getDrawable(tasks.get(position).dotColors[5]));
    ImageView im3 = (ImageView) linearLayout.findViewById(R.id.day3); 
    im3.setImageDrawable(res.getDrawable(tasks.get(position).dotColors[4]));
    ImageView im4 = (ImageView) linearLayout.findViewById(R.id.day4); 
    im4.setImageDrawable(res.getDrawable(tasks.get(position).dotColors[3]));
    ImageView im5 = (ImageView) linearLayout.findViewById(R.id.day5); 
    im5.setImageDrawable(res.getDrawable(tasks.get(position).dotColors[2]));
    ImageView im6 = (ImageView) linearLayout.findViewById(R.id.day6); 
    im6.setImageDrawable(res.getDrawable(tasks.get(position).dotColors[1]));
    ImageView im7 = (ImageView) linearLayout.findViewById(R.id.day7); 
    im7.setImageDrawable(res.getDrawable(tasks.get(position).dotColors[0]));

    //linearLayout.setBackgroundColor(colors[colorPos]);
    return linearLayout;
}
}