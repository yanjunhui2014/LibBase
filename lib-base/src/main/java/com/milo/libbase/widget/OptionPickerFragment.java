package com.milo.libbase.widget;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.milo.libbase.R;
import com.milo.libbase.databinding.LibbaseFragmentOptionPickerBinding;
import com.milo.libbase.databinding.LibbaseVhOptionPickerBinding;
import com.milo.libbase.utils.Utils;
import com.zhl.commonadapter.BaseViewHolder;
import com.zhl.commonadapter.CommonRecyclerAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Title：多选对话框 - Fragment
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 9/22/21
 */
public class OptionPickerFragment<T> extends BottomSheetDialogFragment implements View.OnClickListener {

    private LibbaseFragmentOptionPickerBinding pickerBinding;

    private CommonRecyclerAdapter<T> adapter;
    private LinearSnapHelper mDailyLinearSnapHelper;

    private String title;
    private ArrayList list;
    private OnPickerListener<T> onPickerListener;

    private int currentPosition;
    private boolean isCancel;

    public interface OnPickerListener<T> extends Serializable {
        void onCancel();

        void onConfirm(int position, T t);

        default void onDestroy() {
        }
    }

    public static <T> OptionPickerFragment<T> newInstance(String title, ArrayList list, OnPickerListener<T> onPickerListener) {
        OptionPickerFragment<T> fragment = new OptionPickerFragment<T>();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putSerializable("list", list);
        bundle.putSerializable("listener", onPickerListener);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getTheme() {
        return R.style.libbase_TranslucentHalfBottomSheetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            list = (ArrayList) getArguments().getSerializable("list");
            onPickerListener = (OnPickerListener<T>) getArguments().getSerializable("listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pickerBinding = LibbaseFragmentOptionPickerBinding.inflate(inflater, container, false);
        initView();
        return pickerBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v == pickerBinding.tvCancel) {
            isCancel = true;
            onPickerListener.onCancel();
            dismiss();
        } else if (v == pickerBinding.tvConfirm) {
            if(!Utils.isEmpty(list)) {
                onPickerListener.onConfirm(currentPosition - 1, (T) list.get(currentPosition));
            }
            dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!Utils.isEmpty(list) && !isCancel) {
            onPickerListener.onConfirm(currentPosition - 1, (T) list.get(currentPosition));
        }
        onPickerListener.onDestroy();
    }

    /**
     * 如果在 {@link #show(FragmentManager, String)} 之后马上调用，list 会认定为空的，所以建议延时500ms调用
     *
     * @param defaultPosition
     */
    public void setDefaultPosition(final int defaultPosition) {
        if (Utils.isEmpty(list)) {
            return;
        }
        if ((defaultPosition + 1) >= list.size() || defaultPosition < 0) {
            return;
        }

        try {
            pickerBinding.recyclerView.smoothScrollBy(0, Utils.dp2px(requireContext(), 50) * (defaultPosition));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void showConfirm() {
        pickerBinding.dividerConfirm.setVisibility(View.VISIBLE);
        pickerBinding.tvConfirm.setVisibility(View.VISIBLE);
    }

    public void hideConfirm() {
        pickerBinding.dividerConfirm.setVisibility(View.GONE);
        pickerBinding.tvConfirm.setVisibility(View.GONE);
    }

    private void initView() {
        pickerBinding.tvTitle.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            pickerBinding.tvTitle.setText(title);
        }
        pickerBinding.tvCancel.setOnClickListener(this);
        pickerBinding.tvConfirm.setOnClickListener(this);

        if (Utils.isEmpty(list)) {
            pickerBinding.viewChooseBg.setVisibility(View.GONE);
            pickerBinding.tvEmpty.setVisibility(View.VISIBLE);
            pickerBinding.recyclerView.setVisibility(View.GONE);
            return;
        }

        mDailyLinearSnapHelper = new LinearSnapHelper();
        mDailyLinearSnapHelper.attachToRecyclerView(pickerBinding.recyclerView);
        pickerBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View view = mDailyLinearSnapHelper.findSnapView(pickerBinding.recyclerView.getLayoutManager());
                    if (view != null) {
                        int position = pickerBinding.recyclerView.getChildAdapterPosition(view);
                        selected(position);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        list.add(0, obtainPlaceHolderItem());
        list.add(obtainPlaceHolderItem());

        adapter = new CommonRecyclerAdapter<T>(list) {
            @Override
            public BaseViewHolder<T> createViewHolder(int type) {
                return new OptionPickerVH(() -> currentPosition);
            }
        };
        pickerBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        pickerBinding.recyclerView.setAdapter(adapter);
        selected(1);
    }

    private void selected(int position) {
        this.currentPosition = position;
        adapter.notifyDataSetChanged();
    }

    private PlaceHolderItem obtainPlaceHolderItem() {
        return new PlaceHolderItem();
    }

    private static class OptionPickerVH extends BaseViewHolder {

        LibbaseVhOptionPickerBinding vhBinding;

        private Provider provider;

        interface Provider {
            int curPosition();
        }

        public OptionPickerVH(Provider provider) {
            this.provider = provider;
        }

        @Override
        public void findView(View view) {
            vhBinding = LibbaseVhOptionPickerBinding.bind(view);
        }

        @Override
        public void updateView(Object data, int position) {
            if (provider.curPosition() == position) {
                vhBinding.tvItem.setTextSize(18);
                vhBinding.tvItem.setTextColor(Color.parseColor("#2ACF6F"));
            } else {
                vhBinding.tvItem.setTextSize(16);
                vhBinding.tvItem.setTextColor(Color.parseColor("#80333333"));
            }

            vhBinding.tvItem.setText(data.toString());
        }

        @Override
        public int getLayoutResId() {
            return R.layout.libbase_vh_option_picker;
        }

    }

    private static class PlaceHolderItem {
        @NonNull
        @Override
        public String toString() {
            return "";
        }
    }

}
