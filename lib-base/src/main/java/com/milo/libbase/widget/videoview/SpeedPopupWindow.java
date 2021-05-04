package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.milo.libbase.R;
import com.milo.libbase.utils.Utils;

/**
 * 标题：速度选择弹出框
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/12/4 12:15
 */
public class SpeedPopupWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;

    private TextView mTvSpeed1;
    private TextView mTvSpeed2;
    private TextView mTvSpeed3;
    private TextView mTvSpeed4;
    private TextView mTextViews[];

    private SpeedWindowListener mSpeedWindowListener;
    private float[]             mSpeedValues;
    public  int                 mVideoHeightInPortrait;

    interface SpeedWindowListener {
        void onSpeedClick(float speed);
    }

    public SpeedPopupWindow(@NonNull Context context, SpeedWindowListener windowListener) {
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.mContext = context;
        this.mSpeedWindowListener = windowListener;
        init();
    }

    @Override
    public void onClick(View v) {
        if (v == mTvSpeed1 || v == mTvSpeed2 || v == mTvSpeed3 || v == mTvSpeed4) {
            String speedStr = ((TextView) v).getText().toString();
            mSpeedWindowListener.onSpeedClick(Float.valueOf(speedStr.replace("x", "")));
        }
        dismiss();
    }

    public void setSpeedValues(float[] speedValues) {
        if (speedValues.length == 0) {
            throw new IllegalArgumentException("错误的参数");
        }
        this.mSpeedValues = speedValues;

        for (int i = 0; i < speedValues.length && i < 4; i++) {
            mTextViews[i].setText(speedValues[i] + "x");
        }

        if (speedValues.length < 4) {
            switch (speedValues.length) {
                case 1:
                    mTvSpeed4.setVisibility(View.GONE);
                    mTvSpeed3.setVisibility(View.GONE);
                    mTvSpeed2.setVisibility(View.GONE);
                    break;
                case 2:
                    mTvSpeed4.setVisibility(View.GONE);
                    mTvSpeed3.setVisibility(View.GONE);
                    break;
                case 3:
                    mTvSpeed4.setVisibility(View.GONE);
                    break;
            }
        }
    }

    public void setScreenOrientation(int screenOrientation) {
        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setWidth(Utils.dp2px(mContext, 70));
            this.setHeight(mVideoHeightInPortrait);
        } else {
            this.setWidth(Utils.dp2px(mContext, 140));
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public void setCurrentSpeed(float speed) {

    }

    private void init() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.libbase_popup_speed, null);
        setContentView(contentView);
        mTvSpeed1 = contentView.findViewById(R.id.mTvSpeed1);
        mTvSpeed2 = contentView.findViewById(R.id.mTvSpeed2);
        mTvSpeed3 = contentView.findViewById(R.id.mTvSpeed3);
        mTvSpeed4 = contentView.findViewById(R.id.mTvSpeed4);
        mTextViews = new TextView[]{mTvSpeed1, mTvSpeed2, mTvSpeed3, mTvSpeed4};

        mTvSpeed1.setOnClickListener(this);
        mTvSpeed2.setOnClickListener(this);
        mTvSpeed3.setOnClickListener(this);
        mTvSpeed4.setOnClickListener(this);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
    }

}
