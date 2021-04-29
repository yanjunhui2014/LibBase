package com.milo.libbase.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {
    private static String beforeMsg  = "";
    private static long   beforeTime = 0;

    public static void show(Context context, String message, int duration) {
        if (message == null) {
            return;
        }

        if (context == null) {
            return;
        }

        if (message.equals(beforeMsg) && (System.currentTimeMillis() - beforeTime) < 2000) {
            return;
        }

        beforeMsg = message;
        beforeTime = System.currentTimeMillis();

        try {
            Toast.makeText(context, message, duration).show();
        } catch (Exception e) {
            Looper.prepare();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getString(resId), duration);
    }

    public static void show(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId) {
        if (context == null) {
            return;
        }
        show(context, context.getString(resId));
    }

    public static void showNotEmtpy(Context context, int strResId) {
        showNotEmtpy(context, context.getResources().getString(strResId));
    }

    /**
     * 快捷显示不可为空Toast
     *
     * @param context
     * @param message
     */
    public static void showNotEmtpy(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            message = "数据";
        }
        show(context, message + "不可为空", Toast.LENGTH_SHORT);
    }


}
