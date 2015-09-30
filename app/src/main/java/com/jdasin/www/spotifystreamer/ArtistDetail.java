package com.jdasin.www.spotifystreamer;

import android.app.ActionBar;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jdasin.www.spotifystreamer.model.Artist;
import com.jdasin.www.spotifystreamer.model.Track;

import java.util.ArrayList;

public class ArtistDetail extends ActionBarActivity implements ArtistDetailFragment.TrackListHandler {

    public static final String ARTIST_PARAM = "ARTIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        Artist artist = getIntent().getExtras().getParcelable(ARTIST_PARAM);
        String artistName = artist.getName();
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
        if (savedInstanceState == null) {
            ArtistDetailFragment fragment = ArtistDetailFragment.newInstance(artist);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.artist_detail_container, fragment)
                    .commit();
        }
    }

    public void showDialog(Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        TrackPlayer newFragment = new TrackPlayer();
        newFragment.setArguments(args);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_artist_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
