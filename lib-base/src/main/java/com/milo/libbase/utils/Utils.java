package com.milo.libbase.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.milo.libbase.utils.filefactory.FileFactory;
import com.milo.libbase.utils.filefactory.FileType;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Locale;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 4/29/21
 */
public final class Utils {

    private static final char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static StringBuilder mFormatBuilder = new StringBuilder();
    private static Formatter     mFormatter;

    public static String getFileName(String urlOrName) {
        if (TextUtils.isEmpty(urlOrName)) {
            return null;
        } else {
            String auditValue = urlOrName;
            try {
                final String encodedPath = Uri.parse(urlOrName).getEncodedPath();

                if (encodedPath != null) {
                    int pathIndex = urlOrName.indexOf(encodedPath);
                    if (pathIndex + encodedPath.length() < urlOrName.length()) {
                        String endUrl = urlOrName.substring(pathIndex + encodedPath.length());
                        //替换调encodePath之后的"/"
                        if (endUrl.contains("/")) {
                            endUrl = endUrl.replaceAll("/", "&");
                        }
                        auditValue = encodedPath + endUrl;
                    } else {
                        auditValue = encodedPath;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (TextUtils.isEmpty(auditValue)) {
                auditValue = urlOrName;
            }
            int pos = auditValue.lastIndexOf("/");
            return pos == -1 ? auditValue : auditValue.substring(pos + 1);
        }
    }

    public static String getMD5(String source) throws NoSuchAlgorithmException {
        byte bytes[] = digest(source.getBytes(), "MD5");
        return toHexString(bytes);
    }

    private static byte[] digest(byte srcBytes[], String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(srcBytes);
        byte digestBytes[] = digest.digest();
        return digestBytes;
    }

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
            sb.append(HEXCHAR[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public static void flush(Flushable flushable) {
        try {
            if (flushable != null) {
                flushable.flush();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    /**
     * 根据毫秒格式化时间
     *
     * @param timeMs
     * @return
     */
    public static String millsToString(long timeMs) {
        int totalSeconds = (int) (timeMs / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return getDefaultFormatter().format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return getDefaultFormatter().format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private static Formatter getDefaultFormatter() {
        if (mFormatter == null)
            mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        return mFormatter;
    }

    public static String getProxyUrl(Context context, String url) {
        return getProxy(context).getProxyUrl(url);
    }

    /**
     * 视频代理地址，全局通用
     *
     * @param context
     * @return
     */
    public static HttpProxyCacheServer getProxy(Context context) {
        return newProxy(context, FileFactory.COMMON.getFileDirectory(FileType.VIDEO, "VideoProxy"));
    }

    private static HttpProxyCacheServer newProxy(Context context, String rootDir) {
        final File file = new File(rootDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        //获取缓存大小
        long defalutSize = 1000 * 1024 * 1024;

        return new HttpProxyCacheServer.Builder(context)
                .cacheDirectory(file)
                .maxCacheSize(defalutSize)
                .fileNameGenerator(new FileNameGenerator() {
                    @Override
                    public String generate(String url) {
                        String fileName = Utils.toMd5(url);
                        if (TextUtils.isEmpty(fileName)) {
                            fileName = getFileName(url);
                        }
                        return fileName;
                    }
                }).build();
    }

    public static String toMd5(String text) {
        if (!TextUtils.isEmpty(text)) {
            byte[] hash;
            try {
                hash = MessageDigest.getInstance("MD5").digest(text.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Huh, MD5 should be supported?", e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Huh, UTF-8 should be supported?", e);
            }

            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10)
                    hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } else {
            return "";
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
