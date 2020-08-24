package com.hsappdev.ahs.Misc;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

/**
 * See https://stackoverflow.com/questions/44427611/how-to-play-youtube-video-in-a-fragment-in-viewpager-android
 * A YouTube Player Fragment compatible with View Pagers/ multiple instances of videos
 * with the possible of intercepting full screen actions
 * Does this by lazy initializing video players and by releasing video players when needed
 * to prevent > 1 video players, causing all video fragments to play the same video
 */
public class MediaYoutubeFragment extends YouTubePlayerSupportFragmentX
        implements YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener {
    private static final String KEY_VIDEO_ID = "VIDEO_ID";
    public static final String YOUTUBE_API_KEY = "AIzaSyBRBGC45zgZd6JtLKDq2nfwDnW5symCwX8";
    public String apiKey = "yor apiKey";
    private String mVideoId;
    private YouTubePlayer mPlayer;
    boolean mIsFullScreen;
    private InitializeListener mInitializeListener;
    private MediaFullScreenListener fullScreenListener;

    public InitializeListener getmInitializeListener() {
        return mInitializeListener;
    }

    public void setmInitializeListener(InitializeListener mInitializeListener) {
        this.mInitializeListener = mInitializeListener;
    }

    public void setFullScreenListener(MediaFullScreenListener fullScreenListener) {
        this.fullScreenListener = fullScreenListener;
    }

    public interface MediaFullScreenListener{
        void onFullScreen(boolean fullScreen);
    }

    public interface InitializeListener{
        void startVideo();
        void stopVideo();
    }

    public MediaYoutubeFragment(){
        super();
        mInitializeListener = new InitializeListener() {
            @Override
            public void startVideo() {
                initialize();
            }

            @Override
            public void stopVideo() {
                if (mPlayer != null){
                    mPlayer.release();
                }
            }
        };
    }

    private void initialize() {
        initialize(YOUTUBE_API_KEY, this);
    }

    public static MediaYoutubeFragment newInstance(String videoId, MediaFullScreenListener listener) {
        MediaYoutubeFragment frag = new MediaYoutubeFragment();

        Bundle args = new Bundle();
        args.putString(KEY_VIDEO_ID, videoId);

        frag.setArguments(args);
        frag.setFullScreenListener(listener);
        return frag;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (getArguments() != null) {
            mVideoId = getArguments().getString(KEY_VIDEO_ID);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        mPlayer = youTubePlayer;
        if(youTubePlayer == null)
            return;
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        if(wasRestored) // wasRestored apparently
            youTubePlayer.play();
        else {
            youTubePlayer.cueVideo(mVideoId);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    @Override
    public void onFullscreen(boolean fullscreen) {
        /*mPlayer.setFullscreen(false);
        fullScreenListener.startFullScreen();*/
        fullScreenListener.onFullScreen(fullscreen);
    }

}