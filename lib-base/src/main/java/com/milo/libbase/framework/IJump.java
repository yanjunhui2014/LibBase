package com.milo.libbase.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.milo.libbase.rxactivity.ActivityOnResult;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 标题：跳转接口
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/8/8 15:58
 */
public interface IJump<T> {

    T withString(String key, String value);

    T withInt(String key, int value);

    T withBoolean(String key, boolean value);

    T withLong(String key, long value);

    T withSerializable(String key, Serializable value);

    T withParcelableArrayList(String key, ArrayList<? extends Parcelable> arrayList);

    T withStringArrayList(String key, ArrayList<String> arrayList);

    T withIntegerArrayList(String key, ArrayList<Integer> arrayList);

    T toastWhenNull(Context context);

    /**
     * 为Intent设置Flag
     *
     * @param flag
     * @return
     */
    T addFlag(int flag);

    void setIntent(Intent intent);

    Intent getIntent();

    /**
     * 执行跳转
     */
    void navigation();

    @Deprecated
    void navigation(@NonNull Activity activity, int requestCode);

    void navigation(@NonNull AppCompatActivity activity, int requestCode, ActivityOnResult.Callback callback);

}
