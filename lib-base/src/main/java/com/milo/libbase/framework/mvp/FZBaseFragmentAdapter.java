package com.milo.libbase.framework.mvp;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated 这种写法有安全隐患，具体可参照 <a>https://mp.weixin.qq.com/s/MOWdbI5IREjQP1Px-WJY1Q</a>， 使用 {@link FZBaseFragmentAdapter2} 替代
 */
@Deprecated
public class FZBaseFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private List<String>   mTitles   = new ArrayList<>();

    public FZBaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public FZBaseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setTitle(List<String> titles) {
        mTitles.addAll(titles);
    }

    public void addPager(Fragment pager) {
        if (fragments == null) {
            fragments = new ArrayList<Fragment>();
        }
        fragments.add(pager);
    }

    public void addPager(Fragment pager, String title) {
        if (fragments == null) {
            fragments = new ArrayList<Fragment>();
        }
        fragments.add(pager);
        mTitles.add(title);
    }

    public void setFragments(List<Fragment> fragments) {
        if (this.fragments == null) {
            this.fragments = new ArrayList<Fragment>();
        }
        this.fragments.clear();
        this.fragments.addAll(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null || fragments.size() == 0) {
            return null;
        }
        if (fragments.size() <= position) {
            return fragments.get(fragments.size() - 1);
        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < mTitles.size()) {
            return mTitles.get(position);
        } else {
            return super.getPageTitle(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

}
