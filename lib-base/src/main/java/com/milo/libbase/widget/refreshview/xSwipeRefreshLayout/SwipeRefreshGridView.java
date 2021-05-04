package com.milo.libbase.widget.refreshview.xSwipeRefreshLayout;

import android.content.Context;
import android.util.AttributeSet;

import com.milo.libbase.widget.refreshview.base.BaseRefreshGridView;
import com.milo.libbase.widget.refreshview.base.RefreshLayoutInterface;


public class SwipeRefreshGridView extends BaseRefreshGridView {

    private XSwipeRefreshLayout mXSwipeRefreshLayout;

    public SwipeRefreshGridView(Context context) {
        super(context);
    }

    public SwipeRefreshGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRefreshGridView(Context context, AttributeSet attrs, int defStyleAttr) {
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
