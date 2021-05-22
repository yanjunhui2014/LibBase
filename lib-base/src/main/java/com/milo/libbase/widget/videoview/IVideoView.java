package com.milo.libbase.widget.videoview;


/**
 * Title：视频接口
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/25
 */
public interface IVideoView<T> {

    void prepare(VideoDefinition definition);

    void prepare(VideoDefinition definition, String picUrl);

    void prepare(VideoDefinition definition, String picUrl, String audioUrl);

    void prepare(String url);

    void prepare(String url, String picUrl);

    void prepare(String url, String picUrl, String audioUrl);

    void start();

    void stop();

    void release();

    void seek(int duration);

    int getDuration();

    int getCurDuration();

    boolean setMute(boolean mute);

    boolean isPlaying();

    boolean setSpeed(float speed);

    boolean supportSpeed();//是否支持变速

    boolean enableProxy();

    String getProxyUrl(String url);

    TopBar getTopBar();

    BottomBar getBottomBar();

    LoadingView getLoadingView();

    void showTopBar(long hideDelay);

    void showBottomBar(long hideDelay);

    void hideTopBar();

    void hideBottomBar();

    void changeTopBarVisibile(boolean visibile);

    void changeBottomBarVisibile(boolean visibile);

    T getCoreView();

}
