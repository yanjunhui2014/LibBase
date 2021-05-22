package com.milo.libbase.widget.videoview;

import android.content.Context;

/**
 * Title：二代视频播放器构造器
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/4/19
 */
public class BaseVideoViewBuilder<T extends BaseVideoView> {

    private boolean showTopbar         = true;
    private boolean showBottomBar      = true;
    private long    topBarHideDelay    = 3000;
    private long    bottomBarHideDelay = 3000;
    private boolean volumeEnable       = true;
    private boolean brightnessEnable   = true;
    private boolean videoLoop          = false;
    private boolean showSpeedView      = false;
    private float   speedValue[]       = {1.0f, 1.25f, 1.5f};

    private String videoTitle;

    private TopBar          topBar;
    private BottomBar       bottomBar;
    private LoadingView     loadingView;
    private PlayIconControl playIconControl;

    private TopBarListener    topBarListener;
    private BottomBarListener bottomBarListener;

    private VideoCommonListener commonListener;

    public BaseVideoViewBuilder() {
    }

    public BaseVideoViewBuilder<T> setShowTopbar(boolean showTopbar) {
        this.showTopbar = showTopbar;
        return this;
    }

    public BaseVideoViewBuilder<T> setShowBottomBar(boolean showBottomBar) {
        this.showBottomBar = showBottomBar;
        return this;
    }

    public BaseVideoViewBuilder<T> setTopBarHideDelay(long topBarHideDelay) {
        this.topBarHideDelay = topBarHideDelay;
        return this;
    }

    public BaseVideoViewBuilder<T> setBottomBarHideDelay(long bottomBarHideDelay) {
        this.bottomBarHideDelay = bottomBarHideDelay;
        return this;
    }

    public BaseVideoViewBuilder<T> setVolumeEnable(boolean volumeEnable) {
        this.volumeEnable = volumeEnable;
        return this;
    }

    public BaseVideoViewBuilder<T> setBrightnessEnable(boolean brightnessEnable) {
        this.brightnessEnable = brightnessEnable;
        return this;
    }

    public BaseVideoViewBuilder<T> setVideoLoop(boolean videoLoop) {
        this.videoLoop = videoLoop;
        return this;
    }

    public BaseVideoViewBuilder<T> setShowSpeedView(boolean showSpeedView) {
        this.showSpeedView = showSpeedView;
        return this;
    }

    public BaseVideoViewBuilder<T> setSpeedValue(float[] speedValue) {
        this.speedValue = speedValue;
        return this;
    }

    public BaseVideoViewBuilder<T> setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
        return this;
    }

    public BaseVideoViewBuilder<T> setTopBar(TopBar topBar) {
        this.topBar = topBar;
        return this;
    }

    public BaseVideoViewBuilder<T> setBottomBar(BottomBar bottomBar) {
        this.bottomBar = bottomBar;
        return this;
    }

    public BaseVideoViewBuilder<T> setLoadingView(LoadingView loadingView) {
        this.loadingView = loadingView;
        return this;
    }

    public BaseVideoViewBuilder<T> setPlayIconControl(PlayIconControl playIconControl) {
        this.playIconControl = playIconControl;
        return this;
    }

    public BaseVideoViewBuilder<T> setTopBarListener(TopBarListener topBarListener) {
        this.topBarListener = topBarListener;
        return this;
    }

    public BaseVideoViewBuilder<T> setBottomBarListener(BottomBarListener bottomBarListener) {
        this.bottomBarListener = bottomBarListener;
        return this;
    }

    public BaseVideoViewBuilder<T> setCommonListener(VideoCommonListener commonListener) {
        this.commonListener = commonListener;
        return this;
    }

    public T build(Context context, T baseVideoView) {
        if (showTopbar && topBar == null) {
            topBar = new SimpleTopBar(context);
        }
        if (showBottomBar && bottomBar == null) {
            bottomBar = new SimpleBottomBar(context);
        }
        if (loadingView == null) {
            loadingView = new SimpleLoadingView(context);
        }
        if (playIconControl == null) {
            playIconControl = new SimplePlayIconControl(context);
        }

        baseVideoView.videoTitle = videoTitle;

        baseVideoView.showTopbar = showTopbar;
        baseVideoView.showBottomBar = showBottomBar;
        baseVideoView.topBarHideDelay = topBarHideDelay;
        baseVideoView.bottomBarHideDelay = bottomBarHideDelay;
        baseVideoView.volumeEnable = volumeEnable;
        baseVideoView.brightnessEnable = brightnessEnable;
        baseVideoView.videoLoop = videoLoop;
        baseVideoView.showSpeedView = showSpeedView;
        baseVideoView.speedValue = speedValue;

        baseVideoView.topBar = topBar;
        baseVideoView.bottomBar = bottomBar;
        baseVideoView.loadingView = loadingView;
        baseVideoView.playIconControl = playIconControl;
        baseVideoView.topBarListener = topBarListener;
        baseVideoView.bottomBarListener = bottomBarListener;
        baseVideoView.commonListener = commonListener;
        baseVideoView.init();

        return baseVideoView;
    }

}
