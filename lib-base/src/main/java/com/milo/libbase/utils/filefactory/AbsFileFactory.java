package com.milo.libbase.utils.filefactory;

import java.util.Set;

/**
 * Title：路径提供者的抽象工厂
 * Describe：
 * Remark：写成工厂有两个目的。第一、提高重合代码复用率；第二、方便未来扩展模块定制化
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/10/27
 */
public abstract class AbsFileFactory {

    public abstract Set<String> getAllCacheList();

    public abstract PathProvider createPathProvider(@StorageType int storageType, @PathParentType String parentType);

}
