package com.milo.libbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.milo.libbase.utils.filefactory.FileFactory;
import com.milo.libbase.utils.filefactory.FileType;
import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
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

    private static long mLastClickTime;

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

    public static void writeCommonLog(String tag, String msg) {
        //        LogUtils.write(CodeLog.obtain(Logger.INFO - 2, tag, sdf2.format(new Date()) + ":" + msg));
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        mLastClickTime = time;
        return 0 < timeD && timeD < 1000;
    }

    public static boolean isFastDoubleClick(long interval) {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        mLastClickTime = time;
        return 0 < timeD && timeD < interval;
    }

    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isExist(String filePath) {
        return filePath != null && !filePath.isEmpty() && (new File(filePath)).exists();
    }

    public static boolean copyFile(String originalPath, String newPath) {
        if (originalPath == null || "".equals(originalPath) || newPath == null || "".equals(newPath)) {
            return false;
        }
        File originalFile = new File(originalPath);
        if (!originalFile.exists()) {
            return false;
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(originalFile);
            return copyFile(inputStream, newPath);
        } catch (FileNotFoundException e) {
            Utils.close(inputStream);
            return false;
        } finally {
            Utils.close(inputStream);
        }
    }

    public static boolean copyFile(InputStream inputStream, String newPath) {
        if (inputStream == null || newPath == null || "".equals(newPath)) {
            return false;
        }
        File newFile = new File(newPath);
        OutputStream out = null;
        BufferedOutputStream bout = null;
        try {
            if (!newFile.exists()) {
                newFile.getParentFile().mkdirs();
                newFile.createNewFile();
            }
            out = new FileOutputStream(newFile);
            bout = new BufferedOutputStream(out);
            int read = -1;
            byte[] data = new byte[1024];
            while ((read = inputStream.read(data)) != -1) {
                bout.write(data, 0, read);
            }
            bout.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.close(bout);
            Utils.close(out);
            Utils.close(inputStream);
        }
        return false;
    }

    public static boolean write(String filePath, byte[] data, boolean append) {
        File file = null;
        OutputStream outputStream = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file, append);
            outputStream.write(data);
            outputStream.flush();
            return true;
        } catch (Exception e) {
            if (file != null)
                file.delete();
        } finally {
            Utils.close(outputStream);
        }
        return false;
    }

    public static boolean write(String filePath, InputStream inputStream, boolean append) {
        File file = null;
        OutputStream outputStream = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            byte[] buf = new byte[20 * 1024];
            int ch = -1;
            while ((ch = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, ch);
                outputStream.flush();
            }
            return true;
        } catch (Exception e) {
            if (file != null)
                file.delete();
        } finally {
            Utils.close(outputStream);
            Utils.close(inputStream);
        }
        return false;
    }

    public static boolean delDirFiles(String path) {
        if (path == null || "".equals(path)) {
            return false;
        }
        try {
            File dir = new File(path);
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean delFileOrDir(String path) {
        if (path == null || "".equals(path)) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else {
                try {
                    deleteDirectory(file);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static long getAvailableSDMemorySize(String path) {
        try {
            StatFs stat = new StatFs(path);
            long blockSize = 0;
            long availableBlocks = 0;
            if (Build.VERSION.SDK_INT < 18) {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks();
            } else {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong();
            }
            return availableBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static long getDirFilesSize(File f) throws Exception// 取得文件夹大小
    {
        if (f == null || !f.exists()) {
            return 0;
        }
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isFile()) {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static boolean mkdir(String dir) {
        boolean ret = false;
        if (dir != null && !dir.isEmpty()) {
            File destDir = new File(dir);
            if (!destDir.exists()) {
                ret = destDir.mkdirs();
            } else {
                ret = destDir.isDirectory();
            }
        }

        return ret;
    }

    public static String read(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toString();
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!org.apache.commons.io.FileUtils.isSymlink(directory)) {
            org.apache.commons.io.FileUtils.cleanDirectory(directory);
        }

        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    public static boolean rename(String oldPath, String newPath) {
        return rename(oldPath, newPath, true);
    }

    public static boolean rename(String oldPath, String newPath, boolean isForce) {
        if (TextUtils.isEmpty(oldPath) || TextUtils.isEmpty(newPath)) {
            return false;
        }

        File oldFile = new File(oldPath);
        File newFile = new File(newPath);

        if (!oldFile.exists()) {
            if (newFile.exists()) {
                //老文件不存在是，新文件存在返回重命名成功
                return true;
            }
            return false;
        }

        if (newFile.exists()) {
            if (!isForce) {
                return false;
            } else {
                boolean delete = newFile.delete();
                if (!delete) {
                    return false;
                }
            }
        }

        return oldFile.renameTo(newFile);
    }

}
