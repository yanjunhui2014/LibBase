package com.milo.libbase.demo.data.factory;

import com.milo.libbase.demo.data.bean.MainItemData;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 6/3/21
 */
public class CommonFactory {

    public static MainItemData createMainItemData(final int type, final String name){
        return new MainItemData() {
            @Override
            public int getType() {
                return type;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

}
