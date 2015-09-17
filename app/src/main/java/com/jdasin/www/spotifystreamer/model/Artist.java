package com.jdasin.www.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jdasin.www.spotifystreamer.utils.SpotifyWrapperImageUtils;

/**
 * Created by Daniel on 7/11/2015.
 */
public class Artist implements Parcelable {
    private String id;
    private String name;
    private String imageUrl;

    public Artist(kaaes.spotify.webapi.android.models.Artist artist) {
        this.setId(artist.id);
        this.setName(artist.name);
        this.setImageUrl(SpotifyWrapperImageUtils.getSmallURL(artist.images));
    }

    public Artist(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.imageUrl = parcel.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getId());
        dest.writeString(this.getName());
        dest.writeString(this.getImageUrl());
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel parcel) {
            return new Artist(parcel);
        }

        @Override
        public Artist[] newArray(int i) {
            return new Artist[i];
        }

    };
}
