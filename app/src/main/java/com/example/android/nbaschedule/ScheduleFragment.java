package com.example.android.nbaschedule;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class ScheduleFragment extends Fragment {
    private ScheduleCursorAdapter mScheduleCursorAdapter;
    String LOG_TAG = ScheduleFragment.class.getSimpleName();
    private static int currentPosition;

    public ScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final AdapterView listView = (AdapterView) rootView.findViewById(R.id.listview_schedule);
        //listView.setAdapter(mForecastAdapter);
        listView.setAdapter(mScheduleCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                currentPosition = adapterView.getFirstVisiblePosition();
                Log.v(LOG_TAG, "currentPosition " + currentPosition);
                Cursor c = mScheduleCursorAdapter.getCursor();
                c.moveToPosition(position);
                String home = c.getString(c.getColumnIndex("home"));
                String away = c.getString(c.getColumnIndex("away"));
                String datetime = c.getString(c.getColumnIndex("datetime"));
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("home", home);
                intent.putExtra("away", away);
                intent.putExtra("datetime", datetime);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        ScheduleAdapter schedule_DB = new ScheduleAdapter(getActivity());
        schedule_DB.open();
        Cursor c = schedule_DB.getFutureRows();
        mScheduleCursorAdapter = new ScheduleCursorAdapter(getActivity(), c, 0);

    }

    @Override
    public void onDestroy() {
        Log.v(LOG_TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        AdapterView listView = (AdapterView) getView().findViewById(R.id.listview_schedule);
        Log.v(LOG_TAG, "currentpos onResume " + currentPosition);
        listView.setSelection(currentPosition);
        super.onResume();
    }
}