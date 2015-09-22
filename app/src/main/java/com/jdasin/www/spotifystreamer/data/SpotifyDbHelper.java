package com.jdasin.www.spotifystreamer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jdasin.www.spotifystreamer.data.SpotifyContract.ArtistEntry;
import com.jdasin.www.spotifystreamer.data.SpotifyContract.TrackEntry;
import com.jdasin.www.spotifystreamer.data.SpotifyContract.AlbumEntry;
/**
 * Created by Daniel on 9/16/2015.
 */
public class SpotifyDbHelper  extends SQLiteOpenHelper {

        // If you change the database schema, you must increment the database version.
        private static final int DATABASE_VERSION = 2;

        static final String DATABASE_NAME = "super_spotify_streamer.db";

        public SpotifyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            final String SQL_CREATE_ARTIST_TABLE = "CREATE TABLE " + ArtistEntry.TABLE_NAME + " (" +
                    ArtistEntry._ID + " STRING PRIMARY KEY," +
                    ArtistEntry.COLUMN_SPOTIFY_ID + " TEXT UNIQUE NOT NULL, " +
                    ArtistEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    ArtistEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL " +
                    " );";
            final String SQL_CREATE_ALBUM_TABLE = "CREATE TABLE " + AlbumEntry.TABLE_NAME + " (" +
                    AlbumEntry._ID + " STRING PRIMARY KEY," +
                    AlbumEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    AlbumEntry.COLUMN_SPOTIFY_ID + " TEXT UNIQUE NOT NULL, " +
                    AlbumEntry.COLUMN_LARGE_IMAGE_URL + " TEXT NOT NULL, " +
                    AlbumEntry.COLUMN_SMALL_IMAGE_URL + " TEXT NOT NULL " +
                    " );";
            final String SQL_CREATE_TRACK_TABLE = "CREATE TABLE " + TrackEntry.TABLE_NAME + " (" +
                    TrackEntry._ID + " STRING PRIMARY KEY AUTOINCREMENT," +
                    TrackEntry.COLUMN_ALBUM_KEY + " STRING NOT NULL, " +
                    TrackEntry.COLUMN_ARTIST_KEY + " STRING NOT NULL, " +
                    TrackEntry.COLUMN_SPOTIFY_ID + " TEXT UNIQUE NOT NULL, " +
                    TrackEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    TrackEntry.COLUMN_PLAY_URL + " TEXT NOT NULL," +
                    TrackEntry.COLUMN_RANK + " INTEGER NOT NULL," +
                    " FOREIGN KEY (" + TrackEntry.COLUMN_ARTIST_KEY + ") REFERENCES " +
                    ArtistEntry.TABLE_NAME + " (" + ArtistEntry._ID + "), " +
                    " FOREIGN KEY (" + TrackEntry.COLUMN_ALBUM_KEY + ") REFERENCES " +
                    AlbumEntry.TABLE_NAME + " (" + AlbumEntry._ID + ");";
            sqLiteDatabase.execSQL(SQL_CREATE_ARTIST_TABLE);
            sqLiteDatabase.execSQL(SQL_CREATE_ALBUM_TABLE);
            sqLiteDatabase.execSQL(SQL_CREATE_TRACK_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrackEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ArtistEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AlbumEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }

}
