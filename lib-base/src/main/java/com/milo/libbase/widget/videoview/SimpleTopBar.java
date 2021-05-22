package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
public class SimpleTopBar implements TopBar {

    private Context mContext;

    public View     mTopBarView;
    public View     mImgBack;
    public TextView mTvTitle;
    public View     mImgMenu;

    public SimpleTopBar(Context context) {
        this.mContext = context;

        init();
    }

    @Override
    public View getBackView() {
        return mImgBack;
    }

    @Override
    public TextView getTitleTextView() {
        return mTvTitle;
    }

    @Override
    public View getMenuView() {
        return mImgMenu;
    }

    @Override
    public View getTopBarView() {
        return mTopBarView;
    }

    protected void init() {
        mTopBarView = LayoutInflater.from(mContext).inflate(R.layout.libbase_vh_simple_top_bar, null);
        mImgBack = mTopBarView.findViewById(R.id.mImgBack);
        mTvTitle = mTopBarView.findViewById(R.id.mTvTitle);
        mImgMenu = mTopBarView.findViewById(R.id.mImgMenu);
    }

}
