package com.milo.libbase.framework.mvp;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 标题：优化后的FragmentAdapter
 * 功能：
 * 备注：
 * <p>
 * Created by Milo  2020/2/27
 * E-Mail : 303767416@qq.com
 */
public abstract class FZBaseFragmentAdapter2 extends FragmentPagerAdapter {

    protected SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public FZBaseFragmentAdapter2(@NonNull FragmentManager fm) {
        super(fm);
    }

    public abstract Fragment createFragment(int position);

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (getRegisteredFragment(position) != null) {
            return getRegisteredFragment(position);
        }
        return createFragment(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    /**
     * 获取已注册的Fragment
     *
     * @param position
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}
