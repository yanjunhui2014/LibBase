package com.milo.libbase.widget.refreshview.xSwipeRefreshLayout;

import android.content.Context;
import android.util.AttributeSet;

import com.milo.libbase.widget.refreshview.base.BaseRefreshRecyclerView;
import com.milo.libbase.widget.refreshview.base.RefreshLayoutInterface;

public class SwipeRefreshRecyclerView extends BaseRefreshRecyclerView {

    private XSwipeRefreshLayout mXSwipeRefreshLayout;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
