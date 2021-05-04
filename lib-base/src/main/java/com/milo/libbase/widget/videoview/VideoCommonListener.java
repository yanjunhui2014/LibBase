package com.milo.libbase.widget.videoview;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/26
 */
public interface VideoCommonListener {

    void onDurationChanged(int totalDuration, int curDuration);

    void onVideoStatusChanged(@VideoStatus int videoStatus, String msg);

    void onSpeedChanged(float speed);

}
