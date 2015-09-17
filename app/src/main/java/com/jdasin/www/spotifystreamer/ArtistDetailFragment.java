package com.jdasin.www.spotifystreamer;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jdasin.www.spotifystreamer.adapters.TopTracksAdapter;
import com.jdasin.www.spotifystreamer.model.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistDetailFragment extends Fragment {

    private TopTracksAdapter mAdapter;
    public TrackListHandler mTrackListHandler;
    private ArrayList<Track> mTracks;

    public ArtistDetailFragment() {
        mTracks = new ArrayList<Track>();
    }
    @Override
      public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mTrackListHandler = (TrackListHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TrackListHandler.");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_artist_detail, container, false);
        ListView listView = (ListView) inflatedView.findViewById(R.id.list_view);
        mAdapter = new TopTracksAdapter(getActivity());
        listView.setAdapter(mAdapter);
        String artistId = getActivity().getIntent().getExtras().getString("artist_id");
        final String artistName = getActivity().getIntent().getExtras().getString("artist_name");
        ArtistTop10TracksTask task = new ArtistTop10TracksTask();
        task.execute(artistId);
        AdapterView.OnItemClickListener onITemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object clickedObject = parent.getAdapter().getItem(position);
                if (clickedObject instanceof Track) {
                    mTrackListHandler.onTrackSelected(position, mTracks, artistName);
                }
            }
        };
        listView.setOnItemClickListener(onITemClickListener);
        return inflatedView;
    }

    public class ArtistTop10TracksTask extends AsyncTask<String,Void,Tracks>{

        @Override
        protected Tracks doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotifyService = api.getService();
            Map<String, Object> otherParams = new HashMap<String, Object>();
            //Using BO for now since it is my country :)
            otherParams.put("country", "BO");
            otherParams.put(SpotifyService.OFFSET, 0);
            otherParams.put(SpotifyService.LIMIT, 10);
            Tracks tracks = spotifyService.getArtistTopTrack(params[0], otherParams);
            return tracks;
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            mAdapter.setNotifyOnChange(false);
            mAdapter.clear();
            mTracks.clear();
            if (tracks.tracks != null) {
                for (kaaes.spotify.webapi.android.models.Track track : tracks.tracks) {
                    Track trackItem = new Track(track);
                    mAdapter.add(new Track(track));
                    mTracks.add(trackItem);
                }
            }
            mAdapter.setNotifyOnChange(true);
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface TrackListHandler {
        public void onTrackSelected(int selectedTrackPosition, ArrayList<Track> tracks, String artistName);
    }
}
