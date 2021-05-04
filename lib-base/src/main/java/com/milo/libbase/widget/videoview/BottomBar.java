package com.milo.libbase.widget.videoview;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/25
 */
public interface BottomBar {

    View getBottomBarView();

    ImageView getPlayView();

    TextView getCurTimeTextView();

    SeekBar getSeekBar();

    TextView getDefinitionTextView();

    TextView getSpeedTextView();

    TextView getTotalTimeTextView();

    ImageView getWholeScreenView();

    int[] getPlayViewResIds();

    int[] getWholeScreenResIds();

}
