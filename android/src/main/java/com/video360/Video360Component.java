package com.video360;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageButton;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

public class Video360Component extends RelativeLayout {

    private static String TAG = "Video360Component";
    private View view = null;

    protected VrVideoView videoWidgetView;
    protected SeekBar seekBar;
    private RelativeLayout viewControls;
    private TextView statusText;
    private ImageButton playToggle;

    private boolean isPaused = false;

    public Video360Component(Context context) {
        super(context);
        init();
    }
    public void init() {
        this.view = inflate(getContext(), R.layout.activity_video_player_360, this);
        viewControls = (RelativeLayout) view.findViewById(R.id.viewControls);
        //viewControls.setVisibility(View.GONE);
        statusText = (TextView) view.findViewById(R.id.status_text);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());
        videoWidgetView = (VrVideoView) view.findViewById(R.id.video_view);
        videoWidgetView.setEventListener(new ActivityEventListener());

        playToggle = (ImageButton) view.findViewById(R.id.play_toggle);
        playToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePause();
            }
        });
    }

    private void updateStatusText() {
        StringBuilder status = new StringBuilder();
        status.append(isPaused ? "Paused: " : "Playing: ");
        status.append(String.format("%.2f", videoWidgetView.getCurrentPosition() / 1000f));
        status.append(" / ");
        status.append(videoWidgetView.getDuration() / 1000f);
        status.append(" seconds.");
        statusText.setText(status.toString());

        if(isPaused){
            viewControls.setVisibility(View.VISIBLE);
        }else{
            viewControls.setVisibility(View.GONE);
        }
    }

     private void togglePause() {
        playToggle.setImageResource(isPaused ? R.drawable.pause : R.drawable.play);
        if (isPaused) {
            videoWidgetView.playVideo();
        } else {
            videoWidgetView.pauseVideo();
        }
        isPaused = !isPaused;
        updateStatusText();
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoWidgetView.seekTo(progress);
                updateStatusText();
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
            seekBar.setMax((int) videoWidgetView.getDuration());
            updateStatusText();
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

         @Override
        public void onClick() {
            togglePause();
        }

        /**
         * Update the UI every frame.
         */
        @Override
        public void onNewFrame() {
            updateStatusText();
            seekBar.setProgress((int) videoWidgetView.getCurrentPosition());
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
