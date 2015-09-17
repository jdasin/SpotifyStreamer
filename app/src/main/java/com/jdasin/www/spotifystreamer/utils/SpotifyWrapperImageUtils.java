package com.jdasin.www.spotifystreamer.utils;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Daniel on 7/11/2015.
 */
public class SpotifyWrapperImageUtils {


    public static String getSmallURL(List<Image> images) {
        String smallImageURL = null;
        if (!images.isEmpty()) {
            Image smallestImage = images.get(0);
            for (Image image : images) {
                //Quick way to find small resolution
                if ((image.height + image.width) > (smallestImage.width + smallestImage.height)) {
                    smallestImage = image;
                }
            }
            smallImageURL = smallestImage.url;
        }
        return smallImageURL;
    }

    public static String getLargeImageURL(List<Image> images) {
        String largeImageURL = null;
        if (!images.isEmpty()) {
            Image largestImage = images.get(0);
            for (Image image : images) {
                //Quick way to find large resolution
                if ((image.height + image.width) > (largestImage.width + largestImage.height)) {
                    largestImage = image;
                }
            }
            largeImageURL = largestImage.url;
        }
        return largeImageURL;
    }
}
