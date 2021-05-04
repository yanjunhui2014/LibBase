package com.milo.libbase.business;

import com.milo.libbase.data.impl.PrepareRes;

/**
 * Title：资源抽象工厂
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/11/25
 */
public abstract class ResPrepareFactory<T> {

    public abstract PrepareRes<T> createRes(String downloadUrl, String savePath);

}
