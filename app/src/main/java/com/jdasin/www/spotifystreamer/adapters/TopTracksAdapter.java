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
        ViewHolder holder;
        View itemView;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.track_list_item, parent, false);
            holder = new ViewHolder();
            holder.setNameTextView((TextView) itemView.findViewById(R.id.track_name));
            holder.setAlbumTextView((TextView) itemView.findViewById(R.id.track_album));
            holder.setImageView((ImageView) itemView.findViewById(R.id.list_item_image));
            itemView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            itemView = convertView;
        }
        holder.getNameTextView().setText(track.getTrackName());
        Album album = track.getAlbum();
        if ((album != null)) {
            holder.getAlbumTextView().setText(album.getAlbumName());
            Picasso.with(getContext()).load(album.getSmallImageUrl()).resize(100,100).centerCrop().into(holder.getImageView());
        }
        return itemView;
    }

    private class ViewHolder {
        private TextView nameTextView;
        private TextView albumTextView;
        private ImageView imageView;

        public TextView getNameTextView() {
            return nameTextView;
        }

        public void setNameTextView(TextView nameTextView) {
            this.nameTextView = nameTextView;
        }

        public TextView getAlbumTextView() {
            return albumTextView;
        }

        public void setAlbumTextView(TextView albumTextView) {
            this.albumTextView = albumTextView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
