package com.example.android.nbaschedule;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleFragment extends Fragment {
    public final int TIMEZONE_CHANGE = 5;
    public ScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ScheduleAdapter dba = new ScheduleAdapter(getActivity());
        dba.open();
        Cursor c = dba.getFutureRows();

        List<String> realSchedule = new ArrayList<String>();
        while (c.moveToNext()){
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyyMMddHHmm");
            SimpleDateFormat sdfOutput = new SimpleDateFormat("MM dd HH:mm");
            Date gameDateET;
            try {
                gameDateET = sdfInput.parse(c.getString(c.getColumnIndex("datetime")));
            } catch (ParseException e) {
                gameDateET = new Date();
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(gameDateET);
            cal.add(Calendar.HOUR_OF_DAY, TIMEZONE_CHANGE);
            String formattedTime = sdfOutput.format(cal.getTime());
            String awayTeam = c.getString(c.getColumnIndex("away"));
            String homeTeam = c.getString(c.getColumnIndex("home"));
            String matchUp = awayTeam + " @ " + homeTeam;


            realSchedule.add(formattedTime + " " + matchUp);
        }

        ArrayAdapter<String> forecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_schedule, // The name of the layout ID.
                        R.id.list_item_schedule_textview, // The ID of the textview to populate.
                        realSchedule);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_schedule);
        listView.setAdapter(forecastAdapter);
        return rootView;
    }


    // dba.open();
    // Cursor c = dba.ExampleSelect("Rawr!");
    // Cursor c = dba.ExampleSelect("Rawr!");

}