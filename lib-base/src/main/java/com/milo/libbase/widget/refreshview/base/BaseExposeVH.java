package com.milo.libbase.widget.refreshview.base;


import android.graphics.Rect;

import com.zhl.commonadapter.BaseViewHolder;

/**
 * @author zhl 2020/9/8
 */
public abstract class BaseExposeVH<D extends OnItemExposeListener.ExposeItem> extends BaseViewHolder<D> {

    private OnItemExposeListener mOnItemExposeListener;

    public BaseExposeVH(OnItemExposeListener onItemExposeListener) {
        mOnItemExposeListener = onItemExposeListener;
    }

    @Override
    public void updateView(D data, int position) {
        if (mOnItemExposeListener != null) {
            if (data.isFirst()) {
                data.setFirst(false);
                Rect rect = new Rect();

                boolean cover = mItemView.getGlobalVisibleRect(rect);
                boolean visibleHeightEnough = rect.height() > mItemView.getMeasuredHeight() * 0.8;
                boolean visibleWidthEnough = rect.width() > mItemView.getMeasuredWidth() * 0.8;
                boolean isItemViewVisibleInLogic = visibleHeightEnough || visibleWidthEnough;
                if (cover && isItemViewVisibleInLogic) {
                    mOnItemExposeListener.onItemViewVisible(true, position);
                }
            }
        }
    }
}
