package com.video360;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

public class Video360Component extends RelativeLayout {

    private static String TAG = "Video360Component";
    private View view = null;

    protected VrVideoView videoWidgetView;
    private SeekBar seekBar;


    public Video360Component(Context context) {
        super(context);
        init();
    }
    public void init() {
        this.view = inflate(getContext(), R.layout.activity_video_player_360, this);

        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());
        videoWidgetView = (VrVideoView) view.findViewById(R.id.video_view);
        videoWidgetView.setEventListener(new ActivityEventListener());

    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoWidgetView.seekTo(progress);
            } // else this was from the ActivityEventHandler.onNewFrame()'s seekBar.setProgress update.
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    }


    public class ActivityEventListener extends VrVideoEventListener {
        @Override
        public void onLoadSuccess() {

            Log.i(TAG, "Successfully loaded video " + videoWidgetView.getDuration());
        }

        /**
         * Called by video widget on the UI thread on any asynchronous error.
         */
        @Override
        public void onLoadError(String errorMessage) {
            // An error here is normally due to being unable to decode the video format.
            Log.e(TAG, "Error loading video: " + errorMessage);
        }

        /**
         * Update the UI every frame.
         */
        @Override
        public void onNewFrame() {

        }

        /**
         * Make the video play in a loop. This method could also be used to move to the next video in
         * a playlist.
         */
        @Override
        public void onCompletion() {
            if(view != null) videoWidgetView.seekTo(0);
        }
    }
}
