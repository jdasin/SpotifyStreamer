package com.jdasin.www.spotifystreamer.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Daniel on 9/16/2015.
 */
public class SpotifyContract {
    public static final String CONTENT_AUTHORITY = "com.jdasin.www.spotifystreamer.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_ARTIST = "artist";
    public static final String PATH_TRACK = "track";
    public static final String PATH_ALBUM = "album";

    public static final class ArtistEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTIST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTIST;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTIST;

        // Table name
        public static final String TABLE_NAME = "artist";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SPOTIFY_ID = "spotify_id";
        public static final String COLUMN_IMAGE_URL = "image_url";

        public static final String SEARCH_TERM = "search_term";

        public static Uri buildArtistUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
        public static Uri buildArtistUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
        public static Uri buildArtistByTermUri(String searchTerm) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(SEARCH_TERM, searchTerm).build();
        }
        public static String getSearchTerm(Uri uri) {
            return uri.getQueryParameter(SEARCH_TERM);
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class TrackEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRACK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRACK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRACK;

        public static final String TABLE_NAME = "track";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_ARTIST_KEY = "artist_id";

        public static final String COLUMN_ALBUM_KEY = "album_id";
        public static final String COLUMN_SPOTIFY_ID = "spotify_id";
        public static final String COLUMN_PLAY_URL = "play_url";

        public static final String COLUMN_RANK = "rank";

        public static Uri buildTrackUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
        public static Uri buildTrackUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }

        public static Uri buildTrackByArtistUri(long artistId) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_ARTIST_KEY, Long.toString(artistId)).build();
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getArtistIdFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_ARTIST_KEY);
        }
    }

    public static final class AlbumEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALBUM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALBUM;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALBUM;

        // Table name
        public static final String TABLE_NAME = "album";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SPOTIFY_ID = "spotify_id";
        public static final String COLUMN_LARGE_IMAGE_URL = "large_image_url";

        public static final String COLUMN_SMALL_IMAGE_URL = "small_image_url";

        public static Uri buildAlbumUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
        public static Uri buildAlbumUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
