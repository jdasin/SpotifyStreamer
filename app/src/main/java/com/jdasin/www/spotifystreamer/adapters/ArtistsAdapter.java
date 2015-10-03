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
        View itemView;
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.artist_list_item, parent, false);
            holder = new ViewHolder();
            holder.setNameTextView((TextView) itemView.findViewById(R.id.list_item_text));
            holder.setImageView((ImageView) itemView.findViewById(R.id.list_item_image));
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            itemView = convertView;
        }
        holder.getNameTextView().setText(artist.getName());
        if ((artist.getImageUrl() != null)) {
            Picasso.with(getContext()).load(artist.getImageUrl()).resize(100, 100).centerCrop().into(holder.getImageView());
        }
        return itemView;
    }

    private class ViewHolder {
        private TextView nameTextView;
        private ImageView imageView;

        public void setNameTextView(TextView nameTextView) {
            this.nameTextView = nameTextView;
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
