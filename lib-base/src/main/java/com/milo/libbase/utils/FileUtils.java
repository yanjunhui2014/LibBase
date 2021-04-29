package com.milo.libbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 4/29/21
 */
public final class FileUtils {

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

    public static boolean isExist(String filePath) {
        return !(filePath == null || filePath.isEmpty()) && new File(filePath).exists();
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
