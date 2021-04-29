package com.milo.libbase.utils.filefactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/10/27
 */
public interface PathProvider {

    int getStorageType();

    String getPathParentType();

    String getFilePath(String urlOrName);

    String getFilePath(String urlOrName, boolean keepSuffix);

    String getFilePath(String subDirectory, String urlOrName);

    String getFilePath(String subDirectory, String urlOrName, boolean keepSuffix);

    String getFilePathByMake();

    String getFilePathByMake(@FileType String fileType, boolean showSuffix);

    String getFilePathByMake(@FileType String fileType, boolean showSuffix, String specifySuffix);

    String getFilePathByMake(@FileType String fileType, String subDirectory, boolean showSuffix, String specifySuffix);

    /**
     * @param fileType     - 文件类型（二级目录类型）
     * @param subDirectory - 三级目录类型
     * @param fileName     - 指定文件名
     * @return
     */
    String getFilePath(@FileType String fileType, @Nullable String subDirectory, @NonNull String fileName);

    String getFileDirectory(@FileType String fileType);

    String getFileDirectory(@FileType String fileType, @Nullable String subDirectory);

    List<String> getCacheDirectory();

}
