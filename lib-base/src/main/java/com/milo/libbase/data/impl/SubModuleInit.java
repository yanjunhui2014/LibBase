package com.milo.libbase.data.impl;

import android.app.Application;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 5/4/21
 */
public interface SubModuleInit {

    void init(Application app);

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);
}
