package com.mirrorlabs.vince.amidst;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by vince on 11/25/15.
 *
 * NEED TO ADJUST TIME POSTING BASED ON HOW RECENT
 * IF POSTED SAME DAY AS BEING READ PUT HOW MANY HOURS AGO
 * IF POSTED WITHIN THE HOUR PUT "WITHIN THE HOUR"
 */

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ClipboardItem> clipboardItems;

    public CustomListAdapter(Activity activity, List<ClipboardItem> clipboardItems){

        this.activity = activity;
        this.clipboardItems = clipboardItems;
    }

    @Override
    public int getCount(){
        return clipboardItems.size();
    }

    @Override
    public Object getItem(int location){
        return clipboardItems.get(location);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public View getView (int position, View convertView , ViewGroup parent){

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView timestamp = (TextView)convertView.findViewById(R.id.timestamp);
       // TextView starred = (TextView)convertView.findViewById(R.id.starred);
        TextView clipString = (TextView)convertView.findViewById(R.id.clip);

        ClipboardItem row = clipboardItems.get(position);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd");
        Date datePosted = new Date(row.getTimestamp());


        //Date todaysDate = new Date(System.currentTimeMillis());

        //if (simpleDateFormat.format(datePosted).equals(simpleDateFormat.format(todaysDate))){
        //    SimpleDateFormat hourDifference = new SimpleDateFormat("hh:mm");



        clipString.setText(row.getTitle());
       // starred.setText(row.getIsStarred());
        timestamp.setText(simpleDateFormat.format(datePosted));

        return convertView;

    }



}
