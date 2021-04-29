package com.milo.libbase.utils.filefactory;

import androidx.annotation.StringDef;

/**
 * Title：文件根目录类型
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/10/27
 */
@StringDef({PathParentType.COMMON, PathParentType.TEMP})
public @interface PathParentType {

    String COMMON                    = "common";
    String TEMP                      = "temp";

}
