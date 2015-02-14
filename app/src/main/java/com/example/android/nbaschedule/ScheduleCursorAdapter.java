package com.example.android.nbaschedule;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by justas on 07/02/15.
 */
public class ScheduleCursorAdapter extends CursorAdapter {
    public ScheduleCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_schedule, parent, false);

    }


    SimpleDateFormat sdfInput = new SimpleDateFormat("yyyyMMddHHmm");
    SimpleDateFormat sdfOutput = new SimpleDateFormat("MM dd HH:mm");



    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Date gameDateET;

        try {
            gameDateET = sdfInput.parse(cursor.getString(cursor.getColumnIndex("datetime")));
        } catch (ParseException e) {
            gameDateET = new Date();
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();

        cal.setTime(gameDateET);
        Log.v("ET time", sdfOutput.format(gameDateET));
        cal.add(Calendar.HOUR_OF_DAY, Utility.getTimezoneChange());
        String formattedDateTime = sdfOutput.format(cal.getTime());
        String formattedTime = formattedDateTime.substring(6);

        //if (formattedTime.equals("00:00")){
        //    formattedTime = "23:59";
        //    cal.add(Calendar.DATE, -1);
        //    formattedDateTime = sdfOutput.format(cal.getTime()); //shift back one day
        //}

        String formattedDate = formattedDateTime.substring(0, 5);
        String friendlyDate = Utility.formFriendlyDate(formattedDate);

        TextView dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        dateView.setText(friendlyDate);

        TextView timeView = (TextView) view.findViewById(R.id.list_item_time_textview);
        timeView.setText(formattedTime);

        String homeTeam = cursor.getString(cursor.getColumnIndex("home"));
        TextView homeView = (TextView) view.findViewById(R.id.list_item_home_textview);
        homeView.setText(homeTeam);

        String awayTeam = cursor.getString(cursor.getColumnIndex("away"));
        TextView awayView = (TextView) view.findViewById(R.id.list_item_away_textview);
        awayView.setText(awayTeam);

        this.notifyDataSetChanged();
    }


}
