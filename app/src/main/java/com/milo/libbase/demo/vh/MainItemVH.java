package com.milo.libbase.demo.vh;

import android.view.View;

import com.milo.libbase.demo.R;
import com.milo.libbase.demo.data.bean.MainItemData;
import com.milo.libbase.demo.databinding.VhMainItemBinding;
import com.zhl.commonadapter.BaseViewHolder;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 6/3/21
 */
public class MainItemVH extends BaseViewHolder<MainItemData> {

    protected VhMainItemBinding mBinding;

    @Override
    public void findView(View view) {
        mBinding = VhMainItemBinding.bind(view);
    }

    @Override
    public void updateView(MainItemData data, int position) {
        mBinding.tvItemName.setText(data.getName());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.vh_main_item;
    }

}
