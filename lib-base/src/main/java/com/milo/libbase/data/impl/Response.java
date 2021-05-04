package com.milo.libbase.data.impl;

import java.io.Serializable;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 5/4/21
 */
public interface Response<T> extends Serializable {

     int STATUS_SUCCESS  = 1;
     int STATUS_FAIL     = 0;
     int STATUS_403      = 403;
     int STATUS_OFFLINE  = 404;

     int getStatus();

     String getMsg();

    public T getData();
}
