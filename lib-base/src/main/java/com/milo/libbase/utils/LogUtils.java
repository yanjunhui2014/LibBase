package com.milo.libbase.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 4/29/21
 */
public final class LogUtils {

    private static String TAG_PREFIX_LOGAN = "LOGAN-";
    private static Gson   sGson;

    public static void init(final LogOptions logOptions) {
        sGson = new Gson();
        FormatStrategy logcatFormatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(logOptions.tag)
                .showThreadInfo(logOptions.isShowThreadInfo)
                .methodCount(logOptions.methodCount)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(logcatFormatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return logOptions.isLoggable;
            }
        });

        Logger.addLogAdapter(new LogAdapter() {

            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return tag != null && tag.startsWith(TAG_PREFIX_LOGAN);
            }

            @Override
            public void log(int priority, @Nullable String tag, @NonNull String message) {
                //                write(CodeLog.obtain(priority - 2, tag, message));
            }
        });

        //        LoganConfig config = new LoganConfig.Builder()
        //                .setCachePath(logOptions.logCachePath)
        //                .setPath(logOptions.logPath)
        //                .setEncryptKey16(logOptions.encryptKey16)
        //                .setEncryptIV16(logOptions.encryptIV16)
        //                .build();
        //        Logan.init(config);
        //        Logan.setDebug(logOptions.isLoggable);
        //        Logan.setOnLoganProtocolStatus(new OnLoganProtocolStatus() {
        //            @Override
        //            public void loganProtocolStatus(String cmd, int code) {
        //                FZLogger.i("clogan > cmd : " + cmd + " | " + "code : " + code);
        //            }
        //        });
    }

    public static String getLoganTag(String tag) {
        return TAG_PREFIX_LOGAN + tag;
    }

    public static void d(String msg) {
        if (msg != null) {
            Logger.d(msg);
        }
    }

    public static void d(String tag, String msg) {
        if (msg != null) {
            Logger.t(tag).d(msg);
        }
    }

    public static void e(String msg) {
        if (msg != null) {
            Logger.e(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (msg != null) {
            Logger.t(tag).e(msg);
        }
    }

    public static void w(String msg) {
        if (msg != null) {
            Logger.w(msg);
        }
    }

    public static void w(String tag, String msg) {
        if (msg != null) {
            Logger.t(tag).w(msg);
        }
    }

    public static void i(String msg) {
        if (msg != null) {
            Logger.i(msg);
        }
    }

    public static void i(String tag, String msg) {
        if (msg != null) {
            Logger.t(tag).i(msg);
        }
    }

    public static void v(String msg) {
        if (msg != null) {
            Logger.v(msg);
        }
    }

    public static void v(String tag, String msg) {
        if (msg != null) {
            Logger.t(tag).v(msg);
        }
    }

    public static void json(String msg) {
        if (msg != null) {
            Logger.json(msg);
        }
    }

    public static void json(String tag, String msg) {
        if (msg != null) {
            Logger.t(tag).json(msg);
        }
    }

    public static void xml(String msg) {
        if (msg != null) {
            Logger.xml(msg);
        }
    }

    public static void xml(String tag, String msg) {
        if (msg != null) {
            Logger.t(tag).xml(msg);
        }
    }

    //    public static void write(BusinessLog log) {
    //        if (log != null) {
    //            Logan.w(sGson.toJson(log), log.getType());
    //            log.recycle();
    //        }
    //    }
    //
    //    public static void flush() {
    //        Logan.f();
    //    }
    //
    //    public static void upload(String[] dates, UploadLogRunnable uploadLogRunnable) {
    //        Logan.s(dates, new RealUploadRunnable(uploadLogRunnable));
    //    }
    //
    //    public static Map<String, Long> getAllFilesInfo() {
    //        return Logan.getAllFilesInfo();
    //    }

    public static final class LogOptions {
        String  tag;
        String  logCachePath;
        String  logPath;
        byte[]  encryptKey16;
        byte[]  encryptIV16;
        boolean isShowThreadInfo;
        boolean isLoggable;
        int     methodCount;

        private LogOptions() {

        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void setShowThreadInfo(boolean showThreadInfo) {
            isShowThreadInfo = showThreadInfo;
        }

        public void setLoggable(boolean loggable) {
            isLoggable = loggable;
        }

        public void setMethodCount(int methodCount) {
            this.methodCount = methodCount;
        }

        public void setLogCachePath(String logCachePath) {
            this.logCachePath = logCachePath;
        }

        public void setLogPath(String logPath) {
            this.logPath = logPath;
        }

        public void setEncryptKey16(byte[] encryptKey16) {
            this.encryptKey16 = encryptKey16;
        }

        public void setEncryptIV16(byte[] encryptIV16) {
            this.encryptIV16 = encryptIV16;
        }

        public static final class Builder {

            String  tag = "logger";
            String  logCachePath;
            String  logPath;
            byte[]  encryptKey16;
            byte[]  encryptIV16;
            boolean isShowThreadInfo;
            boolean isLoggable  = true;
            int     methodCount = 1;

            public Builder setTag(String tag) {
                this.tag = tag;
                return this;
            }

            public Builder setShowThreadInfo(boolean showThreadInfo) {
                isShowThreadInfo = showThreadInfo;
                return this;
            }

            public Builder setLoggable(boolean loggable) {
                isLoggable = loggable;
                return this;
            }

            public Builder setMethodCount(int methodCount) {
                this.methodCount = methodCount;
                return this;
            }

            public Builder setLogCachePath(String logCachePath) {
                this.logCachePath = logCachePath;
                return this;
            }

            public Builder setLogPath(String logPath) {
                this.logPath = logPath;
                return this;
            }

            public Builder setEncryptKey16(byte[] encryptKey16) {
                this.encryptKey16 = encryptKey16;
                return this;
            }

            public Builder setEncryptIV16(byte[] encryptIV16) {
                this.encryptIV16 = encryptIV16;
                return this;
            }

            public LogOptions build() {
                LogOptions logOptions = new LogOptions();
                logOptions.setTag(tag);
                logOptions.setLoggable(isLoggable);
                logOptions.setShowThreadInfo(isShowThreadInfo);
                logOptions.setMethodCount(methodCount);
                logOptions.setLogCachePath(logCachePath);
                logOptions.setLogPath(logPath);
                logOptions.setEncryptKey16(encryptKey16);
                logOptions.setEncryptIV16(encryptIV16);
                return logOptions;
            }
        }
    }

}
