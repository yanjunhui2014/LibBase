package com.milo.libbase.utils.filefactory;

import android.app.Application;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.milo.libbase.utils.FileUtils;
import com.milo.libbase.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Title：文件工厂
 * Describe：
 * Remark：适配android19以上私有存储，默认分配私有存储，也可以通过指定Storage的形式得到共有存储。
 * 私有存储：可被系统清理，可通过系统、app的清理缓存清理
 * 共有存储：不会随着手机卸载而卸载，并且图片、视频都会被自动扫描出现在用户相册
 *
 * 未来优化方向：
 * 1、目前是通过url后缀直接对文件类型判断，难免会有不准确的现象，未来可以通过增加魔数判断，提高文件类型判断准确率。
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/10/27
 */
public class FileFactory extends AbsFileFactory {

    private static Application mApplication;
    private static String mRootPath;

    public static PathProvider COMMON;//通用私有存储
    public static PathProvider COMMON_STORAGE;//通用共有存储
    public static PathProvider TEMP;//临时私有存储
    public static PathProvider TEMP_STORAGE;//临时共有存储

    public static void init(Application application, String rootPath) {
        mApplication = application;
        mRootPath = rootPath;

        FileFactory fileFactory = new FileFactory();
        COMMON = fileFactory.createPathProvider(StorageType.External, PathParentType.COMMON);
        COMMON_STORAGE = fileFactory.createPathProvider(StorageType.Storage, PathParentType.COMMON);

        TEMP = fileFactory.createPathProvider(StorageType.External, PathParentType.TEMP);
        TEMP_STORAGE = fileFactory.createPathProvider(StorageType.Storage, PathParentType.TEMP);
    }

    @Override
    public Set<String> getAllCacheList() {
        Set<String> list = new HashSet<>();
        list.addAll(COMMON.getCacheDirectory());
        list.addAll(TEMP.getCacheDirectory());
        return list;
    }

