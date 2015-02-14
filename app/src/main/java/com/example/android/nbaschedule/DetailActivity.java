package com.example.android.nbaschedule;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {
        String LOG_TAG = DetailFragment.class.getSimpleName();
        String tickets_URL = "http://www.justasbr.me/tickets_";

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ScheduleAdapter schedule_DB = new ScheduleAdapter(getActivity());
            //schedule_DB.open();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // The detail Activity called via intent.  Inspect the intent for forecast data.
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("home") && intent.hasExtra("away")
                    && intent.hasExtra("datetime")) {

                final int position = intent.getExtras().getInt("position");

                String home = intent.getExtras().getString("home");
                String homeImageName = schedule_DB.getTeamPictureURL(home);
                String away = intent.getExtras().getString("away");
                String awayImageName = schedule_DB.getTeamPictureURL(away);

                Resources res = getResources();
                int homeDrawableID = res.getIdentifier(homeImageName, "drawable", getActivity().getPackageName());
                int awayDrawableID = res.getIdentifier(awayImageName, "drawable", getActivity().getPackageName());
                ((ImageView) rootView.findViewById(R.id.detail_home_image)).setImageResource(homeDrawableID);
                ((ImageView) rootView.findViewById(R.id.detail_away_image)).setImageResource(awayDrawableID);
                String datetime = intent.getExtras().getString("datetime");

                String gameInfoStr = datetime;
                ((TextView) rootView.findViewById(R.id.detail_text))
                        .setText(gameInfoStr);
                Button ticketButton = (Button) rootView.findViewById(R.id.detail_ticket_button);
                ticketButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                            tickets_URL = tickets_URL + String.valueOf(position);
                            Intent ticketIntent = new Intent(Intent.ACTION_VIEW);
                            ticketIntent.setData(Uri.parse(tickets_URL));
                            startActivity(ticketIntent);
                        }
                    }
                );
            }
            //schedule_DB.close();


            return rootView;
        }
    }
}