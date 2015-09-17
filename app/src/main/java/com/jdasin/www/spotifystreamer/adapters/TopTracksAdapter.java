package com.jdasin.www.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdasin.www.spotifystreamer.R;
import com.jdasin.www.spotifystreamer.model.Album;
import com.jdasin.www.spotifystreamer.model.Track;
import com.squareup.picasso.Picasso;


/**
 * Created by Daniel on 7/5/2015.
 */
public class TopTracksAdapter extends ArrayAdapter<Track> {
    public TopTracksAdapter(Context context) {
        super(context, R.layout.artist_list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.track_list_item, parent, false);
        TextView trackNameTextView = (TextView) itemView.findViewById(R.id.track_name);
        trackNameTextView.setText(track.getTrackName());
        TextView albumNameTextView = (TextView) itemView.findViewById(R.id.track_album);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.list_item_image);
        Album album = track.getAlbum();
        if ((album != null)) {
            albumNameTextView.setText(album.getAlbumName());
            Picasso.with(getContext()).load(album.getSmallImageUrl()).resize(100,100).centerCrop().into(imageView);
        }
        return itemView;
    }
}
