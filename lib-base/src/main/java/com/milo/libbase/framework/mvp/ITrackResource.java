package com.milo.libbase.framework.mvp;

/**
 * 标题：统计数据源接口
 * 功能：
 * 备注：项目业务中，常常需要添加页面来源，实现此接口，可以很好的规范这一点。
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/11/9 11:12
 */
public interface ITrackResource {

    String UNKNOW_JUMP_FROM = "未知"; //未知数据
    String KEY_JUMP_FROM = "key_jump_from";

    /**
     * 跟踪页面名称
     *
     * @return
     */
    String getTrackName();

    /**
     * 获取跳转来源
     *
     * @return
     */
    String getJumpFrom();

}
