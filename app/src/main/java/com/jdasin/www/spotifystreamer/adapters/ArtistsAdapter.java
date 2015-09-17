package com.jdasin.www.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdasin.www.spotifystreamer.R;
import com.jdasin.www.spotifystreamer.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Daniel on 7/5/2015.
 */
public class ArtistsAdapter extends ArrayAdapter<Artist>{
    public ArtistsAdapter(Context context, List<Artist> artists) {
        super(context, R.layout.artist_list_item, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Artist artist = getItem(position);
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.artist_list_item,parent,false);
        TextView textView = (TextView) itemView.findViewById(R.id.list_item_text);
        textView.setText(artist.getName());
        ImageView imageView = (ImageView) itemView.findViewById(R.id.list_item_image);
        if (artist.getImageUrl() != null) {
            Picasso.with(getContext()).load(artist.getImageUrl()).resize(100,100).centerCrop().into(imageView);
        }
        return itemView;
    }
}
