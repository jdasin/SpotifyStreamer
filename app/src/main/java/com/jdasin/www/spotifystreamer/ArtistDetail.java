package com.jdasin.www.spotifystreamer;

import android.app.ActionBar;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jdasin.www.spotifystreamer.model.Track;

import java.util.ArrayList;

public class ArtistDetail extends ActionBarActivity implements ArtistDetailFragment.TrackListHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        String artistName = getIntent().getExtras().getString("artist_name");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
            ActionBar ab = getActionBar();
            if (ab != null) {
                ab.setTitle(R.string.top_tracks);
                ab.setSubtitle(artistName);
            } else {
                setTitle(getString(R.string.top_tracks)+ " - " + artistName);
            }
        } else {
            setTitle(getString(R.string.top_tracks)+ " - " + artistName);
        }
    }

    public void showDialog(Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        TrackPlayer newFragment = new TrackPlayer();
        newFragment.setArguments(args);
        if (false) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrackSelected(int selectedTrackPosition, ArrayList<Track> tracks, String artistName) {
        Bundle args = new Bundle();
        args.putInt(TrackPlayer.ARG_POSITION, selectedTrackPosition);
        args.putParcelableArrayList(TrackPlayer.ARG_TRACKS, tracks);
        args.putString(TrackPlayer.ARG_ARTIST_NAME, artistName);
        showDialog(args);
    }
}
