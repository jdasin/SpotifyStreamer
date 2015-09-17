package com.jdasin.www.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import com.jdasin.www.spotifystreamer.adapters.ArtistsAdapter;
import com.jdasin.www.spotifystreamer.model.Artist;
import java.util.ArrayList;


import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArtistsAdapter mAdapter;
    private ArrayList<Artist> mArtists;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("results")) {
            mArtists = new ArrayList<Artist>();
        } else {
            mArtists = savedInstanceState.getParcelableArrayList("results");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView mListView = (ListView) inflatedView.findViewById(R.id.list_view);
        mAdapter = new ArtistsAdapter(getActivity(), mArtists);
        mListView.setAdapter(mAdapter);
        AdapterView.OnItemClickListener onITemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object clickedObject = parent.getAdapter().getItem(position);
                if (clickedObject instanceof Artist) {
                    Artist artist = (Artist)clickedObject;
                    Intent intent = new Intent(getActivity(),ArtistDetail.class);
                    intent.putExtra("artist_id", artist.getId());
                    intent.putExtra("artist_name", artist.getName());
                    startActivity(intent);
                }
            }
        };
        mListView.setOnItemClickListener(onITemClickListener);

            SearchView searchText = (SearchView)inflatedView.findViewById(R.id.search_text);
            searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    SpotifyArtistTask task = new SpotifyArtistTask();
                    task.execute(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        return inflatedView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("results", mArtists);
        super.onSaveInstanceState(outState);
    }
    public class SpotifyArtistTask extends AsyncTask<String,Void,ArtistsPager> {

        @Override
        protected ArtistsPager doInBackground(String... params) {
            if ((params.length > 0) && (!params[0].isEmpty())) {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                ArtistsPager artists = spotify.searchArtists(params[0]);
                return artists;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            mAdapter.setNotifyOnChange(false);
            mAdapter.clear();
            if ((artistsPager != null) && !artistsPager.artists.items.isEmpty()) {
                for (kaaes.spotify.webapi.android.models.Artist artist : artistsPager.artists.items) {
                    mAdapter.add(new Artist(artist));
                }
            } else {
                Toast.makeText(getActivity(), "No artist found.", Toast.LENGTH_LONG).show();
            }
            mAdapter.setNotifyOnChange(true);
            mAdapter.notifyDataSetChanged();
        }
    }
}
