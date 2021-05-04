package com.milo.libbase.widget.videoview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.milo.libbase.AppConfig;
import com.milo.libbase.R;
import com.milo.libbase.utils.Utils;

import java.util.Map;

/**
 * 标题：清晰度选择弹出框 - 2代
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/12/4 12:15
 */
public class DefinitionPopupWindow<T> extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private T       mVideoView;

    private Map<FZVideoDefinition, FZVideoData> mVideoDefinitionMap;
    private DefinitionPermission<T>             mDefinitionPermission;

    private ViewGroup mLayoutStandard;
    private ViewGroup mLayoutHeight;
    private ViewGroup mLayoutSuper;

    private TextView mTvStandard;
    private TextView mTvStandardSize;
    private TextView mTvHeight;
    private TextView mTvHeightSize;
    private TextView mTvSuper;
    private TextView mTvSuperSize;

    /**
     * 竖屏状态下，video的高度
     */
    public int mVideoHeightInPortrait;

    private DefinitionPopupWindow.DefinitionChangedListener mDefinitionChangedListener;

    /**
     * 清晰度变化监听
     */
    public interface DefinitionChangedListener {

        /**
         * 清晰度变化
         *
         * @param videoDefinition - 清晰度
         * @param videoUrl        - 视频地址
         */
        void onDefiniChanged(FZVideoDefinition videoDefinition, String videoUrl);

    }

    /**
     * 清晰度权限接口
     */
    public interface DefinitionPermission<T> {

        /**
         * 是否有权限
         *
         * @param videoDefinition
         * @return
         */
        boolean hasPermission(FZVideoDefinition videoDefinition);

        /**
         * 申请权限
         *
         * @return
         */
        void requestPermission(T videoView, FZVideoDefinition videoDefinition);

    }

    public DefinitionPopupWindow(@NonNull Context context, T videoView, @NonNull Map<FZVideoDefinition, FZVideoData> videoDefinitionMap,
                                 @NonNull DefinitionPopupWindow.DefinitionChangedListener definitionChangedListener) {
        this(context, videoView, videoDefinitionMap, AppConfig.libBase.getDefinitionPermission(), definitionChangedListener);
    }

    public DefinitionPopupWindow(@NonNull Context context, T videoView, @NonNull Map<FZVideoDefinition, FZVideoData> videoDefinitionMap,
                                 @NonNull DefinitionPermission<T> definitionPermission, @NonNull DefinitionPopupWindow.DefinitionChangedListener definitionChangedListener) {
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.mContext = context;
        this.mVideoView = videoView;
        this.mVideoDefinitionMap = videoDefinitionMap;
        this.mDefinitionPermission = definitionPermission;
        this.mDefinitionChangedListener = definitionChangedListener;
        init();
    }

    @Override
    public void onClick(View v) {
        if (v == mLayoutStandard) {
            mDefinitionChangedListener.onDefiniChanged(FZVideoDefinition.STANDARD, mVideoDefinitionMap.get(FZVideoDefinition.STANDARD).mVideoUrl);
            dismiss();
        } else if (v == mLayoutHeight) {
            mDefinitionChangedListener.onDefiniChanged(FZVideoDefinition.HEIGHT, mVideoDefinitionMap.get(FZVideoDefinition.HEIGHT).mVideoUrl);
            dismiss();
        } else if (v == mLayoutSuper) {
            if(mDefinitionPermission != null) {
                if (mDefinitionPermission.hasPermission(FZVideoDefinition.SUPER)) {
                    mDefinitionChangedListener.onDefiniChanged(FZVideoDefinition.SUPER, mVideoDefinitionMap.get(FZVideoDefinition.SUPER).mVideoUrl);
                } else {
                    mDefinitionPermission.requestPermission(mVideoView, FZVideoDefinition.SUPER);
                }
            } else {
                mDefinitionChangedListener.onDefiniChanged(FZVideoDefinition.SUPER, mVideoDefinitionMap.get(FZVideoDefinition.SUPER).mVideoUrl);
            }
            dismiss();
        }
    }

    private void init() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.libbase_popup_definition, null);
        setContentView(contentView);
        mLayoutStandard = contentView.findViewById(R.id.mLayoutStandard);
        mLayoutStandard.setOnClickListener(this);
        mLayoutHeight = contentView.findViewById(R.id.mLayoutHeight);
        mLayoutHeight.setOnClickListener(this);
        mLayoutSuper = contentView.findViewById(R.id.mLayoutSuper);
        mLayoutSuper.setOnClickListener(this);

        mTvStandard = contentView.findViewById(R.id.mTvStandard);
        mTvStandardSize = contentView.findViewById(R.id.mTvStandardSize);
        mTvStandardSize.setText("(" + mVideoDefinitionMap.get(FZVideoDefinition.STANDARD).mVideoSize + ")");
        mTvHeight = contentView.findViewById(R.id.mTvHeight);
        mTvHeightSize = contentView.findViewById(R.id.mTvHeightSize);
        mTvSuper = contentView.findViewById(R.id.mTvSuper);
        mTvSuperSize = contentView.findViewById(R.id.mTvSuperSize);


        for (Map.Entry<FZVideoDefinition, FZVideoData> item : mVideoDefinitionMap.entrySet()) {
            if (item.getKey() == FZVideoDefinition.HEIGHT && item.getValue() != null) {
                mLayoutHeight.setVisibility(View.VISIBLE);
                mTvHeightSize.setText("(" + item.getValue().mVideoSize + ")");
            } else if (item.getKey() == FZVideoDefinition.SUPER && item.getValue() != null) {
                mLayoutSuper.setVisibility(View.VISIBLE);
                mTvSuperSize.setText("(" + item.getValue().mVideoSize + ")");
            }
        }

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
    }

    /**
     * 设置屏幕方向
     *
     * @param screenOrientation
     */
    public void setScreenOrientation(int screenOrientation) {
        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setWidth(Utils.dp2px(mContext, 160));
            this.setHeight(mVideoHeightInPortrait);
        } else {
            this.setWidth(Utils.dp2px(mContext, 200));
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    /**
     * 设置当前清晰度
     *
     * @param videoDefinition
     */
    public void setCurrentVideoDefinition(FZVideoDefinition videoDefinition) {
        mTvStandard.setTextColor(Color.WHITE);
        mTvStandardSize.setTextColor(Color.WHITE);
        mTvHeight.setTextColor(Color.WHITE);
        mTvHeightSize.setTextColor(Color.WHITE);
        mTvSuper.setTextColor(Color.WHITE);
        mTvSuperSize.setTextColor(Color.WHITE);

        if (videoDefinition == FZVideoDefinition.STANDARD) {
            mTvStandard.setTextColor(mContext.getResources().getColor(R.color.libbase_c1));
            mTvStandardSize.setTextColor(mContext.getResources().getColor(R.color.libbase_c1));
        } else if (videoDefinition == FZVideoDefinition.HEIGHT) {
            mTvHeight.setTextColor(mContext.getResources().getColor(R.color.libbase_c1));
            mTvHeightSize.setTextColor(mContext.getResources().getColor(R.color.libbase_c1));
        } else if (videoDefinition == FZVideoDefinition.SUPER) {
            mTvSuper.setTextColor(mContext.getResources().getColor(R.color.libbase_c1));
            mTvSuperSize.setTextColor(mContext.getResources().getColor(R.color.libbase_c1));
        }
    }

}
