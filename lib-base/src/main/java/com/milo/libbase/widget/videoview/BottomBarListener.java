package com.milo.libbase.widget.videoview;

import android.view.View;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/26
 */
public interface BottomBarListener {

    int SCALE_TYPE_BIG   = 1;
    int SCALE_TYPE_SMALL = 2;

    void onWholeScreenClick(View v, int scaleType);

    void onDefinitionChanged(VideoDefinition definition, String videoUrl);

}
