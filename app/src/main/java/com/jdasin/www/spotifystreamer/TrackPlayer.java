package com.jdasin.www.spotifystreamer;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jdasin.www.spotifystreamer.model.Album;
import com.jdasin.www.spotifystreamer.model.Track;
import com.jdasin.www.spotifystreamer.services.TrackPlayerService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Daniel on 8/19/2015.
 */
public class TrackPlayer extends DialogFragment {
    public static final String ARG_POSITION = "POSITION";
    public static final String ARG_TRACKS = "TRACKS";
    public static final String SPOTIFY_STREAMER_PLAYER = "SpotifyStreamerPlayer";
    public static final String ARG_ARTIST_NAME = "ARTIST_NAME";
    private Track mSelectedTrack;
    private ArrayList<Track> mTracks;
    private TrackPlayerService mService;
    private Intent mIntent;
    private ImageButton mPlayButton;
    private ImageButton mPauseButton;
    private int mSelectedPosition;
    private TextView mAlbumNameTextView;
    private ImageView mImageView;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;

    private MediaController controller;
    private boolean musicThreadFinished;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private TextView mTrackNameTextView;
    private Thread mTrackBarThread;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        Bundle bundle = getArguments();

        View fragmentView = inflater.inflate(R.layout.track_player_layout, container, false);
        mTracks = bundle.getParcelableArrayList(ARG_TRACKS);
        mSelectedPosition = bundle.getInt(ARG_POSITION);
        TextView mArtistNameTextView = (TextView) fragmentView.findViewById(R.id.artist_name);
        mArtistNameTextView.setText(bundle.getString(ARG_ARTIST_NAME));
        mTrackNameTextView = (TextView) fragmentView.findViewById(R.id.track_name);
        mAlbumNameTextView = (TextView) fragmentView.findViewById(R.id.album_name);
        mPlayButton = (ImageButton) fragmentView.findViewById(R.id.play_track);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTrack();
            }
        });
        mPauseButton = (ImageButton)fragmentView.findViewById(R.id.pause_track);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTrack();
            }
        });

        mNextButton = (ImageButton) fragmentView.findViewById(R.id.next_track);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTrack();
            }
        });
        mPreviousButton = (ImageButton) fragmentView.findViewById(R.id.previous_track);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousTrack();
            }
        });

        mImageView = (ImageView) fragmentView.findViewById(R.id.imageView);
        mSeekBar = (SeekBar) fragmentView.findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mService != null && fromUser) {
                    mService.seekTo(progress);
                }
            }
        });
        mCurrentTime = (TextView) fragmentView.findViewById(R.id.current_time);
        mTotalTime = (TextView) fragmentView.findViewById(R.id.total_time);
        musicThreadFinished = true;
        loadCurrentTrackInfo();
        return fragmentView;
    }

    public void loadCurrentTrackInfo() {
        mSelectedTrack = mTracks.get(mSelectedPosition);
        Album album = mSelectedTrack.getAlbum();
        String albumName = album != null ? album.getAlbumName() : "";
        String albumImageUrl = album != null ? album.getLargeImageUrl() : "";
        mAlbumNameTextView.setText(albumName);
        mTrackNameTextView.setText(mSelectedTrack.getTrackName());
        Picasso.with(getActivity()).load(albumImageUrl).resize(640,640).centerCrop().into(mImageView);
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mIntent == null){
            mIntent = new Intent(getActivity(), TrackPlayerService.class);
            getActivity().bindService(mIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(mIntent);
        } else {
            getActivity().bindService(mIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mServiceConnection);
    }

    private boolean mServiceBound = false;
    private ServiceConnection mServiceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackPlayerService.TrackPlayerBinder binder = (TrackPlayerService.TrackPlayerBinder)service;
            //get service
            mService = binder.getService();
            boolean continuePlaying = mService.isPlaying() && (mSelectedTrack.getId() == mService.getCurrentTrack().getId());
            if (continuePlaying) {
                musicThreadFinished = true;
                launchTrackbarThread();
                showPauseButton();
            }
            //pass list
            mService.setTracks(mTracks);
            mService.setSongPosition(mSelectedPosition);
            mServiceBound = true;
            if (!continuePlaying) {
                playTrack();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    private String getAsTime(int timeInMilliSecs) {
        String formatted = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeInMilliSecs),
                TimeUnit.MILLISECONDS.toSeconds(timeInMilliSecs)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilliSecs)));
        return formatted;
    }

    public void playTrack() {
        mService.playSong();
        showPauseButton();
        launchTrackbarThread();
    }

    private void launchTrackbarThread() {
        if (musicThreadFinished) {
            mTrackBarThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    musicThreadFinished = false;
                    int currentPosition = 0;
                    while (!musicThreadFinished || !Thread.currentThread().isInterrupted()) {
                        try {
                            Thread.sleep(1000);
                            currentPosition = mService.getPlayingPosition();
                        } catch (InterruptedException e) {
                            Log.e(SPOTIFY_STREAMER_PLAYER, e.getMessage(), e);
                            return;
                        } catch (Exception e) {
                            Log.e(SPOTIFY_STREAMER_PLAYER, e.getMessage(), e);
                            return;
                        }
                        final int total = mService.getDuration();
                        final String totalTime = getAsTime(total);
                        final String curTime = getAsTime(currentPosition);

                        mSeekBar.setMax(total);
                        if (!mService.isPaused()) {
                            mSeekBar.setProgress(currentPosition);
                            Activity activity = getActivity();
                            if (activity != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean isFinished = mService.isFinished();
                                        if (isFinished) {
                                            mCurrentTime.setText(totalTime);
                                            mSeekBar.setProgress(total);
                                            showPlayButton();
                                            musicThreadFinished = true;
                                        } else {
                                            mCurrentTime.setText(curTime);
                                        }
                                        mTotalTime.setText(totalTime);
                                    }
                                });
                            } else {
                                Thread.currentThread().interrupt();
                                musicThreadFinished = true;
                            }
                        }
                    }
                }
            });
            mTrackBarThread.start();
        }
    }


    public void pauseTrack() {
        mService.pauseSong();
        showPlayButton();
    }

    public void nextTrack() {
        mService.nextSong();
        mSelectedPosition = mService.getSongPosition();
        loadCurrentTrackInfo();
        playTrack();
    }

    public void previousTrack(){
        mService.previousSong();
        mSelectedPosition = mService.getSongPosition();
        loadCurrentTrackInfo();
        playTrack();
    }

    public void showPlayButton() {
        mPlayButton.setVisibility(View.VISIBLE);
        mPauseButton.setVisibility(View.GONE);
    }

    public void showPauseButton() {
        mPlayButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
    }
}
