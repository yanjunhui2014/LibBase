package com.milo.libbase.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.milo.libbase.rxactivity.ActivityOnResult;
import com.milo.libbase.utils.LogUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 标题：源生跳转处理类
 * 功能：利用Android源生代码，处理Intent跳转
 * 备注：因为在不涉及到跨模块跳转的时候，源生跳转就足以满足业务需求，而且还省去了配置路由策略的步骤。
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/8/8 16:03
 */
public class OriginJump implements IJump<OriginJump> {

    private Context mContext;
    private Intent  mIntent;

    private long lastClickTime;

    private OriginJump() {
    }

    public OriginJump(Intent intent) {
        this.mIntent = intent;
    }

    public OriginJump(Context context, Intent intent) {
        this.mContext = context;
        this.mIntent = intent;
    }

    /**
     * class跳转(显式跳转)
     *
     * @param context
     * @param clz
     */
    public OriginJump(@NonNull Context context, @NonNull Class<? extends Activity> clz) {
        this.mContext = context;
        this.mIntent = new Intent(context, clz);
    }

    /**
     * action跳转(隐式跳转)
     *
     * @param context
     * @param action
     */
    public OriginJump(@NonNull Context context, @NonNull String action) {
        this.mContext = context;
        this.mIntent = new Intent(action);
    }

    @Override
    public OriginJump withString(String key, String value) {
        mIntent.putExtra(key, value);
        return this;
    }

    @Override
    public OriginJump withInt(String key, int value) {
        mIntent.putExtra(key, value);
        return this;
    }

    @Override
    public OriginJump withBoolean(String key, boolean value) {
        mIntent.putExtra(key, value);
        return this;
    }

    @Override
    public OriginJump withLong(String key, long value) {
        mIntent.putExtra(key, value);
        return this;
    }

    @Override
    public OriginJump withSerializable(String key, Serializable value) {
        mIntent.putExtra(key, value);
        return this;
    }

    @Override
    public OriginJump withParcelableArrayList(String key, ArrayList<? extends Parcelable> arrayList) {
        mIntent.putParcelableArrayListExtra(key, arrayList);
        return this;
    }

    @Override
    public OriginJump withStringArrayList(String key, ArrayList<String> arrayList) {
        mIntent.putStringArrayListExtra(key, arrayList);
        return this;
    }

    @Override
    public OriginJump withIntegerArrayList(String key, ArrayList<Integer> arrayList) {
        mIntent.putIntegerArrayListExtra(key, arrayList);
        return this;
    }

    @Override
    public OriginJump toastWhenNull(Context context) {
        if (mContext == null) {
            this.mContext = context;
        }
        return this;
    }

    @Override
    public OriginJump addFlag(int flag) {
        mIntent.setFlags(flag);
        return this;
    }

    @Override
    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    @Override
    public Intent getIntent() {
        return mIntent;
    }

    @Override
    public void navigation() {
        if (mIntent == null || mContext == null) {
            if (mContext != null) {
                Toast.makeText(mContext, "There is no mIntent matched", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder log = new StringBuilder("mContext is null");
                if (mIntent == null) {
                    log.append("/There is no mIntent matched");
                }
                LogUtils.e(log.toString());
            }
        } else if (!isFastDoubleClick()) {
            //确保主线程
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mContext.startActivity(mIntent);
                }
            });
        }
    }

    /**
     * 执行带回调的跳转
     *
     * @param activity
     * @param requestCode
     * @deprecated 建议使用 {@link #navigation(AppCompatActivity, int, ActivityOnResult.Callback)} 替换
     */
    @Deprecated
    @Override
    public void navigation(@NonNull Activity activity, int requestCode) {
        if (mIntent == null) {
            Toast.makeText(activity, "There is no mIntent matched", Toast.LENGTH_SHORT).show();
        } else if (!isFastDoubleClick()) {
            activity.startActivityForResult(mIntent, requestCode);
        }
    }

    /**
     * 执行带回调的跳转
     * 该方法中的回调脱离了 activity.onActivityResult 可单独使用，这样提高了代码的可读性和优雅度
     *
     * @param activity
     * @param requestCode
     * @param callback
     */
    public void navigation(@NonNull AppCompatActivity activity, int requestCode, ActivityOnResult.Callback callback) {
        if (mIntent == null) {
            Toast.makeText(activity, "There is no mIntent matched", Toast.LENGTH_SHORT).show();
        } else if (!isFastDoubleClick()) {
            ActivityOnResult activityOnResult = new ActivityOnResult(activity);
            activityOnResult.startForResult(mIntent, requestCode, callback);
        }
    }

    public OriginJump withJumpFrom(String value) {
        mIntent.putExtra("key_jump_from", value);
        return this;
    }

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return 0 < timeD && timeD < 1000;
    }

}
