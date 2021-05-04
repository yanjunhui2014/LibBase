package com.milo.libbase.framework.mvp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.zhl.commonadapter.BaseViewHolder;

/**
 * 标题：少趣BaseViewHolder
 * 功能：兼容类
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/12/26 18:19
 */
public abstract class SimpleBaseViewHolder<T> extends BaseViewHolder<T> {
    protected String TAG = getClass().getSimpleName();

    /**
     * 自动bindView (mContext != null && getLayoutResId() != 0 下有效)
     */
    public void bindView() {
        if (mContext != null && getLayoutResId() != 0) {
            View view = LayoutInflater.from(mContext).inflate(getLayoutResId(), null, false);
            bindView(view);
        }
    }

    /**
     * 自动bindView (mContext != null && getLayoutResId() != 0 下有效)
     */
    public void bindView(@NonNull Context context) {
        this.mContext = context;
        if (getLayoutResId() != 0) {
            View view = LayoutInflater.from(context).inflate(getLayoutResId(), null, false);
            bindView(view);
        }
    }

    /**
     * 将ViewHolder添加到ViewGroup
     *
     * @param viewGroup
     */
    public void attachTo(ViewGroup viewGroup) {
        if (mItemView != null) {
            viewGroup.addView(mItemView);
        }
    }

    /**
     * 将ViewHolder添加到view
     *
     * @param view
     */
    public void attachTo(View view) {
        if (mItemView != null) {
            ((ViewGroup) view).addView(mItemView);
        }
    }

}
