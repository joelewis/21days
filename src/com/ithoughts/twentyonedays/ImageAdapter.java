package com.ithoughts.twentyonedays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
 
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    
	
	private int[] sta= new int[500];
   
   
  
    // Keep all Images in array
   /* public Integer[] mThumbIds = {
            R.drawable.green, R.drawable.green,
            R.drawable.green, R.drawable.green,
            R.drawable.green, R.drawable.green,
            R.drawable.green, R.drawable.green,
            R.drawable.green, R.drawable.green,
            R.drawable.green, R.drawable.green,
            R.drawable.green, R.drawable.green,
            R.drawable.green
    };
 */
    // Constructor
    public ImageAdapter(Context context, int[] content){
    	//super(context, R.layout.two_line_week, R.id.textt1, content); 	
    	sta = content;
    	mContext=context;
    	//mContext = c;
    }
 
    @Override
    public int getCount() {
    	return sta.length;
        //return mThumbIds.length;
    }
 
    @Override
    public Object getItem(int position) {
        return sta[position];
    	//return mThumbIds[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
  
    	ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(sta[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(20, 20));
     
        
        return imageView;
    }
 
}