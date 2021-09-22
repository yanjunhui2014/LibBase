package com.milo.libbase.widget.videoview;

import androidx.annotation.IntDef;

/**
 * Title：视频变化的事件
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/25
 */
@IntDef({VideoStatus.STATE_ERROR, VideoStatus.STATE_NO_STSTE, VideoStatus.STATE_IDLE, VideoStatus.STATE_BUFFERING, VideoStatus.STATE_PLAYING, VideoStatus.STATE_ENDED})
public @interface VideoStatus {

    int STATE_ERROR = -1;

    int STATE_NO_STSTE = 0;

    int STATE_IDLE = 1;

    int STATE_BUFFERING = 2;

    int STATE_PLAYING = 3;

    int STATE_ENDED = 5;

}
