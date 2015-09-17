package com.jdasin.www.spotifystreamer.services;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jdasin.www.spotifystreamer.model.Track;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Daniel on 8/20/2015.
 */
public class TrackPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    public static final String ACTION_PLAY = "com.jdasin.www.action.PLAY";
    public static final String ARG_URL = "URL";
    private MediaPlayer mMediaPlayer = null;
    private ArrayList<Track> mTracks;
    private final IBinder mBinder = new TrackPlayerBinder();
    private int mSongPosition;
    private boolean isPrepared;
    private boolean isPlaying;
    private boolean isFinished;
    private boolean isPaused;
    private int mLastPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        isPlaying = false;
        isFinished = false;
    }

    public void onPrepared(MediaPlayer player) {
        isPrepared = true;
        isFinished = false;
        isPlaying = true;
        isPaused = false;
        player.seekTo(mLastPosition);
        player.start();

    }
    @Override
    public void onDestroy() {
        if (getMediaPlayer() != null) getMediaPlayer().release();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!isPaused()) {
            isPlaying = false;
            isFinished = true;
            mLastPosition = 0;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onUnbind(Intent intent){
        getMediaPlayer().stop();
        getMediaPlayer().release();
        mMediaPlayer = null;
        return false;
    }

    public void setTracks(ArrayList<Track> list) {
        this.mTracks = list;
    }

    public ArrayList<Track> getTracks() {
        return this.mTracks;
    }

    public int getSongPosition() {
        return mSongPosition;
    }

    public void setSongPosition(int mSongPosition) {
        this.mSongPosition = mSongPosition;
    }

    //MediaPlayer controls
    public void playSong(){
        isPrepared = false;
        isPlaying = true;
        isFinished = false;
        getMediaPlayer().reset();
        String url = this.getTracks().get(getSongPosition()).getPlayUrl();
        Uri myUri = Uri.parse(url);
        try {
            getMediaPlayer().setDataSource(getApplicationContext(), myUri);
            getMediaPlayer().setOnPreparedListener(this);
            getMediaPlayer().setOnCompletionListener(this);
            getMediaPlayer().prepareAsync(); // prepare async to not block main thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseSong() {
        isPlaying = false;
        isPaused = true;
        mLastPosition = mMediaPlayer.getCurrentPosition();
        this.getMediaPlayer().pause();
    }

    public void nextSong(){
        if (getSongPosition() < mTracks.size() - 1) {
            setSongPosition(getSongPosition() + 1);
        }
    }

    public void previousSong(){
        if (getSongPosition() > 0) {
            setSongPosition(getSongPosition() - 1);
        }
    }

    public int getDuration(){
        int duration = getMediaPlayer().getDuration();
        if (!isPrepared) {
            duration = 0;
        }
        return duration;
    }

    public boolean isPlaying() {
        return getMediaPlayer().isPlaying() && isPlaying;
    }

    public int getPlayingPosition() {
        int currentDuration = getMediaPlayer().getCurrentPosition();
        if (!isPrepared) {
            currentDuration = 0;
        }
        return currentDuration;
    }

    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    public MediaPlayer getMediaPlayer() {
        if ( mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        return mMediaPlayer;
    }

    public boolean isFinished() {
        return isFinished && isPrepared;
    }

    public boolean isPaused() {
        return isPaused;
    }


    public class TrackPlayerBinder extends Binder {
        public TrackPlayerService getService() {
            return TrackPlayerService.this;
        }
    }

}
