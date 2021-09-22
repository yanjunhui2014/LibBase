package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.milo.libbase.utils.Utils;

/**
 * Title：二代播放器之ExoVideoView
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/25
 */
public class SimpleExoVideoView extends BaseVideoView<PlayerView> {

    protected PlayerView      mPlayerView;
    protected SimpleExoPlayer mExoPlayer;

    protected BaseMediaSource mMediaSource;
    protected int mPlaybackState = VideoStatus.STATE_NO_STSTE;

    public SimpleExoVideoView(Context context) {
        super(context);
    }

    public SimpleExoVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleExoVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepare(String url, String picUrl, String audioUrl) {
        super.prepare(url, picUrl, audioUrl);
        final String fileName = Utils.getFileName(url);
        final String userAgent = Util.getUserAgent(getContext(), "SimpleExoVideoView");

        if (fileName != null && fileName.endsWith(".m3u8")) {
            DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
            DefaultDataSourceFactory mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), BANDWIDTH_METER,
                    new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER));
            mMediaSource = new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(url));
        } else {
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
            if (enableProxy()) {
                url = getProxyUrl(url);
            }
            mMediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));
        }
        mExoPlayer.prepare(mMediaSource, true, true);
    }

    @Override
    public void start() {
        if (mMediaSource == null) {
            return;
        }
        if(mPlaybackState == VideoStatus.STATE_ENDED){
            mExoPlayer.seekTo(0);
        } else if(mExoPlayer.getDuration() != 0 && (mExoPlayer.getCurrentPosition() + 500) >= mExoPlayer.getDuration()){
            mExoPlayer.seekTo(0);
        }
        mExoPlayer.prepare(mMediaSource, false, false);
        mExoPlayer.setPlayWhenReady(true);

        if(bottomBar != null) {
            bottomBar.getPlayView().setImageResource(bottomBar.getPlayViewResIds()[1]);
        }
        if (showTopbar) {
            showTopBar(topBarHideDelay);
            showBottomBar(bottomBarHideDelay);
        }
    }

    @Override
    public void stop() {
        mExoPlayer.stop();
        if(bottomBar != null) {
            bottomBar.getPlayView().setImageResource(bottomBar.getPlayViewResIds()[0]);
        }
    }

    @Override
    public void release() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
        }
    }

    @Override
    public void seek(int duration) {
        mExoPlayer.seekTo(duration);
    }

    @Override
    public int getDuration() {
        int d = (int) mExoPlayer.getDuration();
        return d;
    }

    @Override
    public int getCurDuration() {
        int curD = (int) mExoPlayer.getCurrentPosition();
        return curD;
    }

    @Override
    public boolean setMute(boolean mute) {
        mExoPlayer.setVolume(mute ? 0.0f : 1.0f);
        return true;
    }

    @Override
    public boolean isPlaying() {
        return mExoPlayer.getPlaybackState() == Player.STATE_READY;
    }

    @Override
    public boolean setSpeed(float speed) {
        if (mExoPlayer != null) {
            PlaybackParameters playbackParameters = new PlaybackParameters(speed, 1.0F);
            mExoPlayer.setPlaybackParameters(playbackParameters);
            return true;
        }

        return false;
    }

    @Override
    public boolean supportSpeed() {
        return true;
    }

    @Override
    public PlayerView getCoreView() {
        if (mPlayerView == null) {
            mPlayerView = new PlayerView(getContext());
            mPlayerView.setBackgroundColor(Color.BLACK);
            mPlayerView.setUseController(false);
            mPlayerView.setFocusable(true);
        }

        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
            mExoPlayer.addListener(new EventListener() {
                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    if (commonListener != null) {
                        String msg = null;
                        if (error != null && error.getCause() != null) {
                            msg = error.getCause().getMessage();
                        }
                        commonListener.onVideoStatusChanged(VideoStatus.STATE_ERROR, msg);
                    }
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Log.d("ExoVideoView", "playbackState == " + playbackState);
                    mPlaybackState = playbackState;

                    if (playbackState == Player.STATE_IDLE) {
                        if (loadingView != null) {
                            loadingView.hideLoadingView(SimpleExoVideoView.this);
                        }
                        showIconPlayView(PlayIconControl.TYPE_PLAY);
                        if (commonListener != null) {
                            commonListener.onVideoStatusChanged(VideoStatus.STATE_IDLE, null);
                        }
                    } else if (playbackState == Player.STATE_BUFFERING) {
                        hidePlayIconView();
                        if (loadingView != null) {
                            loadingView.showLoadingView(SimpleExoVideoView.this);
                        }
                        if (commonListener != null) {
                            commonListener.onVideoStatusChanged(VideoStatus.STATE_BUFFERING, null);
                        }
                    } else if (playbackState == Player.STATE_READY) {
                        hidePlayIconView();
                        if (loadingView != null) {
                            loadingView.hideLoadingView(SimpleExoVideoView.this);
                        }
                        if (commonListener != null) {
                            commonListener.onVideoStatusChanged(VideoStatus.STATE_PLAYING, null);
                        }
                    } else if (playbackState == Player.STATE_ENDED) {
                        showIconPlayView(PlayIconControl.TYPE_PLAY);
                        if (commonListener != null) {
                            commonListener.onVideoStatusChanged(VideoStatus.STATE_ENDED, null);
                        }
                        if (videoLoop) {
                            mExoPlayer.seekTo(0);
                            start();
                        }
                    }
                }
            });
        }

        mPlayerView.setPlayer(mExoPlayer);
        return mPlayerView;
    }

    public void showIconPlayView(int type) {
        if (playIconControl != null) {
            playIconControl.showPlayIcon(SimpleExoVideoView.this, type);
            if (type == PlayIconControl.TYPE_PLAY) {
                playIconControl.getPlayIconView().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start();
                    }
                });
            } else {
                playIconControl.getPlayIconView().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stop();
                    }
                });
            }
        }
    }

    public void hidePlayIconView() {
        if (playIconControl != null) {
            playIconControl.hidePlayIcon(this);
        }
    }

}
