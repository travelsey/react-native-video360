package com.video360;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.graphics.Color;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;


public class Video360Module extends SimpleViewManager {
    private static final String CLASS_NAME = "Video360";
    private static final String TAG = Video360Module.class.getSimpleName();
    private Video360Component view;
    private View view2;

    public Video360Module(ReactApplicationContext context) { super(); }

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @Override
    protected View createViewInstance(ThemedReactContext context) {
        view = new Video360Component(context.getCurrentActivity());
        //view.setEventListener(new ActivityEventListener());
       // Log.d(TAG, "createViewInstance: test");

      //  view2 = View.inflate(context.getCurrentActivity(), R.layout.activity_video_player_360,null);

        return view;
    }

    @ReactProp(name = "displayMode")
    public void setDisplayMode(Video360Component view, String mode) {
        switch(mode) {
            case "embedded":
                view.videoWidgetView.setDisplayMode(VrWidgetView.DisplayMode.EMBEDDED);
                break;
            case "fullscreen":
                view.videoWidgetView.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_MONO);
                break;
            case "cardboard":
                view.videoWidgetView.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_STEREO);
                break;
            default:
                view.videoWidgetView.setDisplayMode(VrWidgetView.DisplayMode.EMBEDDED);
                break;
        }
    }

    @ReactProp(name = "volume")
    public void setVolume(Video360Component view, float value) {
        view.videoWidgetView.setVolume(value);
    }

    @ReactProp(name = "enableFullscreenButton")
    public void setFullscreenButtonEnabled(Video360Component view, Boolean enabled) {
        view.videoWidgetView.setFullscreenButtonEnabled(enabled);
    }

    @ReactProp(name = "enableCardboardButton")
    public void setCardboardButtonEnabled(Video360Component view, Boolean enabled) {
        view.videoWidgetView.setStereoModeButtonEnabled(enabled);
    }

    @ReactProp(name = "enableTouchTracking")
    public void setTouchTrackingEnabled(Video360Component view, Boolean enabled) {
        view.videoWidgetView.setTouchTrackingEnabled(enabled);
    }

    @ReactProp(name = "hidesTransitionView")
    public void setTransitionViewEnabled(Video360Component view, Boolean enabled) {
        view.videoWidgetView.setTransitionViewEnabled(!enabled);
    }

    @ReactProp(name = "enableInfoButton")
    public void setInfoButtonEnabled(Video360Component view, Boolean enabled) {
        view.videoWidgetView.setInfoButtonEnabled(enabled);
    }

    @ReactProp(name = "colorBar")
    public void setColorBar(Video360Component view, String color) {
        view.seekBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
        view.seekBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
    }

    @ReactProp(name = "urlVideo")
    public void setVideo(Video360Component view, String url) {
       // String type = config.getString("type");
     //   Uri uri = Uri.parse(config.getString("uri"));

         Uri uri = Uri.parse(url);


        VrVideoView.Options videoOptions = new VrVideoView.Options();
        videoOptions.inputFormat = VrVideoView.Options.FORMAT_DEFAULT;
//
//        switch(type) {
//            case "mono":
//                videoOptions.inputType = VrVideoView.Options.TYPE_MONO;
//                break;
//            case "stereo":
//                videoOptions.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER;
//                break;
//            default:
//                videoOptions.inputType = VrVideoView.Options.TYPE_MONO;
//                break;
//        }
        
        VideoLoaderTask videoLoaderTask = new VideoLoaderTask();
        videoLoaderTask.execute(Pair.create(uri, videoOptions));
    }

    class VideoLoaderTask extends AsyncTask<Pair<Uri, VrVideoView.Options>, Void, Boolean> {
        @SuppressWarnings("WrongThread")
        protected Boolean doInBackground(Pair<Uri, VrVideoView.Options>... args) {
            try {
                view.videoWidgetView.loadVideo(args[0].first, args[0].second);
            } catch (IOException e) {}

            return true;
        }
    }
}