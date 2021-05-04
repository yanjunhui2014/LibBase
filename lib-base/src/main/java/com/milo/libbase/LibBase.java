package com.milo.libbase;

import android.app.Application;
import android.text.TextUtils;

import com.milo.libbase.utils.LogUtils;
import com.milo.libbase.utils.filefactory.FileFactory;
import com.milo.libbase.utils.filefactory.FileType;
import com.milo.libbase.widget.videoview.DefinitionPopupWindow;
import com.milo.libbase.widget.videoview.VideoDefinition;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 5/4/21
 */
public class LibBase {

    private String              fileFactoryName;
    private LogUtils.LogOptions logOptions;

    private DefinitionPopupWindow.DefinitionPermission definitionPermission;

    public String getFileFactoryName() {
        return fileFactoryName;
    }

    public LogUtils.LogOptions getLogOptions() {
        return logOptions;
    }

    public DefinitionPopupWindow.DefinitionPermission getDefinitionPermission() {
        return definitionPermission;
    }

    public static final class LibBaseBuilder {
        private String              fileFactoryName;
        private LogUtils.LogOptions logOptions;

        private DefinitionPopupWindow.DefinitionPermission definitionPermission;

        public LibBaseBuilder() {
        }

        public LibBaseBuilder withFileFactoryName(String fileFactoryName) {
            this.fileFactoryName = fileFactoryName;
            return this;
        }

        public LibBaseBuilder withLogOptions(LogUtils.LogOptions logOptions) {
            this.logOptions = logOptions;
            return this;
        }

        public LibBaseBuilder withDefinitionPermission(DefinitionPopupWindow.DefinitionPermission definitionPermission) {
            this.definitionPermission = definitionPermission;
            return this;
        }

        public LibBase build(Application application) {
            AppConfig.app = application;
            LibBase libBase = new LibBase();

            if (TextUtils.isEmpty(this.fileFactoryName)) {
                this.fileFactoryName = "libBase";
            } else {
                libBase.fileFactoryName = this.fileFactoryName;
            }
            FileFactory.init(application, this.fileFactoryName);


            if (this.logOptions == null) {
                this.logOptions = new LogUtils.LogOptions.Builder()
                        .setLoggable(true)
                        .setShowThreadInfo(false)
                        .setMethodCount(0)
                        .setTag("libBase")
                        .setLogPath(FileFactory.COMMON.getFileDirectory(FileType.LOG))
                        .setLogCachePath(FileFactory.COMMON.getFileDirectory(FileType.LOG) + "/cache")
                        .setEncryptIV16(new byte[]{'q'})
                        .setEncryptKey16(new byte[]{'q'})
                        .build();
            } else {
                libBase.logOptions = this.logOptions;
            }
            LogUtils.init(logOptions);

            if (this.definitionPermission == null) {
                this.definitionPermission = new DefinitionPopupWindow.DefinitionPermission() {
                    @Override
                    public boolean hasPermission(VideoDefinition videoDefinition) {
                        return true;
                    }

                    @Override
                    public void requestPermission(Object videoView, VideoDefinition videoDefinition) {

                    }
                };
            } else {
                libBase.definitionPermission = this.definitionPermission;
            }

            if (AppConfig.libBase != null) {
                throw new RuntimeException("AppConfig.libBase 已经有值了，不允许重复赋值");
            }

            AppConfig.libBase = libBase;
            return libBase;
        }
    }

}
