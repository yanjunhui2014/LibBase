package com.milo.libbase.widget.refreshview;

/**
 * @author zhl 2020/7/2
 */
public enum LoadingState {

    LOADING,

    RELOADING,

    //加载对话框
    MODAL_LOADING,

    MODAL_DISMISS,

    ERROR,

    EMPTY,

    //列表中使用，显示没有更多
    NO_MORE,

    //列表中使用，可以加载更多
    HAVE_MORE,

    //加载完成显示内容
    SHOW_CONTENT;

    private String msg;

    public LoadingState setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getMsg() {
        return msg;
    }
}
