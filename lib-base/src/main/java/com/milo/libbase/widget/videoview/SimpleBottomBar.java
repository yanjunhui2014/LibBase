package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.milo.libbase.R;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2021/3/26
 */
public class SimpleBottomBar implements BottomBar {

    private Context mContext;

    public View      mBottomView;
    public ImageView mPlayView;
    public TextView  mTvCurTime;
    public SeekBar   mSeekBar;
    public TextView  mTvDefinition;
    public TextView  mTvSpeed;
    public TextView  mTvTotalTime;
    public ImageView      mWholeScreen;

    public int[] mPlayViewResIds = new int[]{R.mipmap.libbase_ic_play, R.mipmap.libbase_ic_suspend};
    public int[] mWholeScreenResIds = new int[]{R.mipmap.libbase_ic_full, R.mipmap.libbase_ic_shrink};

    public SimpleBottomBar(Context context) {
        this.mContext = context;

        init();
    }

    @Override
    public View getBottomBarView() {
        return mBottomView;
    }

    @Override
    public ImageView getPlayView() {
        return mPlayView;
    }

    @Override
    public TextView getCurTimeTextView() {
        return mTvCurTime;
    }

    @Override
    public SeekBar getSeekBar() {
        return mSeekBar;
    }

    @Override
    public TextView getDefinitionTextView() {
        return mTvDefinition;
    }

    @Override
    public TextView getSpeedTextView() {
        return mTvSpeed;
    }

    @Override
    public TextView getTotalTimeTextView() {
        return mTvTotalTime;
    }

    @Override
    public ImageView getWholeScreenView() {
        return mWholeScreen;
    }

    @Override
    public int[] getPlayViewResIds() {
        return mPlayViewResIds;
    }

    @Override
    public int[] getWholeScreenResIds() {
        return mWholeScreenResIds;
    }

    protected void init() {
        mBottomView = LayoutInflater.from(mContext).inflate(R.layout.libbase_vh_simple_bottom_bar, null);
        mPlayView = mBottomView.findViewById(R.id.video_play_control);
        mTvCurTime = mBottomView.findViewById(R.id.video_played_time);
        mSeekBar = mBottomView.findViewById(R.id.video_hSeekBar);
        mTvDefinition = mBottomView.findViewById(R.id.video_definition);
        mTvTotalTime = mBottomView.findViewById(R.id.video_total_time);
        mWholeScreen = mBottomView.findViewById(R.id.video_whole_screen);
        mTvSpeed = mBottomView.findViewById(R.id.video_speed);
    }

}
