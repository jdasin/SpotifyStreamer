package com.jdasin.www.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Daniel on 7/11/2015.
 */
public class Track implements Parcelable {
    public static final String TABLE_NAME = "artist";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String IMAGE_URL = "image_url";


    private String trackName;
    private Album album;

    private String albumName;
    private String albumLargeImage;
    private String albumSmallImage;
    private String playUrl;

    public Track(kaaes.spotify.webapi.android.models.Track track) {
        this.setTrackName(track.name);
        this.setAlbum(track.album != null ? new Album(track.album) : null);
        this.setPlayUrl(track.preview_url);
    }
    public Track(Parcel parcel) {
        this.setTrackName(parcel.readString());
        this.setAlbum((Album)parcel.readParcelable(Album.class.getClassLoader()));
        this.setPlayUrl(parcel.readString());
    }
    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trackName);
        dest.writeParcelable(this.getAlbum(), 0);
        dest.writeString(this.getPlayUrl());
    }

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel parcel) {
            return new Track(parcel);
        }

        @Override
        public Track[] newArray(int i) {
            return new Track[i];
        }

    };
}
