package com.milo.libbase.demo.data.bean;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 6/3/21
 */
public interface MainItemData {

    int TYPE_DOWNLOAD      = 0;
    int TYPE_VIDEO_PLAY    = 1;
    int TYPE_SCREEN_RECORD = 2;

    int getType();

    String getName();

}
