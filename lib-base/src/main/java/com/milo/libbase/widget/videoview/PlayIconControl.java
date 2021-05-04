package com.milo.libbase.widget.videoview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Title：播放图标控制器
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/4/15
 */
public interface PlayIconControl {

    int TYPE_PLAY  = 1;
    int TYPE_PAUSE = 2;

    void showPlayIcon(ViewGroup parent, int type);

    void hidePlayIcon(ViewGroup parent);

    View getPlayIconView();

}
