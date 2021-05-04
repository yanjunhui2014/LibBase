package com.milo.libbase.demo;

import androidx.multidex.MultiDexApplication;

import com.milo.libbase.LibBase;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 5/4/21
 */
public class DemoApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        new LibBase.LibBaseBuilder()
                .build(this);
    }

}
