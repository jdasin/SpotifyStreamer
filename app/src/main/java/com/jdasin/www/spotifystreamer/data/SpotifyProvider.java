package com.jdasin.www.spotifystreamer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Daniel on 9/16/2015.
 */
public class SpotifyProvider  extends ContentProvider {


        // The URI Matcher used by this content provider.
        private static final UriMatcher sUriMatcher = buildUriMatcher();
        private SpotifyDbHelper mOpenHelper;

        static final int ARTIST = 100;
        static final int TRACK = 101;
        static final int ALBUM = 102;
        static final int ARTIST_BY_ID = 103;
        static final int TRACK_BY_ID = 104;
        static final int ALBUM_BY_ID = 105;
        static final int ARTIST_TRACKS = 106;

        private static final SQLiteQueryBuilder sTrackByArtistBuilder;

        static{
            sTrackByArtistBuilder = new SQLiteQueryBuilder();

            sTrackByArtistBuilder.setTables(
                    SpotifyContract.TrackEntry.TABLE_NAME + " INNER JOIN " +
                            SpotifyContract.AlbumEntry.TABLE_NAME +
                            " ON " + SpotifyContract.TrackEntry.TABLE_NAME +
                            "." + SpotifyContract.TrackEntry.COLUMN_ARTIST_KEY +
                            " = " + SpotifyContract.ArtistEntry.TABLE_NAME +
                            "." + SpotifyContract.ArtistEntry._ID);
        }

        private static final String sArtistByTermSelection =
            SpotifyContract.ArtistEntry.TABLE_NAME+
                        "." + SpotifyContract.ArtistEntry.COLUMN_NAME + " LIKE ?% ";
        private static final String sArtistById =
            SpotifyContract.ArtistEntry.TABLE_NAME+
                    "." + SpotifyContract.ArtistEntry.COLUMN_SPOTIFY_ID + " = ? OR " + SpotifyContract.ArtistEntry._ID + " = ? ";
        private static final String sAlbumById =
            SpotifyContract.AlbumEntry.TABLE_NAME+
                    "." + SpotifyContract.AlbumEntry.COLUMN_SPOTIFY_ID + " = ? OR " + SpotifyContract.ArtistEntry._ID + " = ? ";
        private static final String sTrackById =
            SpotifyContract.TrackEntry.TABLE_NAME+
                    "." + SpotifyContract.TrackEntry.COLUMN_SPOTIFY_ID + " = ? OR " + SpotifyContract.ArtistEntry._ID + " = ? ";
        private static final String sTrackByArtistSelection =
            SpotifyContract.TrackEntry.TABLE_NAME+
                    "." + SpotifyContract.TrackEntry.COLUMN_ARTIST_KEY + " = ? ";

