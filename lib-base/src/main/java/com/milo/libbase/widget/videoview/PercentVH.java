package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.annotation.IntRange;

import com.milo.libbase.R;
import com.milo.libbase.utils.Utils;

/**
 * 标题：
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/9/23 14:39
 */
public class PercentVH {

    private Context mContext;

    private View      mRootView;
    private ImageView mImageVolume;
    private ViewGroup mLayoutPercent;

    private View[]  mItemView = new View[10];
    private boolean mAttached = false;
    private int mType = Type.VOLUME;

    public PercentVH(Context context) {
        this.mContext = context;
        init();
    }

    @IntDef({Type.VOLUME, Type.BRIGHTNESS})
    public  @interface Type {
        int VOLUME     = 1;
        int BRIGHTNESS = 2;
    }

    private void init() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.libbase_volume_percent, null);
        mImageVolume = mRootView.findViewById(R.id.mImageVolume);
        mLayoutPercent = mRootView.findViewById(R.id.mLayoutPercent);

        for (int i = 0; i < mItemView.length; i++) {
            View childView = new View(mContext);
            childView.setBackgroundColor(Color.TRANSPARENT);

            mItemView[i] = childView;
            mLayoutPercent.addView(childView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childView.getLayoutParams();
            params.width = Utils.dp2px(mContext, 10);
            params.setMargins(Utils.dp2px(mContext, 1), 0, 0, 0);
        }
    }

    public void setType(@Type int type) {
        this.mType = type;
        switch (type) {
            case 1:
                setPercent(VolumeHelper.getInstance(mContext).get100CurrentVolume());
                break;
            case 2:
                mImageVolume.setBackgroundResource(R.mipmap.libbase_ic_brightness);
                setPercent(VolumeHelper.getInstance(mContext).get100CurrentVolume());
                break;
        }
    }

    public void setPercent(@IntRange(from = 0, to = 100) int percent) {
        int currentVolume = percent / 10;
        switch (mType) {
            case 1:
                mImageVolume.setBackgroundResource(currentVolume == 0 ? R.mipmap.libbase_ic_volume_mute : R.mipmap.libbase_ic_volume_normal);
                break;
        }

        for (int i = 0; i < mItemView.length; i++) {
            if (i < currentVolume) {
                mItemView[i].setBackgroundColor(Color.WHITE);
            } else {
                mItemView[i].setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public void attach(FrameLayout viewGroup) {
        if (!mAttached) {
            mAttached = true;
            viewGroup.addView(mRootView);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
            params.width = Utils.dp2px(mContext, 160);
            params.height = Utils.dp2px(mContext, 80);
            params.gravity = Gravity.CENTER;
        }
    }

    public void detach(ViewGroup viewGroup) {
        if (mAttached) {
            mAttached = false;
            viewGroup.removeView(mRootView);
        }
    }

}
