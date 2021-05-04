package com.milo.libbase.widget.videoview;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * 标题：
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/9/23 17:48
 */
public class BrightnessHelper {

    private static BrightnessHelper mInstance;

    private BrightnessHelper() {
    }

    public static BrightnessHelper getInstance() {
        synchronized (BrightnessHelper.class) {
            if (mInstance == null) {
                mInstance = new BrightnessHelper();
            }
        }
        return mInstance;
    }

    public void setWindowBrightness(Context context, int brightness) {
        if (context == null) {
            return;
        }
        brightness = Math.min(255, brightness);
        brightness = Math.max(0, brightness);

        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.screenBrightness = brightness / 255.0f;
            window.setAttributes(lp);
        }
    }

    public int getWindowBrightness(Context context) {
        if (context == null) {
            return 0;
        }
        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            if (lp.screenBrightness == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE) {
                return getSystemBrightness(context);
            }
            return (int) (lp.screenBrightness * 255.0f);
        }

        return 0;
    }

    /**
     * 以0-100为范围，获取当前的亮度值
     *
     * @return 获取当前的亮度值
     */
    public int get100CurrentBrightness(Context context) {
        return (int) (100 * (getWindowBrightness(context) / 255.0f));
    }

    /**
     * 获取系统屏幕亮度
     *
     * @param context
     * @return 获取的值在0-255范围
     */
    public int getSystemBrightness(Context context) {
        try{
            ContentResolver contentResolver = context.getContentResolver();
            return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30);
        }catch (Exception e){

        }
        return 0;
    }

}