    @Override
    public PathProvider createPathProvider(final int storageType, @PathParentType final String parentType) {
        return new PathProvider() {
            @Override
            public int getStorageType() {
                if (Build.VERSION.SDK_INT < 19) {
                    return StorageType.Storage;
                }
                return storageType;
            }

            @Override
            public String getPathParentType() {
                return parentType;
            }

            @Override
            public String getFilePath(String urlOrName) {
                return getFilePath(null, urlOrName, false);
            }

            @Override
            public String getFilePath(String urlOrName, boolean keepSuffix) {
                return getFilePath(null, urlOrName, keepSuffix);
            }

            @Override
            public String getFilePath(String subDirectory, String urlOrName) {
                return getFilePath(subDirectory, urlOrName, false);
            }

            @Override
            public String getFilePath(String subDirectory, String urlOrName, boolean keepSuffix) {
                String fileName = null;
                if (TextUtils.isEmpty(urlOrName)) {
                    //即使参数为空，也会返回一个制造出来的路径
                    return getFilePathByMake(FileType.UNSPECIFY, subDirectory, false, null);
                } else {
                    fileName = Utils.getFileName(urlOrName);
                }

                String fileType = FileType.UNSPECIFY;
                if (!TextUtils.isEmpty(urlOrName)) {
                    if (urlOrName.endsWith("mp4")) {
                        fileType = FileType.VIDEO;
                    } else if (urlOrName.endsWith("jpg") || urlOrName.endsWith("png")) {
                        fileType = FileType.IMAGE;
                    } else if (urlOrName.endsWith("aac") || urlOrName.endsWith("mp3") || urlOrName.endsWith("wav")) {
                        fileType = FileType.AUDIO;
                    } else if (urlOrName.endsWith(".apk")) {
                        fileType = FileType.APK;
                    } else if (urlOrName.endsWith(".txt")) {
                        fileType = FileType.TXT;
                    } else if (urlOrName.endsWith(".srt")) {
                        fileType = FileType.SRT;
                    }
                }

                if (!keepSuffix) {
                    //不保留后缀
                    if (!TextUtils.isEmpty(fileName)) {
                        if (fileName.endsWith(".mp4") || fileName.endsWith(".jpg") || fileName.endsWith(".png")
                                || fileName.endsWith(".aac") || urlOrName.endsWith(".mp3") || urlOrName.endsWith(".wav") || urlOrName.endsWith(".gif")) {
                            fileName = fileName.substring(0, fileName.lastIndexOf("."));
                        }
                    }
                }

                return getFilePath(fileType, subDirectory, fileName);
            }

            @Override
            public String getFilePathByMake() {
                return getFilePathByMake(FileType.UNSPECIFY, false);
            }

            @Override
            public String getFilePathByMake(@FileType String fileType, boolean showSuffix) {
                return getFilePathByMake(fileType, showSuffix, null);
            }

            @Override
            public String getFilePathByMake(@FileType String fileType, boolean showSuffix, String specifySuffix) {
                return getFilePathByMake(fileType, null, showSuffix, specifySuffix);
            }

            @Override
            public String getFilePathByMake(@FileType String fileType, String subDirectory, boolean showSuffix, String specifySuffix) {
                String suffix = "";
                if (showSuffix) {
                    switch (fileType) {
                        case FileType.APK:
                            suffix = ".apk";
                            break;
                        case FileType.TXT:
                            suffix = ".txt";
                            break;
                        case FileType.IMAGE:
                            suffix = ".png";
                            break;
                        case FileType.VIDEO:
                            suffix = ".mp4";
                            break;
                        case FileType.SRT:
                            suffix = ".srt";
                            break;
                        case FileType.AUDIO:
                            if (TextUtils.isEmpty(specifySuffix)) {
                                throw new IllegalArgumentException("无法为 FileType.AUDIO 自动创建后缀名, 请使用getFilePathByMake(String fileType, boolean showSuffix, String specify) 替代");
                            } else if (".aac".equals(specifySuffix) || ".mp3".equals(specifySuffix) || ".wav".equals(specifySuffix)) {
                                suffix = specifySuffix;
                            } else {
                                throw new IllegalArgumentException("audio必须以mp3/aac/wav结尾");
                            }
                        case FileType.UNSPECIFY:
                            if (TextUtils.isEmpty(specifySuffix)) {
                                throw new IllegalArgumentException("无法为 FileType.UNSPECIFY 指定后缀名");
                            }
                    }
                }
                return getFilePath(fileType, subDirectory, String.format("make_%s%s", System.currentTimeMillis(), suffix));
            }

            @Override
            public String getFilePath(@FileType String fileType, @Nullable String subDirectory, @NonNull String fileName) {
                if (mApplication == null) {
                    throw new NullPointerException("mApplication不得为空,是否尚未初始化");
                }
                if (mRootPath == null) {
                    throw new NullPointerException("mRootPath不得为空,是否尚未初始化");
                }
                if (TextUtils.isEmpty(fileName)) {
                    throw new NullPointerException("fileName不得为空");
                }

                StringBuilder builder = new StringBuilder();

                switch (getStorageType()) {
                    case StorageType.External:
                        builder.append(mApplication.getExternalFilesDir(parentType).getAbsolutePath());
                        break;
                    case StorageType.Storage:
                        builder.append(Environment.getExternalStorageDirectory());
                        builder.append("/");
                        builder.append(mRootPath);
                        builder.append("/");
                        builder.append(parentType);
                        break;
                }

                builder.append("/");
                builder.append(fileType);
                if (!TextUtils.isEmpty(subDirectory)) {
                    builder.append("/");
                    builder.append(subDirectory);
                }

                //创建用到的文件夹
                FileUtils.mkdir(builder.toString());

                builder.append("/");
                builder.append(fileName);

                return builder.toString();
            }

            @Override
            public String getFileDirectory(@FileType String fileType) {
                return getFileDirectory(fileType, null);
            }

            @Override
            public String getFileDirectory(@FileType  String fileType, @Nullable String subDirectory) {
                if (mApplication == null) {
                    throw new NullPointerException("mApplication不得为空,是否尚未初始化");
                }
                if (mRootPath == null) {
                    throw new NullPointerException("mRootPath不得为空,是否尚未初始化");
                }

                StringBuilder builder = new StringBuilder();

                switch (getStorageType()) {
                    case StorageType.External:
                        builder.append(mApplication.getExternalFilesDir(parentType).getAbsolutePath());
                        break;
                    case StorageType.Storage:
                        builder.append(Environment.getExternalStorageDirectory());
                        builder.append("/");
                        builder.append(mRootPath);
                        builder.append("/");
                        builder.append(parentType);
                        break;
                }

                builder.append("/");
                builder.append(fileType);
                if (!TextUtils.isEmpty(subDirectory)) {
                    builder.append("/");
                    builder.append(subDirectory);
                }

                //创建用到的文件夹
                FileUtils.mkdir(builder.toString());

                return builder.toString();
            }

            @Override
            public List<String> getCacheDirectory() {
                List<String> cacheList = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= 19) {
                    cacheList.add(mApplication.getExternalFilesDir(parentType).getAbsolutePath());
                }
                cacheList.add(Environment.getExternalStorageDirectory() + "/" + mRootPath + "/" + PathParentType.TEMP);

                return cacheList;
            }
        };
    }

}
