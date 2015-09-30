package com.jdasin.www.spotifystreamer;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jdasin.www.spotifystreamer.model.Artist;
import com.jdasin.www.spotifystreamer.model.Track;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements ArtistDetailFragment.TrackListHandler, MainActivityFragment.ArtistListHandler {
    private static final String ARTIST_DETAIL_FRAGMENT_TAG = "ARTIST_DETAIL";
    private boolean mIsTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.artist_detail_container) != null) {
            mIsTwoPaneMode = true;
        } else {
            mIsTwoPaneMode = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void showDialog(Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        TrackPlayer newFragment = new TrackPlayer();
        newFragment.setArguments(args);
        // The device is using a large layout, so show the fragment as a dialog
        newFragment.show(fragmentManager, "dialog");

    }

    @Override
    public void onArtistSelected(Artist artist) {
        if (!mIsTwoPaneMode) {
            Intent intent = new Intent(this, ArtistDetail.class);
            intent.putExtra(ArtistDetail.ARTIST_PARAM, artist);
            startActivity(intent);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.artist_detail_container, ArtistDetailFragment.newInstance(artist), ARTIST_DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }
}
