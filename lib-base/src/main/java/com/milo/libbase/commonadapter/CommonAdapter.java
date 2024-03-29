package com.milo.libbase.commonadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

/**
 * Created by zhouhl on 2016/2/22.
 * CommonAdapter
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> mDatas;

    public CommonAdapter() {

    }

    public CommonAdapter(List<T> datas) {
        this.mDatas = datas;
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        } else {
            return mDatas.size();
        }
    }

    @Override
    public T getItem(int position) {
        if (position >= 0 && position < mDatas.size()) {
            return mDatas.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;

        if (convertView == null) {
            holder = createViewHolder(getItemViewType(position));
            convertView = holder.getDataBindingRoot(parent.getContext(), parent);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(holder.getLayoutResId(), parent, false);
            }
            holder.bindView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }

        T data = mDatas.get(position);
        holder.updateView(data, position);
        if (data instanceof ExposeItem) {
            ExposeItem exposeItem = (ExposeItem) data;
            if (!exposeItem.isTracked()) {
                holder.onExpose(data, position);
                exposeItem.setTracked(true);
            }
        }

        return convertView;
    }

    public abstract BaseViewHolder<T> createViewHolder(int type);
}
