package com.jdasin.www.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Daniel on 7/11/2015.
 */
public class Track implements Parcelable {
    private String trackName;
    private Album album;
    private String playUrl;
    private String id;
    public Track(kaaes.spotify.webapi.android.models.Track track) {
        this.setTrackName(track.name);
        this.setAlbum(track.album != null ? new Album(track.album) : null);
        this.setPlayUrl(track.preview_url);
        this.setId(track.id);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