        private Cursor getTrackByArtist(Uri uri, String[] projection, String sortOrder) {
            String artistId = SpotifyContract.TrackEntry.getArtistIdFromUri(uri);

            String[] selectionArgs = null;
            String selection = null;
            if (artistId != null) {
                selection = sTrackByArtistSelection ;
                selectionArgs = new String[]{artistId};
            }

            return sTrackByArtistBuilder.query(mOpenHelper.getReadableDatabase(),
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
        }

        private Cursor getArtistByTerm(
            Uri uri, String[] projection, String sortOrder) {

            String searchTerm = SpotifyContract.ArtistEntry.getSearchTerm(uri);

            return mOpenHelper.getReadableDatabase().query(
                    SpotifyContract.ArtistEntry.TABLE_NAME,
                    projection,
                    sArtistByTermSelection,
                    new String[]{searchTerm},
                    null,
                    null,
                    sortOrder
            );
        }

        private Cursor getArtistById(
                Uri uri, String[] projection, String sortOrder) {

            String id = SpotifyContract.ArtistEntry.getIdFromUri(uri);
            return mOpenHelper.getReadableDatabase().query(
                    SpotifyContract.ArtistEntry.TABLE_NAME,
                    projection,
                    sArtistById,
                    new String[]{id, id},
                    null,
                    null,
                    sortOrder
            );
        }

        private Cursor getAlbumById(
                Uri uri, String[] projection, String sortOrder) {

            String id = SpotifyContract.AlbumEntry.getIdFromUri(uri);
            return mOpenHelper.getReadableDatabase().query(
                    SpotifyContract.AlbumEntry.TABLE_NAME,
                    projection,
                    sAlbumById,
                    new String[]{id, id},
                    null,
                    null,
                    sortOrder
            );
        }

        private Cursor getTrackById(
                Uri uri, String[] projection, String sortOrder) {

            String id = SpotifyContract.TrackEntry.getIdFromUri(uri);
            return mOpenHelper.getReadableDatabase().query(
                    SpotifyContract.TrackEntry.TABLE_NAME,
                    projection,
                    sTrackById,
                    new String[]{id, id},
                    null,
                    null,
                    sortOrder
            );
        }

        static UriMatcher buildUriMatcher() {
            final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
            final String authority = SpotifyContract.CONTENT_AUTHORITY;

            matcher.addURI(authority, SpotifyContract.PATH_ALBUM, ALBUM);
            matcher.addURI(authority, SpotifyContract.PATH_TRACK, TRACK);
            matcher.addURI(authority, SpotifyContract.PATH_ARTIST, ARTIST);
            matcher.addURI(authority, SpotifyContract.PATH_ARTIST + "/TRACKS/" , ARTIST_TRACKS);
            matcher.addURI(authority, SpotifyContract.PATH_ALBUM + "/*", ALBUM_BY_ID);
            matcher.addURI(authority, SpotifyContract.PATH_TRACK  + "/*", TRACK_BY_ID);
            matcher.addURI(authority, SpotifyContract.PATH_ARTIST + "/*", ARTIST_BY_ID);
            return matcher;
        }


        @Override
        public boolean onCreate() {
            mOpenHelper = new SpotifyDbHelper(getContext());
            return true;
        }

        @Override
        public String getType(Uri uri) {
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case ARTIST:
                    return SpotifyContract.ArtistEntry.CONTENT_TYPE;
                case ARTIST_BY_ID:
                    return SpotifyContract.ArtistEntry.CONTENT_ITEM_TYPE;
                case ALBUM:
                    return SpotifyContract.AlbumEntry.CONTENT_TYPE;
                case ALBUM_BY_ID:
                    return SpotifyContract.AlbumEntry.CONTENT_ITEM_TYPE;
                case TRACK:
                    return SpotifyContract.TrackEntry.CONTENT_TYPE;
                case TRACK_BY_ID:
                    return SpotifyContract.TrackEntry.CONTENT_ITEM_TYPE;
                case ARTIST_TRACKS:
                    return SpotifyContract.TrackEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                            String sortOrder) {
            // Here's the switch statement that, given a URI, will determine what kind of request it is,
            // and query the database accordingly.
            Cursor retCursor;
            switch (sUriMatcher.match(uri)) {
                //Artist/
                case ARTIST:
                {
                    retCursor = getArtistByTerm(uri, projection, sortOrder);
                    break;
                }
                // "Artist/*"
                case ARTIST_BY_ID: {
                    retCursor = getArtistById(uri, projection, sortOrder);
                    break;
                }
                // "Album/"
                case ALBUM: {
                    retCursor = mOpenHelper.getReadableDatabase().query(
                            SpotifyContract.AlbumEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );
                    break;
                }
                // "Album/*"
                case ALBUM_BY_ID: {
                    retCursor = getAlbumById(uri, projection, sortOrder);
                    break;
                }
                //track/
                case TRACK: {
                    retCursor = getTrackByArtist(uri, projection, sortOrder);
                    break;
                }
                //track/*
                case TRACK_BY_ID: {
                    retCursor = getTrackById(uri, projection, sortOrder);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
            return retCursor;
        }

        /*
            Student: Add the ability to insert Locations to the implementation of this function.
         */
        @Override
        public Uri insert(Uri uri, ContentValues values) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            Uri returnUri = null;

            switch (match) {
                case ARTIST: {
                    long _id = db.insert(SpotifyContract.ArtistEntry.TABLE_NAME, null, values);
                    if ( _id > 0 )
                        returnUri = SpotifyContract.ArtistEntry.buildArtistUri(_id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }
                case ALBUM: {
                    long _id = db.insert(SpotifyContract.AlbumEntry.TABLE_NAME, null, values);
                    if ( _id > 0 )
                        returnUri = SpotifyContract.AlbumEntry.buildAlbumUri(_id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }
                case TRACK: {
                    long _id = db.insert(SpotifyContract.TrackEntry.TABLE_NAME, null, values);
                    if ( _id > 0 )
                        returnUri = SpotifyContract.TrackEntry.buildTrackUri(_id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int rowsDeleted = 0;
            // this makes delete all rows return the number of rows deleted
            if ( null == selection ) selection = "1";
            switch (match) {
                case ARTIST:
                    rowsDeleted = db.delete(
                            SpotifyContract.ArtistEntry.TABLE_NAME, selection, selectionArgs);
                    break;
                case ALBUM:
                        rowsDeleted = db.delete(
                                SpotifyContract.AlbumEntry.TABLE_NAME, selection, selectionArgs);
                        break;
                case TRACK:
                        rowsDeleted = db.delete(
                                SpotifyContract.TrackEntry.TABLE_NAME, selection, selectionArgs);
                        break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            // Because a null deletes all rows
            if (rowsDeleted != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsDeleted;
        }

        @Override
        public int update(
                Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int rowsUpdated = 0;

            switch (match) {
                case ARTIST:
                    rowsUpdated = db.update(SpotifyContract.ArtistEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                case ALBUM:
                    rowsUpdated = db.update(SpotifyContract.AlbumEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                case TRACK:
                    rowsUpdated = db.update(SpotifyContract.TrackEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
        }

        @Override
        public int bulkInsert(Uri uri, ContentValues[] values) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int returnCount = 0;
            switch (match) {
                case ARTIST:
                    db.beginTransaction();
                    try {
                        for (ContentValues value : values) {
                            long _id = db.insert(SpotifyContract.ArtistEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return returnCount;
                case ALBUM:
                    db.beginTransaction();
                    try {
                        for (ContentValues value : values) {
                            long _id = db.insert(SpotifyContract.AlbumEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return returnCount;
                case TRACK:
                    db.beginTransaction();
                    try {
                        for (ContentValues value : values) {
                            long _id = db.insert(SpotifyContract.TrackEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return returnCount;
                default:
                    return super.bulkInsert(uri, values);
            }
        }
}
