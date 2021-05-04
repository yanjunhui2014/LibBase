package com.milo.libbase.widget.videoview;

import android.view.View;
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
public interface TopBar {

    View getBackView();

    TextView getTitleTextView();

    View getMenuView();

    View getTopBarView();

}
