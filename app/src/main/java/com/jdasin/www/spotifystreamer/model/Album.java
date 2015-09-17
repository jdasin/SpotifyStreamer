package com.jdasin.www.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jdasin.www.spotifystreamer.utils.SpotifyWrapperImageUtils;

import kaaes.spotify.webapi.android.models.AlbumSimple;

/**
 * Created by Daniel on 7/11/2015.
 */
public class Album implements Parcelable {
    private String albumName;
    private String smallImageUrl;
    private String largeImageUrl;

    public Album(AlbumSimple album) {
        this.setAlbumName(album.name);
        this.setLargeImageUrl(SpotifyWrapperImageUtils.getLargeImageURL(album.images));
        this.setSmallImageUrl(SpotifyWrapperImageUtils.getSmallURL(album.images));
    }
    public Album(Parcel parcel) {
        this.setAlbumName(parcel.readString());
        this.setLargeImageUrl(parcel.readString());
        this.setSmallImageUrl(parcel.readString());
    }
    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getAlbumName());
        dest.writeString(this.getLargeImageUrl());
        dest.writeString(this.getSmallImageUrl());
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel parcel) {
            return new Album(parcel);
        }

        @Override
        public Album[] newArray(int i) {
            return new Album[i];
        }

    };
}
