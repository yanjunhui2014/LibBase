package com.milo.libbase.widget.refreshview.xSwipeRefreshLayout;

import android.content.Context;
import android.util.AttributeSet;

import com.milo.libbase.widget.refreshview.base.BaseRefreshListView;
import com.milo.libbase.widget.refreshview.base.RefreshLayoutInterface;

public class SwipeRefreshListView extends BaseRefreshListView {

    private XSwipeRefreshLayout mXSwipeRefreshLayout;

    public SwipeRefreshListView(Context context) {
        super(context);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected RefreshLayoutInterface createRefreshLayout() {
        mXSwipeRefreshLayout = new XSwipeRefreshLayout(getContext());
        return mXSwipeRefreshLayout;
    }

    public XSwipeRefreshLayout getXSwipeRefreshLayout() {
        return mXSwipeRefreshLayout;
    }


}
