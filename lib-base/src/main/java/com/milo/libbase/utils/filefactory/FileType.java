package com.milo.libbase.utils.filefactory;

import androidx.annotation.StringDef;

/**
 * Title：文件类型
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/10/27
 */
@StringDef({FileType.UNSPECIFY, FileType.IMAGE, FileType.TXT, FileType.VIDEO, FileType.AUDIO, FileType.APK, FileType.SRT, FileType.LOG})
public @interface FileType {

    String UNSPECIFY = "unspecify";
    String IMAGE = "image";
    String TXT = "txt";
    String VIDEO = "video";
    String AUDIO = "audio";
    String APK = "apk";
    String SRT = "srt";
    String LOG = "log";

}
