package com.jdasin.www.spotifystreamer;

/**
 * Created by Daniel on 9/29/2015.
 */
public class ArtistInfo {
    private String artistName;
    private String artistId;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public ArtistInfo(String artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
    }
}