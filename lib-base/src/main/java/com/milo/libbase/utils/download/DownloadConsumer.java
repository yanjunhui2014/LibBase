package com.milo.libbase.utils.download;

/**
 * Title：下载消费者
 * Describe：
 * Remark：消费者拥有取消消费的能力
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public interface DownloadConsumer {

    int STATUS_INQUEUE = 0;//队列中
    int STATUS_CONNECT = 1;//连接中
    int STATUS_DOWN_ING = 2;//下载中
    int STATUS_DOWN_ERROR = 3;//下载错误
    int STATUS_DOWN_DONE = 4;//下载完成
    int STATUS_DOWN_CANCEL = -1;//下载取消

    void cancel();//取消消费

    int getStatus();//消费状态

    String getTaskId();

}
