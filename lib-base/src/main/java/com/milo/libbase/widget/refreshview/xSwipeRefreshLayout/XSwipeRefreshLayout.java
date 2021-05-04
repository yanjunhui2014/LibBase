package com.milo.libbase.widget.refreshview.xSwipeRefreshLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.milo.libbase.widget.refreshview.base.RefreshLayoutInterface;

public class XSwipeRefreshLayout extends SwipeRefreshLayout implements RefreshLayoutInterface {

    public XSwipeRefreshLayout(Context context) {
        super(context);
    }

    public XSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnRefreshListener(final RefreshLayoutInterface.OnRefreshListener onRefreshListener) {
        this.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshListener != null) {
                    onRefreshListener.onRefresh();
                }
            }
        });
    }

    @Override
    public void setRefreshEnable(boolean isEnable) {
        setEnabled(isEnable);
    }

    @Override
    public ViewGroup getSelf() {
        return this;
    }
}
